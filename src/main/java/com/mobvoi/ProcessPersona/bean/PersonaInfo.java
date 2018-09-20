// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.processPersona.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 用户标签 实体类
 * created by zhhgao@mobvoi.com on 18-8-21
 */
public class PersonaInfo implements Serializable {

  /**
   * 酷我用户ID
   */
  private String kwID;
  /**
   * 要添加的标签信息
   */
  private List<TagInfo> addTagInfoList;
  /**
   * 要更新的标签信息
   */
  private List<TagInfo> updateTagInfoList;

  /**
   * 埋点信息 列表
   */
  private List<PointInfo> pointInfoList;

  public String getKwID() {
    return kwID;
  }

  public void setKwID(String kwID) {
    this.kwID = kwID;
  }

  public List<TagInfo> getAddTagInfoList() {
    return addTagInfoList;
  }

  public void setAddTagInfoList(List<TagInfo> addTagInfoList) {
    this.addTagInfoList = addTagInfoList;
  }

  public List<TagInfo> getUpdateTagInfoList() {
    return updateTagInfoList;
  }

  public void setUpdateTagInfoList(
      List<TagInfo> updateTagInfoList) {
    this.updateTagInfoList = updateTagInfoList;
  }

  public List<PointInfo> getPointInfoList() {
    return pointInfoList;
  }

  public void setPointInfoList(List<PointInfo> pointInfoList) {
    this.pointInfoList = pointInfoList;
  }
}