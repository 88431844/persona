// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.processPersona.bean.TagInfo;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * created by zhhgao@mobvoi.com on 2018/9/18
 */
public class CallService {

  /**
   * 请求用户标签信息的接口url
   */
  private static String GET_USER_TAGS_URL = PropertiesUtil
      .getProperties("get.user.tags.url", Const.CONFIG_PROCESS_PERSONA);

  /**
   * 请求所有音乐tags信息的接口url
   */
  private static String ALL_MUSIC_TAGS_URL = PropertiesUtil
      .getProperties("all.music.tags.url", Const.CONFIG_PROCESS_PERSONA);

  /**
   * 请求所有音乐标签
   *
   * @return tagsMap
   */
  public static Map<String, String> requestAllMusicTags() {
    Map<String, String> tagsMap = new HashMap<>();
    JSONObject reqAllMusicTags = new JSONObject();
    reqAllMusicTags.put("domain", "MUSIC");
    String tagStr = HttpUtil
        .post(ALL_MUSIC_TAGS_URL, reqAllMusicTags.toJSONString());
    if (StringUtils.isNotEmpty(tagStr)) {
      JSONArray musicTagsArrayJson = JSONArray.parseArray(tagStr);
      for (Object ob : musicTagsArrayJson) {
        JSONObject tagJson = (JSONObject) ob;
        tagsMap.put(tagJson.getString("tagName"), tagJson.getString("tagID"));
      }
    }
    return tagsMap;
  }

  /**
   * 请求该用户所有标签
   */
  public static Map<String, TagInfo> requestUserTagsScore(String userID) {
    Map<String, TagInfo> userLastTagInfoMap = new HashMap<>();
    JSONObject requestUserTagsScore = new JSONObject();
    requestUserTagsScore.put("userID", userID);
    String userTagsStr = HttpUtil
        .post(GET_USER_TAGS_URL, requestUserTagsScore.toJSONString());

    if (StringUtils.isNotEmpty(userTagsStr)) {
      JSONArray userTagsArrayJson = JSONArray.parseArray(userTagsStr);
      for (Object ob : userTagsArrayJson) {
        JSONObject tagJson = (JSONObject) ob;
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTagID(tagJson.getString("tagID"));
        tagInfo.setPeriodScore(tagJson.getDouble("periodScore"));
        userLastTagInfoMap.put(tagInfo.getTagID(), tagInfo);
      }
    }
    return userLastTagInfoMap;
  }

}