// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.offlineRecommend.util;

import com.mobvoi.util.CommonCallService;
import com.mobvoi.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * created by zhhgao@mobvoi.com on 2018/10/18
 */
public class OfflineCallService extends CommonCallService {

  /**
   * 请求所有酷我用户ID接口url
   */
  private static String GET_ALL_USER_URL = PropertiesUtil
      .getProcessOfflineRecommendConf("get.all.user.url");

  /**
   * 获取所有用户信息接口
   *
   * @return uList
   */
  public static List<String> getAllUsers() {
    List<String> kwIDs = new ArrayList<>();
//    String responseStr = HttpUtil.post(GET_ALL_USER_URL, "");
//    if (StringUtils.isNotEmpty(responseStr)) {
//      JSONObject users = JSON
//          .parseObject(String.valueOf(JSON.parseObject(responseStr).get("users")));
//      if (StringUtils.isNotEmpty(users.toJSONString())) {
//        JSONArray usersArrayJson = JSONArray.parseArray(users.toJSONString());
//        for (Object o : usersArrayJson) {
//          String kwID = String.valueOf(o);
//          if (null != kwID) {
//            uList.add(kwID);
//          }
//        }
//      }
//    }
    for (int i = 0; i < 10; i++) {
      String kwID = "kwID" + i + 1;
      kwIDs.add(kwID);
    }

    return kwIDs;
  }
}
