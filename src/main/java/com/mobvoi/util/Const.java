// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

/**
 * 基本常量类 created by zhhgao@mobvoi.com on 2018/9/4
 */
public class Const {

  /**
   * 通用 配置文件
   */
  public static String CONFIG_COMMON = "config-Common.properties";
  /**
   * 离线推荐音乐列表 spark job 配置文件
   */
  public static String CONFIG_OFFLINE_RECOMMEND = "config-OfflineRecommend.properties";

  /**
   * 处理音乐meta信息 spark job 配置文件
   */
  public static String CONFIG_PROCESS_MUSICMETA = "config-ProcessMusicMeta.properties";

  /**
   * 计算用户画像 spark job 配置文件
   */
  public static String CONFIG_PROCESS_PERSONA = "config-ProcessPersona.properties";


  /**
   * 埋点数据中json中的酷我ID名称
   */
  public static String JSON_KW_ID = PropertiesUtil.getProcessPersonaConf("json.kw.id");

  /**
   * 选择hive表查询出来的account列
   */
  public static String HIVE_ACCOUNT_COL = PropertiesUtil
      .getProcessPersonaConf("hive.account.col");

  /**
   * 选择hive表查询出来的properties列
   */
  public static String HIVE_PROPERTIES_COL = PropertiesUtil
      .getProcessPersonaConf("hive.properties.col");

  /**
   * 用户权值 比较 系数（扩大N倍防止小数点位数过多，导致本应该有大小的比较，却认为是相等）
   */
  public static int COMPARE_COEFFICIENT = Integer
      .parseInt(PropertiesUtil.getProcessOfflineRecommendConf("compare.coefficient"));
}