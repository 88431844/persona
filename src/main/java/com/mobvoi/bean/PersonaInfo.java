// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class PersonaInfo implements Serializable {
  //酷我用户ID
  private String userID;
  //标签列表
  private List<TagInfo> tagInfoList;

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
}