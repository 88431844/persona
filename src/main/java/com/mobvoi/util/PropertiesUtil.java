// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * properties 读取工具类 created by zhhgao@mobvoi.com on 18-8-21
 */
public class PropertiesUtil {

  /**
   * 配置文件map 第一层为配置文件名称，第二层为对应配置文件的key，value
   */
  private static Map<String, Map<String, String>> allParam = new HashMap<>();
  /**
   * log
   */
  private static Logger log = Logger.getLogger(PropertiesUtil.class);

  /**
   * 根据配置文件和key取相应值
   */
  private static String getProperties(String key, String configFiles) {
    if (null != key && null != configFiles) {
      Map<String, String> propertiesMap = allParam.get(configFiles);
      if (null == propertiesMap || 0 == propertiesMap.size()) {
        propertiesMap = new HashMap<>();
        log.info("init " + configFiles + " to map ...............");
        Properties props = new Properties();
        try {
          props.load(new FileInputStream(configFiles));
        } catch (Exception e) {
          try {
            props.load(new FileInputStream(
                PropertiesUtil.class.getClassLoader().getResource(configFiles).getPath()));
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
        Set<Entry<Object, Object>> allKey = props.entrySet();
        for (Entry<Object, Object> entry : allKey) {
          propertiesMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        if (propertiesMap.size() > 0) {
          allParam.put(configFiles, propertiesMap);
        }
      }
      return propertiesMap.get(key);
    }
    //如果key或者configFiles有一个为null则返回null
    else {
      return null;
    }
  }

  /**
   * 读取 用户画像 配置文件
   */
  public static String getProcessPersonaConf(String key) {
    return getProperties(key, Const.CONFIG_PROCESS_PERSONA);
  }

  /**
   * 读取 处理音乐meta 配置文件
   */
  public static String getProcessMusicMetaConf(String key) {
    return getProperties(key, Const.CONFIG_PROCESS_MUSICMETA);
  }

  /**
   * 读取 离线推荐音乐 配置文件
   */
  public static String getProcessOfflineRecommendConf(String key) {
    return getProperties(key, Const.CONFIG_OFFLINE_RECOMMEND);
  }

}