// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.processPersona.bean.FilterMusicInfo;
import com.mobvoi.processPersona.bean.PersonaInfo;
import com.mobvoi.processPersona.bean.TagInfo;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * created by zhhgao@mobvoi.com on 2018/9/18
 */
public class CallService {

  /**
   * 请求用户标签信息的接口url
   */
  private static String GET_USER_TAGS_URL = PropertiesUtil
      .getProcessPersonaConf("get.user.tags.url");

  /**
   * 请求所有音乐tags信息的接口url
   */
  private static String ALL_MUSIC_TAGS_URL = PropertiesUtil
      .getProcessPersonaConf("all.music.tags.url");

  /**
   * 同步用户画像标签权值的接口url
   */
  private static String SYNC_PERSONA_URL = PropertiesUtil
      .getProcessPersonaConf("sync.persona.url");
  /**
   * 更新音乐过滤表的接口url
   */
  private static String UPDATE_FILTER_MUSIC_URL = PropertiesUtil
      .getProcessPersonaConf("update.filter.music.url");
  /**
   * log
   */
  private static Logger log = Logger.getLogger(CallService.class);

  /**
   * 请求所有音乐标签
   *
   * @return tagsMap
   */
  public static Map<String, String> requestAllMusicTags() {
    Map<String, String> tagsMap = new HashMap<>();
    JSONObject reqAllMusicTags = new JSONObject();
    reqAllMusicTags.put("domain", "MUSIC");
    String responseStr = HttpUtil
        .post(ALL_MUSIC_TAGS_URL, reqAllMusicTags.toJSONString());
    if (StringUtils.isNotEmpty(responseStr)) {
      JSONObject tags = JSON
          .parseObject(String.valueOf(JSON.parseObject(responseStr).get("tags")));
      if (StringUtils.isNotEmpty(tags.toJSONString())) {
        JSONArray musicTagsArrayJson = JSONArray.parseArray(tags.toJSONString());
        for (Object ob : musicTagsArrayJson) {
          JSONObject tagJson = (JSONObject) ob;
          tagsMap.put(tagJson.getString("tagName"), tagJson.getString("tagID"));
        }
      }
    }
    return tagsMap;
  }

  /**
   * 请求该用户所有标签
   */
  public static Map<String, TagInfo> requestUserTagsScore(String kwID) {
    Map<String, TagInfo> userLastTagInfoMap = new HashMap<>();
    JSONObject requestUserTagsScore = new JSONObject();
    requestUserTagsScore.put("kwID", kwID);
    String responseStr = HttpUtil
        .post(GET_USER_TAGS_URL, requestUserTagsScore.toJSONString());

    if (StringUtils.isNotEmpty(responseStr)) {
      JSONObject tags = JSON
          .parseObject(String.valueOf(JSON.parseObject(responseStr).get("tags")));
      if (StringUtils.isNotEmpty(tags.toJSONString())) {
        JSONArray userTagsArrayJson = JSONArray.parseArray(tags.toJSONString());
        for (Object ob : userTagsArrayJson) {
          JSONObject tagJson = (JSONObject) ob;
          TagInfo tagInfo = new TagInfo();
          tagInfo.setTagID(tagJson.getString("tagID"));
          tagInfo.setPeriodScore(tagJson.getDouble("periodScore"));
          userLastTagInfoMap.put(tagInfo.getTagID(), tagInfo);
        }
      }
    }
    return userLastTagInfoMap;
  }

  /**
   * 同步用户画像标签权值
   */
  public static void syncPersona(PersonaInfo personaInfo) {
    JSONObject syncUserTagsScore = new JSONObject();
    syncUserTagsScore.put("personaInfo", JSON.toJSONString(personaInfo));
    HttpUtil.post(SYNC_PERSONA_URL, syncUserTagsScore.toJSONString());
  }

  /**
   * 更新某个用户的过滤音乐列表接口
   */
  public static void updateFilterMusicList(FilterMusicInfo filterMusicInfo) {
    JSONObject updateFilterMusicList = new JSONObject();
    updateFilterMusicList.put("filterMusicInfo", JSON.toJSONString(filterMusicInfo));
    HttpUtil.post(UPDATE_FILTER_MUSIC_URL, updateFilterMusicList.toJSONString());
  }
}