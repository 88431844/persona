// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

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
}