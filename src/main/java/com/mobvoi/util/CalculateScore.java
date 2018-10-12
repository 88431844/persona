// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import com.mobvoi.util.EnumUtil.playEnum;

/**
 * 权值计算 工具类 created by zhhgao@mobvoi.com on 2018/9/17
 */
public class CalculateScore {

  /**
   * 计算本次 用户标签
   */
  public static double processUserTagsScore(double playProportion, String musicAction,
      String comeFrom) {
    double periodScore;//标签权值

    double musicActionScore = getMusicActionScore(musicAction);//音乐动作 权值
    double excitationScore = getExcitationScore(playProportion);//激励权值
    double comeFromScore = getComeFromScore(comeFrom);//音乐来源 权值

    periodScore = musicActionScore + excitationScore + comeFromScore;
    return periodScore;
  }

  /**
   * 计算音乐来源 权值
   */
  private static double getComeFromScore(String comeFrom) {
    double comeFromScore = 0D;
    //音乐来源：上一首
    if (EnumUtil.musicEnum.COME_FROM_LAST.getMsg().equals(comeFrom)) {
      comeFromScore = EnumUtil.musicEnum.COME_FROM_LAST.getScore();
    }
    //音乐来源：下一首
    else if (EnumUtil.musicEnum.COME_FROM_NEXT.getMsg().equals(comeFrom)) {
      comeFromScore = EnumUtil.musicEnum.COME_FROM_NEXT.getScore();
    }
    //音乐来源：随机列表
    else if (EnumUtil.musicEnum.COME_FROM_RANDOM_LIST.getMsg().equals(comeFrom)) {
      comeFromScore = EnumUtil.musicEnum.COME_FROM_RANDOM_LIST.getScore();
    }
    //音乐来源：循环列表
    else if (EnumUtil.musicEnum.COME_FROM_LOOP_LIST.getMsg().equals(comeFrom)) {
      comeFromScore = EnumUtil.musicEnum.COME_FROM_LOOP_LIST.getScore();
    }
    //音乐来源：搜索
    else if (EnumUtil.musicEnum.COME_FROM_SEARCH.getMsg().equals(comeFrom)) {
      comeFromScore = EnumUtil.musicEnum.COME_FROM_SEARCH.getScore();
    }
    return comeFromScore;
  }

  /**
   * 计算音乐动作 权值
   *
   * @param musicAction 音乐动作
   * @return musicActionScore
   */
  private static double getMusicActionScore(String musicAction) {
    double musicActionScore = 0D;
    //音乐动作：收藏
    if (EnumUtil.musicEnum.MUSIC_ACTION_COLLECTION.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_COLLECTION.getScore();
    }
    //音乐动作：取消收藏
    else if (EnumUtil.musicEnum.MUSIC_ACTION_COLLECTION_CANCEL.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_COLLECTION_CANCEL.getScore();
    }
    //音乐动作：下载完成
    else if (EnumUtil.musicEnum.MUSIC_ACTION_DOWNLOAD_SUCCESS.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_DOWNLOAD_SUCCESS.getScore();
    }
    //音乐动作：下载后删除
    else if (EnumUtil.musicEnum.MUSIC_ACTION_DOWNLOAD_DELETE.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_DOWNLOAD_DELETE.getScore();
    }
    //音乐动作：购买单曲
    else if (EnumUtil.musicEnum.MUSIC_ACTION_BUY_SONG.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_BUY_SONG.getScore();
    }
    //音乐动作：购买会员
    else if (EnumUtil.musicEnum.MUSIC_ACTION_BUY_VIP.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_BUY_VIP.getScore();
    }
    //音乐动作：查看歌词
    else if (EnumUtil.musicEnum.MUSIC_ACTION_LOOK_LYRICS.getMsg().equals(musicAction)) {
      musicActionScore = EnumUtil.musicEnum.MUSIC_ACTION_LOOK_LYRICS.getScore();
    }
    return musicActionScore;
  }

  /**
   * 计算播放比例 激励权值
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