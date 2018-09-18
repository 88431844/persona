// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import com.mobvoi.util.Const.playEnum;

/**
 * 权值计算 工具类 created by zhhgao@mobvoi.com on 2018/9/17
 */
public class CalculateScore {

  /**
   * 计算本次 用户标签
   */
  public static double processUserTagsScore(double playProportion, Integer musicAction,
      Integer comeFrom) {
    double periodScore = 0D;//标签权值
    double excitationScore = getExcitationScore(playProportion);//激励权值

    //音乐动作：收藏
    if (Const.musicEnum.MUSIC_ACTION_COLLECTION.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_COLLECTION.getScore();
    }
    //音乐动作：取消收藏
    else if (Const.musicEnum.MUSIC_ACTION_COLLECTION_CANCEL.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_COLLECTION_CANCEL.getScore();
    }
    //音乐动作：下载完成
    else if (Const.musicEnum.MUSIC_ACTION_DOWNLOAD_SUCCESS.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_DOWNLOAD_SUCCESS.getScore();
    }
    //音乐动作：下载后删除
    else if (Const.musicEnum.MUSIC_ACTION_DOWNLOAD_DELETE.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_DOWNLOAD_DELETE.getScore();
    }
    //音乐动作：购买单曲
    else if (Const.musicEnum.MUSIC_ACTION_BUY_SONG.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_BUY_SONG.getScore();
    }
    //音乐动作：购买会员
    else if (Const.musicEnum.MUSIC_ACTION_BUY_VIP.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_BUY_VIP.getScore();
    }
    //音乐动作：查看歌词
    else if (Const.musicEnum.MUSIC_ACTION_LOOK_LYRICS.getCode() == musicAction) {
      periodScore = Const.musicEnum.MUSIC_ACTION_LOOK_LYRICS.getScore();
    }

    //音乐来源：上一首
    else if (Const.musicEnum.COME_FROM_LAST.getCode() == comeFrom) {
      periodScore = excitationScore + Const.musicEnum.COME_FROM_LAST.getScore();
    }
    //音乐来源：下一首
    else if (Const.musicEnum.COME_FROM_NEXT.getCode() == comeFrom) {
      periodScore = excitationScore + Const.musicEnum.COME_FROM_NEXT.getScore();
    }
    //音乐来源：随机列表
    else if (Const.musicEnum.COME_FROM_RANDOM_LIST.getCode() == comeFrom) {
      periodScore = excitationScore + Const.musicEnum.COME_FROM_RANDOM_LIST.getScore();
    }
    //音乐来源：循环列表
    else if (Const.musicEnum.COME_FROM_LOOP_LIST.getCode() == comeFrom) {
      periodScore = excitationScore + Const.musicEnum.COME_FROM_LOOP_LIST.getScore();
    }
    //音乐来源：搜索
    else if (Const.musicEnum.COME_FROM_SEARCH.getCode() == comeFrom) {
      periodScore = excitationScore + Const.musicEnum.COME_FROM_SEARCH.getScore();
    }

    return periodScore;
  }

  /**
   * 播放比例 激励权值计算
   */
  private static double getExcitationScore(double playProportion) {
    //默认 认为播放比例低于百分之十
    double excitationScore = playEnum.PLAY_10_UNDER.getScore();

    if (playProportion >= playEnum.PLAY_90.getPercent()) {
      excitationScore = playEnum.PLAY_90.getScore();
    } else if (playProportion >= playEnum.PLAY_75.getPercent()) {
      excitationScore = playEnum.PLAY_75.getScore();
    } else if (playProportion >= playEnum.PLAY_50.getPercent()) {
      excitationScore = playEnum.PLAY_50.getScore();
    } else if (playProportion >= playEnum.PLAY_25.getPercent()) {
      excitationScore = playEnum.PLAY_25.getScore();
    } else if (playProportion >= playEnum.PLAY_10.getPercent()) {
      excitationScore = playEnum.PLAY_10.getScore();
    }

    return excitationScore;
  }
}