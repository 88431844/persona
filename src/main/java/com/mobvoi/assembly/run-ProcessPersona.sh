#!/bin/sh
JAR_PATH=persona-1.0-SNAPSHOT.jar

spark2-submit \
--class com.mobvoi.ProcessPersona \
--master yarn \
--deploy-mode client \
--jars /spark-test/persona-1.0-SNAPSHOT/lib/*.jar $JAR_PATH \