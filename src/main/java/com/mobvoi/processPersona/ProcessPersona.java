// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona;


import static com.mobvoi.util.CallService.requestUserTagsScore;
import static com.mobvoi.util.Const.HIVE_ACCOUNT_COL;
import static com.mobvoi.util.Const.JSON_KW_ID;
import static com.mobvoi.util.ConvertData.convertRowToPointInfo;
import static com.mobvoi.util.ConvertData.formatRowMapForAccount;

import com.alibaba.fastjson.JSON;
import com.mobvoi.processPersona.bean.PersonaInfo;
import com.mobvoi.processPersona.bean.PointInfo;
import com.mobvoi.processPersona.bean.TagInfo;
import com.mobvoi.util.CallService;
import com.mobvoi.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.spark.Partitioner;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

/**
 * 用户标签计算 spark job created by zhhgao@mobvoi.com on 18-8-21
 */
public class ProcessPersona {

  /**
   * spark job 名称
   */
  private static String APPLICATION_NAME = PropertiesUtil
      .getProcessPersonaConf("spark.application.name");
  /**
   * hive warehouse 地址
   */
  private static String HIVE_WARE_HOUSE = PropertiesUtil
      .getProcessPersonaConf("spark.sql.warehouse.dir");
  /**
   * hive database
   */
  private static String HIVE_DATABASE = PropertiesUtil
      .getProcessPersonaConf("hive.database");
  /**
   * hive sql
   */
  private static String HIVE_SQL = PropertiesUtil
      .getProcessPersonaConf("hive.sql");

  /**
   * 用户标签时 间衰减系数
   */
  private static double USER_TAG_TIME_REDUCE_COEFFICIENT = Double
      .parseDouble(PropertiesUtil
          .getProcessPersonaConf("user.tag.time.reduce.coefficient"));
  /**
   * 过滤音乐列表 播放百分比 阈值
   */
  private static double FILTER_MUSIC_PLAY_PROPORTION = Double
      .parseDouble(PropertiesUtil
          .getProcessPersonaConf("filter.music.play.proportion"));
  /**
   * 过滤音乐列表 每日播放次数 阈值
   */
  private static double FILTER_MUSIC_PLAY_TIMES = Double
      .parseDouble(
          PropertiesUtil.getProcessPersonaConf("filter.music.play.times"));
  /**
   * log
   */
  private static Logger log = Logger.getLogger(ProcessPersona.class);

