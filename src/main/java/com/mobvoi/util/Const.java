// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

/**
 * 基本常量类 created by zhhgao@mobvoi.com on 2018/9/4
 */
public class Const {

  /**
   * 音乐动作 枚举类
   */
  public enum musicEnum {

    /**
     * 音乐来源
     */
    //上一曲
    COME_FROM_LAST(0,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("come.from.last.score")),
        PropertiesUtil.getPropertiesOut("come.from.last.msg")),

    //下一曲
    COME_FROM_NEXT(1,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("come.from.next.score")),
        PropertiesUtil.getPropertiesOut("come.from.next.msg")),

    //搜索
    COME_FROM_SEARCH(2,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("come.from.search.score")),
        PropertiesUtil.getPropertiesOut("come.from.search.msg")),

    //循环列表
    COME_FROM_LOOP_LIST(3,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("come.from.loop.list.score")),
        PropertiesUtil.getPropertiesOut("come.from.loop.list.msg")),

    //随机列表
    COME_FROM_RANDOM_LIST(4,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("come.from.random.list.score")),
        PropertiesUtil.getPropertiesOut("come.from.random.list.msg")),

    /**
     * 音乐动作
     */
    //收藏
    MUSIC_ACTION_COLLECTION(5,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.collection.score")),
        PropertiesUtil.getPropertiesOut("music.action.collection.msg")),

    //取消收藏
    MUSIC_ACTION_COLLECTION_CANCEL(6,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.collection.cancel.score")),
        PropertiesUtil.getPropertiesOut("music.action.collection.cancel.msg")),

    //下载成功
    MUSIC_ACTION_DOWNLOAD_SUCCESS(7,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.download.success.score")),
        PropertiesUtil.getPropertiesOut("music.action.download.success.msg")),

    //下载后删除
    MUSIC_ACTION_DOWNLOAD_DELETE(8,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.download.delete.score")),
        PropertiesUtil.getPropertiesOut("music.action.download.delete.msg")),

    //购买单曲
    MUSIC_ACTION_BUY_SONG(9,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.buy.song.score")),
        PropertiesUtil.getPropertiesOut("music.action.buy.song.msg")),

    //购买会员
    MUSIC_ACTION_BUY_VIP(10,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.buy.vip.score")),
        PropertiesUtil.getPropertiesOut("music.action.buy.vip.msg")),

    //查看歌词
    MUSIC_ACTION_LOOK_LYRICS(11,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("music.action.look.lyrics.score")),
        PropertiesUtil.getPropertiesOut("music.action.look.lyrics.msg"));

    musicEnum(int code, double score, String msg) {
      this.code = code;
      this.score = score;
      this.msg = msg;
    }

    private final int code;

    private final double score;

    private String msg;

    public int getCode() {
      return code;
    }

    public double getScore() {
      return score;
    }

    public String getMsg() {
      return msg;
    }

  }

  public enum playEnum {

    //播放百分比
    PLAY_10_UNDER(10,
        Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.10.under.score"))),

    PLAY_10(10, Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.10.score"))),


    PLAY_25(25, Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.25.score"))),


    PLAY_50(50, Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.50.score"))),


    PLAY_75(75, Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.75.score"))),


    PLAY_90(90, Double.parseDouble(PropertiesUtil.getPropertiesOut("play.percent.90.score")));

    playEnum(double percent, double score) {
      this.percent = percent;
      this.score = score;
    }

    private final double percent;
    private final double score;

    public double getPercent() {
      return percent;
    }

    public double getScore() {
      return score;
    }

  }

}