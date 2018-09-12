// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.bean.PersonaInfo;
import com.mobvoi.bean.TagInfo;
import com.mobvoi.util.HttpUtil;
import com.mobvoi.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


/**
 * 用户标签计算 spark job
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class ProcessPersona {

  /**
   * spark job 名称
   */
  private static String APPLICATION_NAME = PropertiesUtil.getProperties("spark.application.name");
  /**
   * hive warehouse 地址
   */
  private static String HIVE_WARE_HOUSE = PropertiesUtil.getProperties("spark.sql.warehouse.dir");
  /**
   * hive database
   */
  private static String HIVE_DATABASE = PropertiesUtil.getProperties("hive.database");
  /**
   * hive sql
   */
  private static String HIVE_SQL = PropertiesUtil.getProperties("hive.sql");

  /**
   * 选择hive表查询出来的列
   */
  private static String HIVE_SELECTED_COL = PropertiesUtil.getProperties("hive.selected.col");
  /**
   * 请求所有音乐tags信息的接口url
   */
  private static String ALL_MUSIC_TAGS_URL = PropertiesUtil.getProperties("all.music.tags.url");
  /**
   * 请求用户标签信息的接口url
   */
  private static String USER_TAGS_URL = PropertiesUtil.getProperties("user.tags.url");
  /**
   * 用户标签时 间衰减系数
   */
  private static Double USER_TAG_TIME_REDUCE_COEFFICIENT = Double
      .parseDouble(PropertiesUtil.getProperties("user.tag.time.reduce.coefficient"));

  /**
   * 1.更新音乐过滤表
   * 2.计算本次用户标签权值
   * 3.根据用户是否已经拥有标签，进行增加/更新用户标签操作
   */
  public static void main(String[] args) {

    SparkSession ss = SparkSession.builder()
        .appName(APPLICATION_NAME)
        .config("spark.sql.warehouse.dir", HIVE_WARE_HOUSE)
        .enableHiveSupport().getOrCreate();

    //调用接口读取所有标签ID和标签名称的HashMap，存为广播变量
    Map<String, String> tagsMap = new HashMap<>();
    JSONObject reqAllMusicTags = new JSONObject();
    reqAllMusicTags.put("domain", "MUSIC");
    String tagStr = HttpUtil
        .post(ALL_MUSIC_TAGS_URL, reqAllMusicTags.toJSONString(), HttpUtil.CONTENT_TYPE_JSON);
    if (StringUtils.isNotEmpty(tagStr)) {
      JSONArray musicTagsArrayJson = JSONArray.parseArray(tagStr);
      for (Object ob : musicTagsArrayJson) {
        JSONObject tagJson = (JSONObject) ob;
        tagsMap.put(tagJson.getString("tagName"), tagJson.getString("tagID"));
      }
    }

    JavaSparkContext jsc = JavaSparkContext.fromSparkContext(ss.sparkContext());
    Broadcast<Map<String, String>> tagsCast = jsc.broadcast(tagsMap);

    //切换使用的hive数据库
    ss.sql("use " + HIVE_DATABASE);

    Dataset<Row> rowDataSet = ss.sql(HIVE_SQL);

    JavaRDD<PersonaInfo> personaInfoJavaRDD = rowDataSet.select(HIVE_SELECTED_COL).toJavaRDD().map(
        (Function<Row, PersonaInfo>) row -> {
          PersonaInfo personaInfo = new PersonaInfo();

          Map<String, Object> rowMap = formatRowMap(row);

          String userID = String.valueOf(rowMap.get("userID"));
          String musicID = String.valueOf(rowMap.get("musicID"));
          Integer musicDuration = Integer.parseInt(String.valueOf(rowMap.get("musicDuration")));
          Integer musicPlayDuration = Integer.parseInt(String.valueOf(rowMap.get("playDuration")));
          Integer musicAction = convertMusicAction(rowMap);//音乐动作：详见Const常量类。
          Integer comeFrom = convertMusicComeFrom(rowMap);//音乐来源：详见Const常量类。

          double periodScore = 0D;
          //TODO 计算用户标签权值（调接口获取上次权值*时间衰减系数 + 本次权值）

          //TODO 计算本次标签权值

          //TODO 标签更新/添加

          //获取该用户所有标签
          Map<String, TagInfo> userLastTagInfoMap = new HashMap<>();//key为tagID，value为tagInfo
          JSONObject requestUserTagsScore = new JSONObject();
          requestUserTagsScore.put("userID", userID);
          String userTagsStr = HttpUtil
              .post(USER_TAGS_URL, requestUserTagsScore.toJSONString(), HttpUtil.CONTENT_TYPE_JSON);

          if (StringUtils.isNotEmpty(userTagsStr)) {
            JSONArray userTagsArrayJson = JSONArray.parseArray(userTagsStr);
            for (Object ob : userTagsArrayJson) {
              JSONObject tagJson = (JSONObject) ob;
              TagInfo tagInfo = new TagInfo();
              tagInfo.setTagID(tagJson.getString("tagID"));
              tagInfo.setPeriodScore(tagJson.getDouble("periodScore"));
              userLastTagInfoMap.put(tagInfo.getTagID(), tagInfo);
            }
          }

          //用从广播变量获取的 标签ID和标签名称的HashMap，把所有标签名称转换为标签ID
          //TODO 这块需要获取到实际埋点信息才可以具体写如何获取tagNameList
          List<String> tagNameList = (List<String>) rowMap.get("tagNameList");
          List<TagInfo> tagInfoList = new ArrayList<>();
          Map<String, String> tagCastMap = tagsCast.getValue();
          if (null != tagCastMap && tagCastMap.size() > 0) {
            for (String tagName : tagNameList) {
              TagInfo tagInfo = new TagInfo();
              //通过tagName换tagID
              tagInfo.setTagID(tagCastMap.get(tagName));
              tagInfo.setPeriodScore(periodScore);
              tagInfoList.add(tagInfo);
            }
          }

          personaInfo.setUserID(String.valueOf(rowMap.get("UserID")));
          personaInfo.setTagInfoList(tagInfoList);
          return personaInfo;
        });

  }

  /**
   * 读取schema，这里在通过spark-sql读取到row数据之后，将schema解析出来，并且映射为HashMap
   */
  private static Map<String, Object> formatRowMap(Row row) {
    Map<String, Object> rowMap = new HashMap<>();
    try {
      for (int i = 0; i < row.schema().fields().length; i++) {
        String colName = row.schema().fields()[i].name();
        Object colValue = row.get(i);
        rowMap.put(colName, colValue);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rowMap;
  }

  /**
   * 埋点原始数据转换 音乐动作
   */
  private static Integer convertMusicAction(Map<String, Object> rowMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return 0;
  }

  /**
   * 埋点原始数据转换 音乐来源
   */
  private static Integer convertMusicComeFrom(Map<String, Object> rowMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return 0;
  }
}