  /**
   * 1.更新音乐过滤表 2.计算本次用户标签权值 3.根据用户是否已经拥有标签，进行增加/更新用户标签操作
   */
  public static void main(String[] args) {

    SparkSession ss = SparkSession.builder()
        .appName(APPLICATION_NAME)
        .master("local")
        .config("spark.sql.warehouse.dir", HIVE_WARE_HOUSE)
        .enableHiveSupport()
        .getOrCreate();

    /**
     * 设置全量标签 广播变量
     */
    //调用接口读取所有标签ID和标签名称的HashMap，存为广播变量
    JavaSparkContext jsc = JavaSparkContext.fromSparkContext(ss.sparkContext());
    Map<String, String> tagsMap = CallService.requestAllMusicTags();
    log.info("ProcessPersona broadcast tagsCast size : " + tagsMap.size());
    Broadcast<Map<String, String>> tagsCast = jsc.broadcast(tagsMap);

    //切换使用的hive数据库
    ss.sql("use " + HIVE_DATABASE);
//    ss.read().json("/Users/luck/Downloads/1.json").show();
//    Dataset<Row> rowDataSet = ss.sql(HIVE_SQL).cache();
//    Dataset<Row> rowDataSet = ss.read().json("/Users/luck/Downloads/fake-pointData201810102014.json");
    Dataset<Row> rowDataSet = ss.read()
        .json("/Users/luck/Downloads/fake-pointData201810121808.json");

    //获取去重后的kwIDs
    JavaPairRDD<String, Integer> kwIDsRDD = rowDataSet.select(HIVE_ACCOUNT_COL).toJavaRDD()
//    JavaPairRDD<String, Integer> kwIDsRDD = rowDataSet.toJavaRDD()
        .mapToPair(
            (PairFunction<Row, String, Integer>) row -> {
              Map<String, String> accountMap = formatRowMapForAccount(row);
              String kwID = String.valueOf(accountMap.get(JSON_KW_ID));
              return new Tuple2<>(kwID, 0);
            })
        //过滤kwID == null 数据
        .filter((Function<Tuple2<String, Integer>, Boolean>) tuple2 -> null != tuple2._1)
        //去重处理
        .distinct()
        //缓存kwUserInfoJavaPairRDD，为下次与埋点数据进行join操作
        .cache();//如果这里OOM,修改为.persist(MEMORY_AND_DISK)

    /**
     * 计算用户画像标签权值
     */
    //从hive表中取用户画像计算相关值和kwID
    JavaRDD<PersonaInfo> personaInfoJavaRDD = rowDataSet.toJavaRDD().mapToPair(
        (PairFunction<Row, String, PointInfo>) row -> {

          PointInfo pointInfo = convertRowToPointInfo(row);
          return new Tuple2<>(pointInfo.getKwID(), pointInfo);
        })
        .groupByKey()//按照key：kwID进行分组
        .rightOuterJoin(kwIDsRDD, new Partitioner() {
          @Override
          public int numPartitions() {
            return 2;
          }

          @Override
          public int getPartition(Object o) {
            return (o.toString()).hashCode() % numPartitions();
          }
        })
        //遍历join后的结果集
        .map(
            (Function<Tuple2<String, Tuple2<Optional<Iterable<PointInfo>>, Integer>>, PersonaInfo>) v1 -> {
              PersonaInfo personaInfo = new PersonaInfo();
              String kwID = v1._1;
              personaInfo.setKwID(kwID);
              Map<String, TagInfo> userLastTagInfoMap = requestUserTagsScore(
                  kwID);//key为tagID，value为tagInfo

              Tuple2<Optional<Iterable<PointInfo>>, Integer> tuple2 = v1._2;
              Optional<Iterable<PointInfo>> optional = tuple2._1;
              if (optional.isPresent()) {
                Iterator<PointInfo> infoIterator = optional.get().iterator();

                List<TagInfo> addTagInfoList = new ArrayList<>();
                List<TagInfo> updateTagInfoList = new ArrayList<>();
                List<PointInfo> pointInfoList = new ArrayList<>();

                while (infoIterator.hasNext()) {
                  PointInfo pointInfo = infoIterator.next();
                  List<String> tagNameList = pointInfo.getTagNameList();
                  double thisPeriodScore = pointInfo.getThisPeriodScore();//本次标签权值
                  double finalPeriodScore;//计算后的最终标签权值

                  /**
                   * 计算最终用户标签权值（调接口获取上次权值*时间衰减系数 + 本次权值）
                   */
                  if (userLastTagInfoMap.size() > 0) {
                    Map<String, String> tagCastMap = tagsCast.getValue();
                    if (null != tagCastMap && tagCastMap.size() > 0) {
                      for (String tagName : tagNameList) {
                        TagInfo tagInfo = new TagInfo();
                        //用从广播变量获取的 标签ID和标签名称的HashMap，把所有标签名称转换为标签ID
                        String tagID = tagCastMap.get(tagName);
                        tagInfo.setTagID(tagID);
                        tagInfo.setTagName(tagName);
                        if (null != userLastTagInfoMap.get(tagID)) {
                          //不等于null说明用户拥有该标签 进行累加计算
                          finalPeriodScore =
                              thisPeriodScore + userLastTagInfoMap.get(tagID).getPeriodScore()
                                  * USER_TAG_TIME_REDUCE_COEFFICIENT;
                          tagInfo.setPeriodScore(finalPeriodScore);
                          updateTagInfoList.add(tagInfo);
                        } else {
                          //说明这个标签是用户新拥有的
                          tagInfo.setPeriodScore(thisPeriodScore);
                          addTagInfoList.add(tagInfo);
                        }
                      }
                    }
                  }
                  pointInfoList.add(pointInfo);
                }
                personaInfo.setAddTagInfoList(addTagInfoList);
                personaInfo.setUpdateTagInfoList(updateTagInfoList);
                personaInfo.setPointInfoList(pointInfoList);
                log.info("+++++++ personaInfo : " + JSON.toJSONString(personaInfo));
                //同步用户画像标签权值
                CallService.syncPersona(personaInfo);
              }
              return personaInfo;
            })
        //缓存结果，为了计算过滤音乐列表
        .cache();

    /**
     * 处理过滤音乐列表
     */
//    JavaRDD<FilterMusicInfo> filterMusicRDD = personaInfoJavaRDD.mapPartitions(
//        (FlatMapFunction<Iterator<PersonaInfo>, PointInfo>) personaInfoIterator -> {
//          List<PointInfo> pointInfos = new ArrayList<>();
//          while (personaInfoIterator.hasNext()) {
//            PersonaInfo personaInfo = personaInfoIterator.next();
//            pointInfos.addAll(personaInfo.getPointInfoList());
//          }
//          return Arrays.asList((PointInfo[]) pointInfos.toArray()).iterator();
//        }).mapToPair((PairFunction<PointInfo, String, FilterRule>) pointInfo -> {
//      FilterRule filterRule = new FilterRule();
//      String kwID = pointInfo.getKwID();
//      String musicID = pointInfo.getMusicID();
//      String key = kwID + "," + musicID;
//      filterRule.setKwID(kwID);
//      filterRule.setMusicID(pointInfo.getMusicID());
//      filterRule.setPlayProportion(pointInfo.getPlayProportion());
//      filterRule.setPlayTimes(0);
//      return new Tuple2<>(key, filterRule);
//    }).groupByKey().map((Function<Tuple2<String, Iterable<FilterRule>>, FilterMusicInfo>) v1 -> {
//      FilterMusicInfo filterMusicInfo = new FilterMusicInfo();
//      int playTimes = 0;
//      for (FilterRule filterRule : v1._2) {
//        double playProportion = filterRule.getPlayProportion();
//        //大于播放比例设定的阈值，则算一次播放
//        if (playProportion >= FILTER_MUSIC_PLAY_PROPORTION) {
//          playTimes = playTimes + filterRule.getPlayTimes();
//        }
//      }
//      //如果大于播放次数阈值，则将该用户的这首音乐，加入该用户的过滤音乐列表中
//      if (playTimes >= FILTER_MUSIC_PLAY_TIMES) {
//        String[] key = v1._1.split(",");
//        if (key.length == 2) {
//          String kwID = key[0];
//          String musicID = key[1];
//          filterMusicInfo.setKwID(kwID);
//          filterMusicInfo.setMusicID(musicID);
//        }
//      }
//      //过滤kwID和musicID为空的记录，不为空则更新过滤音乐列表
//      if (null != filterMusicInfo.getKwID() && null != filterMusicInfo.getMusicID()) {
//        CallService.updateFilterMusicList(filterMusicInfo);
//      }
//      return filterMusicInfo;
//    });

    //为了以上所有转换操作执行，必须设置一个action来触发，first代价相对较小
//    personaInfoJavaRDD.first();
    personaInfoJavaRDD.count();
//    filterMusicRDD.first();
//    kwIDsRDD.first();
  }
}