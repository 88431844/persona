// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.bean.PointInfo;
import com.mobvoi.util.PropertiesUtil;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;

/**
 * 用户标签计算 spark job
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class ProcessPersona {

  /**
   * 部署模式
   */
  private static String DEPLOY_MODE = PropertiesUtil.getProperties("spark.deploy.mode");
  /**
   * spark job 名称
   */
  private static String APPLICATION_NAME = PropertiesUtil.getProperties("spark.application.name");
  /**
   * 分析数据文件位置
   */
  private static String POINT_FILE_PATH = PropertiesUtil.getProperties("point.file.path");
  /**
   * #读取HDFS的partition数量
   */
  private static int HDFS_MINPARTITIONS = Integer.parseInt(PropertiesUtil.getProperties("hdfs.minpartitions"));

  /**
   * 1.更新音乐过滤表
   * 2.计算本次用户标签权值
   * 3.根据用户是否已经拥有标签，进行增加/更新用户标签操作
   */
  public static void main(String[] args) {

    SparkSession spark = SparkSession.builder().master(DEPLOY_MODE).appName(APPLICATION_NAME).getOrCreate();

    SparkContext sc = spark.sparkContext();

    //获取ETL后的埋点数据
    RDD<String> pointStrRDD = sc.textFile(POINT_FILE_PATH,HDFS_MINPARTITIONS);

    //将埋点数据
    JavaRDD<PointInfo> pointJavaRDD = pointStrRDD.toJavaRDD().map(
        (Function<String, PointInfo>) str -> JSONObject.parseObject(str,PointInfo.class));




  }
}