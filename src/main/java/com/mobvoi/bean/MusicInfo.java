// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * created by zhhgao@mobvoi.com on 18-8-21
 */
@Deprecated
public class MusicInfo implements Serializable {

  private String musicID;//音乐ID

  private String musicTitle;//音乐标题

  private String artist;//演唱者

  private String album;//专辑

  private String years;//年代

  private String musicGenre;//音乐流派

  private String fileType;//文件类型 例如：mp3、acc等

  private int fileSize;//文件大小 单位kb

  private String tags;//英文逗号分隔标签

  private int musicDuration;//音乐时长(单位:秒)

  private Date createdAt;//创建时间

  private Date updatedAt;//修改时间

  public String getMusicID() {
    return musicID;
  }

  public void setMusicID(String musicID) {
    this.musicID = musicID;
  }

  public String getMusicTitle() {
    return musicTitle;
  }

  public void setMusicTitle(String musicTitle) {
    this.musicTitle = musicTitle;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getYears() {
    return years;
  }

  public void setYears(String years) {
    this.years = years;
  }

  public String getMusicGenre() {
    return musicGenre;
  }

  public void setMusicGenre(String musicGenre) {
    this.musicGenre = musicGenre;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public int getFileSize() {
    return fileSize;
  }

  public void setFileSize(int fileSize) {
    this.fileSize = fileSize;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public int getMusicDuration() {
    return musicDuration;
  }

  public void setMusicDuration(int musicDuration) {
    this.musicDuration = musicDuration;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}