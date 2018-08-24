// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.bean;

/**
 * created by zhhgao@mobvoi.com on 18-8-21
 */
@Deprecated
public class MusicInfo {

  //酷我用户ID
  private String userID;
  //音乐ID
  private String musicID;
  //音乐标题
  private String musicTitle;
  //演唱者
  private String artist;
  //专辑
  private String album;
  //年代
  private String years;
  //音乐流派
  private String musicGenre;
  //时间戳（13位，单位毫秒）
  private long timestamp;
  //音乐来源：推送、搜索、榜单等。
  private String musicFrom;
  //音乐时长(单位:秒)
  private int musicDuration;
  //播放时长(单位:秒)
  private int playDuration;
  //动作：收藏（喜欢）、踩、上一曲、下一曲等。
  private String musicAction;

  @Override
  public String toString() {
    return "MusicInfo{" +
        "userID='" + userID + '\'' +
        ", musicID='" + musicID + '\'' +
        ", musicTitle='" + musicTitle + '\'' +
        ", artist='" + artist + '\'' +
        ", album='" + album + '\'' +
        ", years='" + years + '\'' +
        ", musicGenre='" + musicGenre + '\'' +
        ", timestamp=" + timestamp +
        ", musicFrom='" + musicFrom + '\'' +
        ", musicDuration=" + musicDuration +
        ", playDuration=" + playDuration +
        ", musicAction='" + musicAction + '\'' +
        '}';
  }

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

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getMusicFrom() {
    return musicFrom;
  }

  public void setMusicFrom(String musicFrom) {
    this.musicFrom = musicFrom;
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
}