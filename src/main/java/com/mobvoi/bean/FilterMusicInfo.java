// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.util.List;

/**
 * 过滤用户音乐 实体类 created by zhhgao@mobvoi.com on 2018/9/13
 */
public class FilterMusicInfo {

  private String userID;//酷我用户ID

  private List<String> musicIDList;//过滤音乐ID列表

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public List<String> getMusicIDList() {
    return musicIDList;
  }

  public void setMusicIDList(List<String> musicIDList) {
    this.musicIDList = musicIDList;
  }
}