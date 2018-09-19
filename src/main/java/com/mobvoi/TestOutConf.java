// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi;

import com.mobvoi.util.Const;
import com.mobvoi.util.PropertiesUtil;
import org.apache.spark.sql.SparkSession;

/**
 * 测试spark读取外部配置文件 created by zhhgao@mobvoi.com on 2018/9/17
 */
public class TestOutConf {

  public static void main(String[] args) {
    SparkSession ss = SparkSession.builder()
        .appName("Test-out-conf")
        .getOrCreate();

    System.out
        .println("--------- one ----------:" + PropertiesUtil
            .getProperties("hive.database", Const.CONFIG_PROCESS_PERSONA));

    System.out.println("--------- two ----------:" + PropertiesUtil
        .getProperties("hive.sql", Const.CONFIG_PROCESS_PERSONA));
  }
}