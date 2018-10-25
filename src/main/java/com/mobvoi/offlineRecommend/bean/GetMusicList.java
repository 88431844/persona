// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.offlineRecommend.bean;

import com.mobvoi.bean.TagInfo;

/**
 * created by zhhgao@mobvoi.com on 2018/10/23
 */
public class GetMusicList extends TagInfo {

  /**
   * 标签ID
   */
  private String tagID;
  /**
   * 请求音乐数量
   */
  private long count;

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public String getTagID() {
    return tagID;
  }

  @Override
  public void setTagID(String tagID) {
    this.tagID = tagID;
  }
}