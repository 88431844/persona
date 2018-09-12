// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 埋点信息
 * created by zhhgao@mobvoi.com on 18-8-22
 */
@Deprecated
public class PointInfo implements Serializable {

  private String userID;//酷我用户ID

  private String musicID;//音乐ID

  private int musicDuration;//音乐时长(单位:秒)

  private int playDuration;//播放时长(单位:秒)

  private int musicAction;//动作：详见Const常量类。

  private int comeFrom;//音乐来源：详见Const常量类。

  private List<String> tags;//标签ID列表

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

  public int getMusicDuration() {
    return musicDuration;
  }

  public void setMusicDuration(int musicDuration) {
    this.musicDuration = musicDuration;
  }

  public int getPlayDuration() {
    return playDuration;
  }

  public void setPlayDuration(int playDuration) {
    this.playDuration = playDuration;
  }

  public int getMusicAction() {
    return musicAction;
  }

  public void setMusicAction(int musicAction) {
    this.musicAction = musicAction;
  }

  public int getComeFrom() {
    return comeFrom;
  }

  public void setComeFrom(int comeFrom) {
    this.comeFrom = comeFrom;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}