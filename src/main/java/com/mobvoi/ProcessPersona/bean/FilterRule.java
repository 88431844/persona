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
  private String userID;

  /**
   * 音乐ID
   */
  private String musicID;

  /**
   * 播放百分比
   */
  private double playProportion;

  /**
   * 播放次数
   */
  private int playTimes;

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

  public int getPlayTimes() {
    return playTimes;
  }

  public void setPlayTimes(int playTimes) {
    this.playTimes = playTimes;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }
}