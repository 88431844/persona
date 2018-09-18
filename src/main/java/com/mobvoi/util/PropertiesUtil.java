// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * properties 读取工具类
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class PropertiesUtil implements Serializable {
  private static final Properties properties = new Properties();
  private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
  private static InputStream inputStream = null;
  private static final String FILE_NAME = "config.properties";
  private static Map<String,String> allParam = new HashMap<String, String>();

  public static Properties loadProperty (){
    inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(FILE_NAME);
    try {
      properties.load(inputStream);

    } catch (IOException e) {
      logger.error("init"+FILE_NAME+"error",e);
      logger.error(e.getMessage());
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        logger.error("init error"+FILE_NAME+"close stream fail",e);
        logger.error(e.getMessage());
      }
    }
    return properties;
  }

  //根据文件名称-key，返回相应key的值
  public static String getPropertiesByKey(String fileName,String key){
    try {
      if(allParam.containsKey(key)){
        return allParam.get(key);
      }else{
        logger.info("开始初始化配置文件【"+fileName+"】");
        allParam.clear();
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        Properties p = new Properties();
        p.load(in);
        Set<Entry<Object, Object>> allKey = p.entrySet();
        for (Entry<Object, Object> entry : allKey) {
          allParam.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        in.close();
        logger.info("成功初始化配置文件【"+fileName+"】");
        return allParam.get(key);
      }
    } catch (Exception e) {
      logger.error("初始化配置文件【"+fileName+"】出错");
      e.printStackTrace();
    }
    return null;
  }


  public static Map<String,String> getProperties(){
    try {
      if(allParam.size()==0){

        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Properties p = new Properties();
        p.load(in);
        Set<Entry<Object, Object>> allKey = p.entrySet();
        for (Entry<Object, Object> entry : allKey) {
          allParam.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        in.close();
        return allParam;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return allParam;
  }

  @Deprecated
  public static String getProperties(String key){
    //String path = PropertiesUtil.class.getClassLoader().getResource(FILE_NAME).getPath();
    String value = getPropertiesByKey(FILE_NAME, key);
    //logger.info("配置在此"+FILE_NAME+"---"+value+" 路径："+path);
    return value;
  }

  /**
   * 读取jar包外部配置文件
   */
  public static String getPropertiesOut(String key) {
    //判断map的size是否为零，如果为零则说明第一次启动，否则直接读取map
    if (allParam.size() == 0) {
      logger.info("init properties to map ...............");
      Properties props = new Properties();
      try {
        props.load(new FileInputStream(FILE_NAME));
      } catch (Exception e) {
        e.printStackTrace();
      }
      Set<Entry<Object, Object>> allKey = props.entrySet();
      for (Entry<Object, Object> entry : allKey) {
        allParam.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
      }
    }
    return allParam.get(key);
  }
}