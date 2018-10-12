// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import static com.mobvoi.util.CalculateScore.processUserTagsScore;
import static com.mobvoi.util.Const.HIVE_ACCOUNT_COL;
import static com.mobvoi.util.Const.HIVE_PROPERTIES_COL;

import com.mobvoi.processPersona.bean.PointInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import scala.collection.Iterator;
import scala.collection.mutable.Seq;

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

  public static Map<String, String> formatRowMapForAccount(Row row) {
    Map<String, String> rowMap = new HashMap<>();
    try {
      for (int i = 0; i < row.schema().fields().length; i++) {
        for (int j = 0; j < row.getStruct(i).size(); j++) {
          String key = row.getStruct(i).schema().fields()[j].name();
          String value = row.getStruct(i).getString(j);
          rowMap.put(key, value);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rowMap;
  }

  /**
   * 埋点原始数据转换 音乐动作
   */
  public static String convertMusicAction(Map<String, Object> propertiesMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return String.valueOf(propertiesMap.get("musicAction"));
  }

  /**
   * 埋点原始数据转换 音乐来源
   */
  public static String convertMusicComeFrom(Map<String, Object> propertiesMap) {
    //TODO 待具体埋点数据格式定下来，可能需要写个枚举类，更方便转换
    return String.valueOf(propertiesMap.get("comeFrom"));
  }


  /**
   * 埋点原始数 获取标签名称列表
   */
  public static List<String> getTagNames(Map<String, Object> propertiesMap) {
    //TODO 可能需要先查看埋点结构，规范或者定义，然后写个对应的枚举类
    List<String> tList = new ArrayList<>();
    Seq<String> ss = (Seq<String>) propertiesMap.get("tags");
    Iterator<String> iterator = ss.iterator();
    while (iterator.hasNext()) {
      String tagName = iterator.next();
      tList.add(tagName);
    }
    return tList;
  }

  /**
   * hive表原始数据 转换为 埋点信息 实体类
   */
  public static PointInfo convertRowToPointInfo(Row row) {
    PointInfo pointInfo = new PointInfo();
    Map<String, Object> rowMap = formatRowMap(row);
    Map<String, Object> propertiesMap = new HashMap<>();
    GenericRowWithSchema propertiesRow = (GenericRowWithSchema) rowMap.get(HIVE_PROPERTIES_COL);
    for (int i = 0; i < propertiesRow.length(); i++) {
      String key = propertiesRow.schema().fields()[i].name();
      Object value = propertiesRow.get(i);
      propertiesMap.put(key, value);
    }

    //TODO 这块需要抽取方法，灵活解析字段，而不是写死。
    GenericRowWithSchema accountRow = (GenericRowWithSchema) rowMap.get(HIVE_ACCOUNT_COL);
    String kwID = (String) accountRow.get(2);

    double playProportion = Double
        .parseDouble(String.valueOf(propertiesMap.get("playProportion")));//播放比例
    String musicAction = convertMusicAction(propertiesMap);//音乐动作：详见Const常量类。
    String comeFrom = convertMusicComeFrom(propertiesMap);//音乐来源：详见Const常量类。
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