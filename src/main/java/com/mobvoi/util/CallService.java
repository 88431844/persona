// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobvoi.processPersona.bean.FilterMusicInfo;
import com.mobvoi.processPersona.bean.PersonaInfo;
import com.mobvoi.processPersona.bean.TagInfo;
import java.util.HashMap;
import java.util.Map;
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

//    List<TagInfo> tagInfoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
//      TagInfo tagInfo = new TagInfo();
//      tagInfo.setPeriodScore(i);
//      tagInfo.setTagID("tagID"+i);
//      tagInfo.setTagName("tagName"+i);
//      tagInfoList.add(tagInfo);
      tagsMap.put("tagName" + i, "tagID" + i);
    }

//    JSONObject reqAllMusicTags = new JSONObject();
//    reqAllMusicTags.put("domain", "MUSIC");
////    String responseStr = "{\"errCode\":0,\"tags\":[{\"tagID\":\"tagID0\",\"tagName\":\"tagName0\",\"periodScore\":0.0},{\"tagID\":\"tagID1\",\"tagName\":\"tagName1\",\"periodScore\":1.0},{\"tagID\":\"tagID2\",\"tagName\":\"tagName2\",\"periodScore\":2.0},{\"tagID\":\"tagID3\",\"tagName\":\"tagName3\",\"periodScore\":3.0},{\"tagID\":\"tagID4\",\"tagName\":\"tagName4\",\"periodScore\":4.0},{\"tagID\":\"tagID5\",\"tagName\":\"tagName5\",\"periodScore\":5.0},{\"tagID\":\"tagID6\",\"tagName\":\"tagName6\",\"periodScore\":6.0},{\"tagID\":\"tagID7\",\"tagName\":\"tagName7\",\"periodScore\":7.0},{\"tagID\":\"tagID8\",\"tagName\":\"tagName8\",\"periodScore\":8.0},{\"tagID\":\"tagID9\",\"tagName\":\"tagName9\",\"periodScore\":9.0}]}";
//    String responseStr = HttpUtil
//        .post(ALL_MUSIC_TAGS_URL, reqAllMusicTags.toJSONString());
//    log.info("CallService requestAllMusicTags responseStr : "+ responseStr);
//    if (StringUtils.isNotEmpty(responseStr)) {
//
//      JSONObject jsonObject = JSON.parseObject(responseStr);
////
////      String tagsStr = String.valueOf(jsonObject.get("tags"));
////
////      JSONObject tags = JSON.parseObject(tagsStr);
//      if (StringUtils.isNotEmpty(jsonObject.toJSONString())) {
////        JSONArray musicTagsArrayJson = JSONArray.parseArray(tags.toJSONString());
//        JSONArray musicTagsArrayJson = JSONArray.parseArray(jsonObject.get("tags").toString());
//        for (Object ob : musicTagsArrayJson) {
//          JSONObject tagJson = (JSONObject) ob;
//          tagsMap.put(tagJson.getString("tagName"), tagJson.getString("tagID"));
//        }
//      }
//    }
    return tagsMap;
  }

  /**
   * 请求该用户所有标签
   */
  public static Map<String, TagInfo> requestUserTagsScore(String kwID) {
    Map<String, TagInfo> userLastTagInfoMap = new HashMap<>();
    for (int i = 0; i < 10; i++) {

      TagInfo tagInfo = new TagInfo();
      tagInfo.setPeriodScore(3 * i);
      tagInfo.setTagID("tagID" + 3 * i);
      tagInfo.setTagName("tagName" + 3 * i);
      userLastTagInfoMap.put(tagInfo.getTagID(), tagInfo);
    }
//    JSONObject requestUserTagsScore = new JSONObject();
//    requestUserTagsScore.put("kwID", kwID);
//    String responseStr = HttpUtil
//        .post(GET_USER_TAGS_URL, requestUserTagsScore.toJSONString());
//    log.info("CallService requestUserTagsScore responseStr : "+ responseStr);
//    if (StringUtils.isNotEmpty(responseStr)) {
//      JSONObject tags = JSON
//          .parseObject(String.valueOf(JSON.parseObject(responseStr).get("tags")));
//      if (StringUtils.isNotEmpty(tags.toJSONString())) {
//        JSONArray userTagsArrayJson = JSONArray.parseArray(tags.toJSONString());
//        for (Object ob : userTagsArrayJson) {
//          JSONObject tagJson = (JSONObject) ob;
//          TagInfo tagInfo = new TagInfo();
//          tagInfo.setTagID(tagJson.getString("tagID"));
//          tagInfo.setPeriodScore(tagJson.getDouble("periodScore"));
//          userLastTagInfoMap.put(tagInfo.getTagID(), tagInfo);
//        }
//      }
//    }
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

  public static void main(String[] args) {
    CallService.requestAllMusicTags();
  }
}