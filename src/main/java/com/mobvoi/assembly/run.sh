#!/bin/sh
JAR_PATH=persona-1.0-SNAPSHOT.jar

spark2-submit --class com.mobvoi.TestHive  \
--master yarn \
--executor-memory 1G \
--driver-memory 2G \
--conf spark.default.parallelism=100 \
--total-executor-cores 30 \
--executor-cores 2 \
--deploy-mode client --jars /spark-test/persona-1.0-SNAPSHOT/lib/*.jar $JAR_PATH

#jars 后面跟的路径为hdfs的相对路径 再后面跟要执行的spark job jar
#--total-executor-cores 30 \ 这个配置貌似只在Spark standalone and Mesos生效，提交时候测试一下去掉这个参数
#--conf spark.default.parallelism=100 \ 这个配置提交时候测试一下去掉：https://spark.apache.org/docs/latest/configuration.html