#!/bin/sh
JAR_PATH=persona-1.0-SNAPSHOT.jar

spark2-submit \
--class com.mobvoi.processPersona.ProcessPersona \
--master yarn \
--deploy-mode client \
--files config.properties \
$JAR_PATH \