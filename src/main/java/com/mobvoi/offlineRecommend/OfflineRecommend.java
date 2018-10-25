// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.offlineRecommend;

import com.alibaba.fastjson.JSON;
import com.mobvoi.bean.TagInfo;
import com.mobvoi.bean.UserInfo;
import com.mobvoi.offlineRecommend.bean.GetMusicList;
import com.mobvoi.offlineRecommend.bean.OfflineInfo;
import com.mobvoi.offlineRecommend.util.OfflineCallService;
import com.mobvoi.util.CommonCallService;
import com.mobvoi.util.Const;
import com.mobvoi.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

/**
 * 离线推荐音乐列表生成 spark job created by zhhgao@mobvoi.com on 2018/9/4
 */
public class OfflineRecommend {

  /**
   * spark job 名称
   */
  private static String APPLICATION_NAME = PropertiesUtil
      .getProcessOfflineRecommendConf("spark.application.name");
  /**
   * 离线推荐列表长度
   */
  private static Integer OFFLINE_MUSIC_SUM = Integer
      .parseInt(PropertiesUtil.getProcessOfflineRecommendConf("offline.music.sum"));
  /**
   * log
   */
  private static Logger log = Logger.getLogger(OfflineRecommend.class);

  public static void main(String[] args) {
    SparkSession ss = SparkSession.builder()
        .appName(APPLICATION_NAME)
        .master("local")//TODO 部署到集群时候需要注释掉！！！！
        .getOrCreate();

    JavaSparkContext js = JavaSparkContext.fromSparkContext(ss.sparkContext());

    List<String> kwIDs = OfflineCallService.getAllUsers();

    JavaRDD<String> kwIDsRDD = js.parallelize(kwIDs);

    JavaRDD<OfflineInfo> offlineInfoJavaRDD = kwIDsRDD
        //过滤掉kwID为null的数据。
        .filter(new Function<String, Boolean>() {
          @Override
          public Boolean call(String kwID) throws Exception {
            return null != kwID;
          }
        })
        //遍历所有kwID,请求这些用户对应的标签信息。
        .map(new Function<String, UserInfo>() {
          @Override
          public UserInfo call(String kwID) throws Exception {
            return CommonCallService.getUsersTagsScore(kwID);
          }
        })
        //将用户和标签信息组合成，以kwID为key，value为TagInfo的Tuple。
        .mapToPair(new PairFunction<UserInfo, String, List<TagInfo>>() {
          @Override
          public Tuple2<String, List<TagInfo>> call(UserInfo userInfo) throws Exception {
            return new Tuple2<String, List<TagInfo>>(userInfo.getKwID(), userInfo.getTags());
          }
        })
        //遍历上面那个Tuple,计算离线音乐推荐列表。
        .map(new Function<Tuple2<String, List<TagInfo>>, OfflineInfo>() {
          @Override
          public OfflineInfo call(Tuple2<String, List<TagInfo>> v1) throws Exception {
            OfflineInfo offlineInfo = new OfflineInfo();
            List<String> musicIDs = new ArrayList<>();
            List<TagInfo> tagInfos = v1._2;
            List<GetMusicList> musicLists = new ArrayList<>();//获取音乐列表request
            double totalPeriodScore = 0D;//该用户所有标签权值总和
            //对该用户的所有标签进行倒叙排序，分值大的在上面。
            Collections.sort(tagInfos, new Comparator<TagInfo>() {
              @Override
              public int compare(TagInfo t1, TagInfo t2) {
                int compareCoefficient = Const.COMPARE_COEFFICIENT;//比较系数
                double compareDouble = t1.getPeriodScore() * compareCoefficient
                    - t2.getPeriodScore() * compareCoefficient;
                if (compareDouble > 0) {
                  return -1;
                } else {
                  return 1;
                }
              }
            });

            //TODO 根据排序后的标签权值，根据逻辑，计算离线音乐推荐列表
            //将按照权值倒叙排列好的标签列表，根据离线推荐音乐列表的长度，以及某个标签占总标签的权值的比例，进行接口请求，标签权值大的请求的music数量也多。
            //每个标签请求的音乐列表之间肯定会有叠加重复的情况，根据叠加重复的次数，将音乐列表按照叠加重复次数倒叙排序，去TopN，N为离线推荐音乐列表长度。
            //接着将此离线推荐音乐列表存入该用户的过滤音乐列表中，为了下次过滤已经推荐过的音乐。
            //最后将此离线推荐音乐列表通过接口调用更新到该用户的离线推荐音乐列表中。

            //计算该用户所有标签权值总和
            for (TagInfo t : tagInfos) {
              totalPeriodScore += t.getPeriodScore();
            }
            //拼装 获取音乐列表request
            for (TagInfo tagInfo : tagInfos) {
              long count = Math.round(tagInfo.getPeriodScore() / totalPeriodScore);
              GetMusicList getMusicList = new GetMusicList();
              getMusicList.setCount(count);
              getMusicList.setTagID(tagInfo.getTagID());
              musicLists.add(getMusicList);
            }

//            OfflineCallService.getMusicListByTagID(musicLists);

            offlineInfo.setKwID(v1._1);
            offlineInfo.setMusicIDs(musicIDs);
            log.info("+++++++++++ offlineInfo :" + JSON.toJSONString(offlineInfo));
            CommonCallService.updateFilterMusicList(offlineInfo);
            return offlineInfo;
          }
        });

    //触发RDD计算的动作。
    offlineInfoJavaRDD.count();
  }
}