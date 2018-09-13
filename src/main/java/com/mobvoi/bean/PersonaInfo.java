// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户标签 实体类
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class PersonaInfo implements Serializable {

  private String userID;//酷我用户ID

  private String musicID;//音乐ID

  private List<TagInfo> tagInfoList;//标签列表

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public List<TagInfo> getTagInfoList() {
    return tagInfoList;
  }

  public void setTagInfoList(List<TagInfo> tagInfoList) {
    this.tagInfoList = tagInfoList;
  }

  public String getMusicID() {
    return musicID;
  }

  public void setMusicID(String musicID) {
    this.musicID = musicID;
  }
}