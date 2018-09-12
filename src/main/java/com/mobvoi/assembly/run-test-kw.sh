#!/bin/sh
JAR_PATH=persona-1.0-SNAPSHOT.jar

spark2-submit \
--class com.mobvoi.TestReadTxT \
--master yarn \
--deploy-mode client \
$JAR_PATH \