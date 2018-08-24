#!/bin/sh
JAR_PATH=testspark-1.0-SNAPSHOT.jar

spark2-submit --class com.spark.TestSpark  \
--master yarn \
--executor-memory 1G \
--driver-memory 2G \
--conf spark.default.parallelism=100 \
--total-executor-cores 30 \
--executor-cores 2 \
--deploy-mode client --jars /spark-test-files/lib/*.jar $JAR_PATH

#jars 后面跟的路径为hdfs的相对路径 再后面跟要执行的spark job jar