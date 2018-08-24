// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by zhhgao@mobvoi.com on 18-8-22
 */
public class PointInfo implements Serializable {
  //酷我用户ID
  private String userID;
  //音乐ID
  private String musicID;
  //音乐时长(单位:秒)
  private int musicDuration;
  //播放时长(单位:秒)
  private int playDuration;
  //动作：收藏（喜欢）、踩、上一曲、下一曲等。
  private String musicAction;
  //标签ID列表
  private List<String> tags;

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

  public String getMusicAction() {
    return musicAction;
  }

  public void setMusicAction(String musicAction) {
    this.musicAction = musicAction;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}