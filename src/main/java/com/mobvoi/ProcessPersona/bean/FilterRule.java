// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona.bean;


import java.io.Serializable;

/**
 * 过滤音乐规则 实体类 created by zhhgao@mobvoi.com on 2018/9/14
 */
public class FilterRule implements Serializable {

  /**
   * 酷我用户ID
   */
  private String kwID;

  /**
   * 音乐ID
   */
  private String musicID;

  /**
   * 播放百分比
   */
  private double playProportion;

  public String getMusicID() {
    return musicID;
  }

  public void setMusicID(String musicID) {
    this.musicID = musicID;
  }

  public double getPlayProportion() {
    return playProportion;
  }

  public void setPlayProportion(double playProportion) {
    this.playProportion = playProportion;
  }

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }
}