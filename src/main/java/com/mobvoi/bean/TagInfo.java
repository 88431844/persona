// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;


import java.io.Serializable;

/**
 * 标签信息 实体类 created by zhhgao@mobvoi.com on 18-8-22
 */
public class TagInfo implements Serializable {

  /**
   * 标签ID
   */
  private String tagID;
  /**
   * 标签名称
   */
  private String tagName;
  /**
   * 标签区间权重
   */
  private double periodScore;

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getTagID() {
    return tagID;
  }

  public void setTagID(String tagID) {
    this.tagID = tagID;
  }

  public double getPeriodScore() {
    return periodScore;
  }

  public void setPeriodScore(double periodScore) {
    this.periodScore = periodScore;
  }
}