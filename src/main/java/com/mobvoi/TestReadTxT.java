// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;

/**
 * created by zhhgao@mobvoi.com on 2018/9/10
 */
public class TestReadTxT {

  public static void main(String[] args) {
    SparkSession ss = SparkSession.builder()
        .appName("TestReadTxT").getOrCreate();

    SparkContext sc = ss.sparkContext();

    RDD<String> kw = sc.textFile("/spark-test-files/KW.txt", 2);

    JavaRDD<String> kwrdd = kw.toJavaRDD().map(new Function<String, String>() {
      @Override
      public String call(String s) throws Exception {
        if (s.contains("#")) {
          return "1";
        } else {
          return "0";
        }
      }
    });

    System.out.println("-----------------------total : " + kwrdd.count());

    Long haveTag = kwrdd.filter(new Function<String, Boolean>() {
      @Override
      public Boolean call(String v1) throws Exception {
        if ("0".equals(v1)) {
          return false;
        } else {
          return true;
        }
      }
    }).count();

    System.out.println("-----------------------have tag : " + haveTag);
  }


}