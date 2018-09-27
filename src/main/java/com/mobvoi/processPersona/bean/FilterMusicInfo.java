// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona.bean;

import java.io.Serializable;

/**
 * 过滤用户音乐 实体类 created by zhhgao@mobvoi.com on 2018/9/13
 */
public class FilterMusicInfo implements Serializable {

  /**
   * 酷我用户ID
   */
  private String kwID;
  /**
   * 过滤音乐ID
   */
  private String musicID;

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }

  public String getMusicID() {
    return musicID;
  }

  public void setMusicID(String musicID) {
    this.musicID = musicID;
  }
}