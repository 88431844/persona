// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi;

import org.apache.spark.SparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;

/**
 * 测试spark 读取hive
 * created by zhhgao@mobvoi.com on 2018/9/6
 */
public class TestHive {

  public static void main(String[] args) {
    SparkSession ss = SparkSession.builder()
        .appName("Test-spark-hive")
        .config("spark.sql.warehouse.dir", "/user/hive/warehouse/")
        .enableHiveSupport().getOrCreate();

    ss.sql("use mobvoi");
    ss.sql("show databases").show();
    ss.sql("select * from analytics limit 2").show();

    SparkContext sc = ss.sparkContext();
    RDD<String> stringRDD = sc.textFile("/spark-test-files/1.json", 2);
    System.out.println("spark-test-files/1.json。。。。。。：" + stringRDD.count());


  }
}