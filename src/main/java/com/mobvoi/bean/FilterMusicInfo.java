// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 过滤用户音乐 实体类 created by zhhgao@mobvoi.com on 2018/9/13
 */
public class FilterMusicInfo implements Serializable {

  /**
   * 酷我用户ID
   */
  private String kwID;
  /**
   * 过滤音乐ID列表
   */
  private List<String> musicIDs;

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }

  public List<String> getMusicIDs() {
    return musicIDs;
  }

  public void setMusicIDs(List<String> musicIDs) {
    this.musicIDs = musicIDs;
  }
}