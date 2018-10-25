// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.bean.FilterMusicInfo;
import com.mobvoi.bean.TagInfo;
import com.mobvoi.bean.UserInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * created by zhhgao@mobvoi.com on 2018/10/18
 */
public class CommonCallService {

  /**
   * 请求用户标签信息的接口url
   */
  private static String GET_USER_TAGS_URL = PropertiesUtil
      .getCommonConf("get.user.tags.url");

  /**
   * 更新音乐过滤表的接口url
   */
  private static String UPDATE_FILTER_MUSIC_URL = PropertiesUtil
      .getCommonConf("update.filter.music.url");

  /**
   * 请求用户拥有标签接口
   */
  public static UserInfo getUsersTagsScore(String kwID) {
    UserInfo userInfo = new UserInfo();
    List<TagInfo> tagInfos = new ArrayList<>();
    if (StringUtils.isNotEmpty(kwID)) {
      userInfo.setKwID(kwID);
    } else {
      return userInfo;
    }
    JSONObject request = new JSONObject();
    request.put("kwID", kwID);
    String response = HttpUtil.post(GET_USER_TAGS_URL, request.toJSONString());
    if (StringUtils.isNotEmpty(response)) {
      JSONObject jsonObject = JSON.parseObject(response);
      JSONArray jsonArray = jsonObject.getJSONArray("tags");
      tagInfos = JSONObject.parseArray(jsonArray.toJSONString(), TagInfo.class);
    }
    userInfo.setTags(tagInfos);
    return userInfo;
  }

  /**
   * 更新某个用户的过滤音乐列表接口
   */
  public static void updateFilterMusicList(FilterMusicInfo filterMusicInfo) {
    JSONObject request = new JSONObject();
    request.put("filterMusicInfo", JSON.toJSONString(filterMusicInfo));
//    HttpUtil.post(UPDATE_FILTER_MUSIC_URL, request.toJSONString());
  }
}