// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import static com.mobvoi.util.CalculateScore.processUserTagsScore;
import static com.mobvoi.util.Const.HIVE_ACCOUNT_COL;
import static com.mobvoi.util.Const.HIVE_PROPERTIES_COL;
import static com.mobvoi.util.Const.JSON_KW_ID;

import com.mobvoi.processPersona.bean.PointInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.Row;

/**
 * 数据转换 工具类 created by zhhgao@mobvoi.com on 2018/9/18
 */
public class ConvertData {


  /**
   * 读取schema，这里在通过spark-sql读取到row数据之后，将schema解析出来，并且映射为HashMap
   */
  public static Map<String, Object> formatRowMap(Row row) {
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
  public static Integer convertMusicAction(Map<String, Object> rowMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return 0;
  }

  /**
   * 埋点原始数据转换 音乐来源
   */
  public static Integer convertMusicComeFrom(Map<String, Object> rowMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return 0;
  }


  /**
   * 埋点原始数 获取标签名称列表
   */
  public static List<String> getTagNames(Map<String, Object> rowMap) {
    //TODO 可能需要先查看埋点结构，规范或者定义，然后写个对应的枚举类
    return new ArrayList<>();
  }

  /**
   * hive表原始数据 转换为 埋点信息 实体类
   */
  public static PointInfo convertRowToPointInfo(Row row) {
    PointInfo pointInfo = new PointInfo();
    Map<String, Object> rowMap = formatRowMap(row);
    Map<String, Object> accountMap = (Map<String, Object>) rowMap.get(HIVE_ACCOUNT_COL);
    Map<String, Object> propertiesMap = (Map<String, Object>) rowMap.get(HIVE_PROPERTIES_COL);
    String kwID = String.valueOf(accountMap.get(JSON_KW_ID));
    double playProportion = Double
        .parseDouble(String.valueOf(propertiesMap.get("playProportion")));//播放比例
    Integer musicAction = convertMusicAction(propertiesMap);//音乐动作：详见Const常量类。
    Integer comeFrom = convertMusicComeFrom(propertiesMap);//音乐来源：详见Const常量类。
    //计算本次标签权值
    double thisPeriodScore = processUserTagsScore(playProportion, musicAction, comeFrom);
    List<String> tagNameList = getTagNames(propertiesMap);
    String musicID = String.valueOf(propertiesMap.get("musicID"));

    pointInfo.setKwID(kwID);
    pointInfo.setMusicID(musicID);
    pointInfo.setThisPeriodScore(thisPeriodScore);
    pointInfo.setPlayProportion(playProportion);
    pointInfo.setTagNameList(tagNameList);
    return pointInfo;
  }

}