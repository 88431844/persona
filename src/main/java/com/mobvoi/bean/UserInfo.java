// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.util.List;

/**
 * created by zhhgao@mobvoi.com on 2018/10/18
 */
public class UserInfo {

  /**
   * 酷我用户ID
   */
  private String kwID;
  /**
   * 标签信息列表
   */
  private List<TagInfo> tags;

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }

  public List<TagInfo> getTags() {
    return tags;
  }

  public void setTags(List<TagInfo> tags) {
    this.tags = tags;
  }
}