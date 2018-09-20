// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 埋点信息 实体类 created by zhhgao@mobvoi.com on 2018/9/19
 */
public class PointInfo implements Serializable {

  /**
   * 酷我用户ID
   */
  private String kwID;
  /**
   * 音乐ID
   */
  private String musicID;
  /**
   * 本次计算的标签权值
   */
  private double thisPeriodScore;
  /**
   * 播放比例
   */
  private double playProportion;
  /**
   * 埋点中标签名称列表
   */
  private List<String> tagNameList;

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }

  public double getThisPeriodScore() {
    return thisPeriodScore;
  }

  public void setThisPeriodScore(double thisPeriodScore) {
    this.thisPeriodScore = thisPeriodScore;
  }

  public List<String> getTagNameList() {
    return tagNameList;
  }

  public void setTagNameList(List<String> tagNameList) {
    this.tagNameList = tagNameList;
  }

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
}