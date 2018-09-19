// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona;


import static com.mobvoi.util.CalculateScore.processUserTagsScore;
import static com.mobvoi.util.CallService.requestUserTagsScore;
import static com.mobvoi.util.ConvertData.convertMusicAction;
import static com.mobvoi.util.ConvertData.convertMusicComeFrom;
import static com.mobvoi.util.ConvertData.formatRowMap;
import static com.mobvoi.util.ConvertData.getTagNames;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.processPersona.bean.FilterMusicInfo;
import com.mobvoi.processPersona.bean.FilterRule;
import com.mobvoi.processPersona.bean.PersonaInfo;
import com.mobvoi.processPersona.bean.TagInfo;
import com.mobvoi.util.CallService;
import com.mobvoi.util.Const;
import com.mobvoi.util.HttpUtil;
import com.mobvoi.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
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
      .getProperties("spark.application.name", Const.CONFIG_PROCESS_PERSONA);
  /**
   * hive warehouse 地址
   */
  private static String HIVE_WARE_HOUSE = PropertiesUtil
      .getProperties("spark.sql.warehouse.dir", Const.CONFIG_PROCESS_PERSONA);
  /**
   * hive database
   */
  private static String HIVE_DATABASE = PropertiesUtil
      .getProperties("hive.database", Const.CONFIG_PROCESS_PERSONA);
  /**
   * hive sql
   */
  private static String HIVE_SQL = PropertiesUtil
      .getProperties("hive.sql", Const.CONFIG_PROCESS_PERSONA);
  /**
   * 选择hive表查询出来的列
   */
  private static String HIVE_SELECTED_COL = PropertiesUtil
      .getProperties("hive.selected.col", Const.CONFIG_PROCESS_PERSONA);

  /**
   * 更新用户标签信息的接口url
   */
  private static String UPDATE_USER_TAGS_URL = PropertiesUtil
      .getProperties("update.user.tags.url", Const.CONFIG_PROCESS_PERSONA);
  /**
   * 更新音乐过滤表的接口url
   */
  private static String UPDATE_FILTER_MUSIC_URL = PropertiesUtil
      .getProperties("update.filter.music.url", Const.CONFIG_PROCESS_PERSONA);
  /**
   * 用户标签时 间衰减系数
   */
  private static double USER_TAG_TIME_REDUCE_COEFFICIENT = Double
      .parseDouble(PropertiesUtil
          .getProperties("user.tag.time.reduce.coefficient", Const.CONFIG_PROCESS_PERSONA));
  /**
   * 过滤音乐列表 播放百分比 阈值
   */
  private static double FILTER_MUSIC_PLAY_PROPORTION = Double
      .parseDouble(PropertiesUtil
          .getProperties("filter.music.play.proportion", Const.CONFIG_PROCESS_PERSONA));
  /**
   * 过滤音乐列表 每日播放次数 阈值
   */
  private static double FILTER_MUSIC_PLAY_TIMES = Double
      .parseDouble(
          PropertiesUtil.getProperties("filter.music.play.times", Const.CONFIG_PROCESS_PERSONA));

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
        .config("spark.sql.warehouse.dir", HIVE_WARE_HOUSE)
        .enableHiveSupport().getOrCreate();

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

    //从hive表中取所有埋点信息的properties字段内容，做为最初始的处理数据集。
    JavaRDD<Row> javaRDD = ss.sql(HIVE_SQL).select(HIVE_SELECTED_COL).toJavaRDD()
        .cache();//如果这里OOM,修改为.persist(MEMORY_AND_DISK)

    JavaRDD<PersonaInfo> personaInfoJavaRDD = javaRDD.map(
        (Function<Row, PersonaInfo>) row -> {
          PersonaInfo personaInfo = new PersonaInfo();

          Map<String, Object> rowMap = formatRowMap(row);

          String userID = String.valueOf(rowMap.get("userID"));//酷我用户ID
          //TODO 先取UserID 调用接口 再进行后续计算
          String musicID = String.valueOf(rowMap.get("musicID"));//音乐ID
          double playProportion = Double
              .parseDouble(String.valueOf(rowMap.get("playProportion")));//播放比例
          Integer musicAction = convertMusicAction(rowMap);//音乐动作：详见Const常量类。
          Integer comeFrom = convertMusicComeFrom(rowMap);//音乐来源：详见Const常量类。

          double finalPeriodScore;//计算后的最终标签权值
          //计算本次标签权值
          double thisPeriodScore = processUserTagsScore(playProportion, musicAction, comeFrom);

          /**
           * 计算最终用户标签权值（调接口获取上次权值*时间衰减系数 + 本次权值）
           */
          //获取该用户所有标签
          Map<String, TagInfo> userLastTagInfoMap = requestUserTagsScore(
              userID);//key为tagID，value为tagInfo
          List<TagInfo> tagInfoList = new ArrayList<>();
          if (userLastTagInfoMap.size() > 0) {
            //计算标签权值
            List<String> tagNameList = getTagNames(rowMap);

            Map<String, String> tagCastMap = tagsCast.getValue();
            if (null != tagCastMap && tagCastMap.size() > 0) {
              for (String tagName : tagNameList) {
                TagInfo tagInfo = new TagInfo();
                //用从广播变量获取的 标签ID和标签名称的HashMap，把所有标签名称转换为标签ID
                String tagID = tagCastMap.get(tagName);
                tagInfo.setTagID(tagID);
                if (null != userLastTagInfoMap.get(tagID)) {
                  //不等于null说明用户拥有该标签 进行累加计算
                  finalPeriodScore =
                      thisPeriodScore + userLastTagInfoMap.get(tagID).getPeriodScore()
                          * USER_TAG_TIME_REDUCE_COEFFICIENT;
                } else {
                  //说明这个标签是用户新拥有的
                  finalPeriodScore = thisPeriodScore;
                }
                tagInfo.setPeriodScore(finalPeriodScore);
                tagInfoList.add(tagInfo);
              }
            }
          }

          personaInfo.setUserID(userID);
          personaInfo.setMusicID(musicID);
          personaInfo.setTagInfoList(tagInfoList);
          return personaInfo;
        });

    /**
     * 过滤音乐列表处理
     */
    //生成Tuple结构
    JavaPairRDD<String, FilterRule> mapToPairRdd = javaRDD.mapToPair(
        (PairFunction<Row, String, FilterRule>) row -> {
          Map<String, Object> rowMap = formatRowMap(row);
          String userID = String.valueOf(rowMap.get("userID"));//酷我用户ID
          //TODO 先取UserID 调用接口 再进行后续计算
          String musicID = String.valueOf(rowMap.get("musicID"));//音乐ID
          double playProportion = Double
              .parseDouble(String.valueOf(rowMap.get("playProportion")));//播放比例
          FilterRule filterRule = new FilterRule();
          filterRule.setUserID(userID);
          filterRule.setMusicID(musicID);
          filterRule.setPlayProportion(playProportion);
          filterRule.setPlayTimes(0);
          return new Tuple2<>(musicID, filterRule);
        });

    //根据规则计算 符合要求的音乐播放次数
    JavaPairRDD<String, FilterRule> reducePairRdd = mapToPairRdd.reduceByKey(
        (Function2<FilterRule, FilterRule, FilterRule>) (v1, v2) -> {
          FilterRule filterRule = new FilterRule();
          String v1MusicID = v1.getMusicID();
          String v2MusicID = v2.getMusicID();
          String v1UserID = v1.getUserID();
          String v2UserID = v1.getUserID();
          double v1PlayProportion = v1.getPlayProportion();
          double v2PlayProportion = v2.getPlayProportion();
          int playTimes;

          //如果是同一个用户，用一首音乐，播放的比例大于阈值，则把两次播放次数相加
          if (v1UserID.equals(v2UserID) &&
              v1MusicID.equals(v2MusicID) &&
              v1PlayProportion >= FILTER_MUSIC_PLAY_PROPORTION &&
              v2PlayProportion >= FILTER_MUSIC_PLAY_PROPORTION) {
            playTimes = v1.getPlayTimes() + v2.getPlayTimes();
            filterRule.setUserID(v1UserID);
            filterRule.setPlayTimes(playTimes);
            filterRule.setMusicID(v1MusicID);
          } else {
            filterRule.setUserID(null);
          }
          return filterRule;
        });
    //过滤没有计数的记录
    JavaPairRDD<String, FilterRule> filterRdd = reducePairRdd.filter(
        (Function<Tuple2<String, FilterRule>, Boolean>) v1 -> {
          //TODO 待逻辑验证通过后，改成lamada表达式
          if (null == v1._2.getUserID()) {
            return false;
          } else {
            return true;
          }
        });
    //生成过滤音乐列表
    JavaRDD<FilterMusicInfo> filterMusicList = filterRdd
        .map((Function<Tuple2<String, FilterRule>, FilterMusicInfo>) v1 -> {
          FilterMusicInfo filterMusicInfo = new FilterMusicInfo();
          if (FILTER_MUSIC_PLAY_TIMES <= v1._2.getPlayTimes()) {
            filterMusicInfo.setMusicID(v1._1);
            filterMusicInfo.setUserID(v1._2.getUserID());
          } else {
            filterMusicInfo.setUserID(null);
          }
          return filterMusicInfo;
        }).filter((Function<FilterMusicInfo, Boolean>) v1 -> {
          //TODO 待逻辑验证通过后，改成lamada表达式
          if (null == v1.getUserID()) {
            return false;
          } else {
            return true;
          }
        });

    //TODO 更新音乐过滤表 和 增加/更新 用户标签权值 这两块不要collect ，而是flatmap在各个patrtion里面循环进行调用接口
    /**
     * 更新音乐过滤表
     */
    List<FilterMusicInfo> filterMusicInfoList = filterMusicList.collect();
    JSONObject updateFilterMusic = new JSONObject();
    updateFilterMusic.put("filterMusicInfoList", JSON.toJSONString(filterMusicInfoList));
    //TODO http 封装成接口
    String updateFilterMusicResponse = HttpUtil
        .post(UPDATE_FILTER_MUSIC_URL, updateFilterMusic.toJSONString());
    System.out.println("update filter music response :" + updateFilterMusicResponse);

    /**
     * 增加/更新 用户标签权值
     */
    List<PersonaInfo> personaInfoList = personaInfoJavaRDD.collect();
    JSONObject updateUserTagsScore = new JSONObject();
    updateUserTagsScore.put("personaInfoList", JSON.toJSONString(personaInfoList));
    String updateUserTagsResponse = HttpUtil
        .post(UPDATE_USER_TAGS_URL, updateUserTagsScore.toJSONString());
    System.out.println("update user tags response :" + updateUserTagsResponse);
  }
}