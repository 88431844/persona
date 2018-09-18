// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona.bean;

/**
 * 过滤用户音乐 实体类 created by zhhgao@mobvoi.com on 2018/9/13
 */
public class FilterMusicInfo {

  private String userID;//酷我用户ID

  private String musicID;//过滤音乐ID

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getMusicID() {
    return musicID;
  }

  public void setMusicID(String musicID) {
    this.musicID = musicID;
  }
}