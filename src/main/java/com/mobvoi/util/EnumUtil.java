// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;


/**
 * created by zhhgao@mobvoi.com on 2018/9/18
 */
public class EnumUtil {

  //用户画像配置文件
  private static final String processPersona = Const.CONFIG_PROCESS_PERSONA;

  /**
   * 音乐动作 枚举类
   */
  public enum musicEnum {

    /**
     * 音乐来源
     */
    //上一曲
    COME_FROM_LAST(0,
        Double.parseDouble(PropertiesUtil.getProperties("come.from.last.score", processPersona)),
        PropertiesUtil.getProperties("come.from.last.msg", processPersona)),

    //下一曲
    COME_FROM_NEXT(1,
        Double.parseDouble(PropertiesUtil.getProperties("come.from.next.score", processPersona)),
        PropertiesUtil.getProperties("come.from.next.msg", processPersona)),

    //搜索
    COME_FROM_SEARCH(2,
        Double.parseDouble(PropertiesUtil.getProperties("come.from.search.score", processPersona)),
        PropertiesUtil.getProperties("come.from.search.msg", processPersona)),

    //循环列表
    COME_FROM_LOOP_LIST(3,
        Double
            .parseDouble(PropertiesUtil.getProperties("come.from.loop.list.score", processPersona)),
        PropertiesUtil.getProperties("come.from.loop.list.msg", processPersona)),

    //随机列表
    COME_FROM_RANDOM_LIST(4,
        Double.parseDouble(
            PropertiesUtil.getProperties("come.from.random.list.score", processPersona)),
        PropertiesUtil.getProperties("come.from.random.list.msg", processPersona)),

    /**
     * 音乐动作
     */
    //收藏
    MUSIC_ACTION_COLLECTION(5,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.collection.score", processPersona)),
        PropertiesUtil.getProperties("music.action.collection.msg", processPersona)),

    //取消收藏
    MUSIC_ACTION_COLLECTION_CANCEL(6,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.collection.cancel.score", processPersona)),
        PropertiesUtil.getProperties("music.action.collection.cancel.msg", processPersona)),

    //下载成功
    MUSIC_ACTION_DOWNLOAD_SUCCESS(7,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.download.success.score", processPersona)),
        PropertiesUtil.getProperties("music.action.download.success.msg", processPersona)),

    //下载后删除
    MUSIC_ACTION_DOWNLOAD_DELETE(8,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.download.delete.score", processPersona)),
        PropertiesUtil.getProperties("music.action.download.delete.msg", processPersona)),

    //购买单曲
    MUSIC_ACTION_BUY_SONG(9,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.buy.song.score", processPersona)),
        PropertiesUtil.getProperties("music.action.buy.song.msg", processPersona)),

    //购买会员
    MUSIC_ACTION_BUY_VIP(10,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.buy.vip.score", processPersona)),
        PropertiesUtil.getProperties("music.action.buy.vip.msg", processPersona)),

    //查看歌词
    MUSIC_ACTION_LOOK_LYRICS(11,
        Double.parseDouble(
            PropertiesUtil.getProperties("music.action.look.lyrics.score", processPersona)),
        PropertiesUtil.getProperties("music.action.look.lyrics.msg", processPersona));

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
        Double.parseDouble(
            PropertiesUtil.getProperties("play.percent.10.under.score", processPersona))),

    PLAY_10(10,
        Double.parseDouble(PropertiesUtil.getProperties("play.percent.10.score", processPersona))),

    PLAY_25(25,
        Double.parseDouble(PropertiesUtil.getProperties("play.percent.25.score", processPersona))),

    PLAY_50(50,
        Double.parseDouble(PropertiesUtil.getProperties("play.percent.50.score", processPersona))),

    PLAY_75(75,
        Double.parseDouble(PropertiesUtil.getProperties("play.percent.75.score", processPersona))),

    PLAY_90(90,
        Double.parseDouble(PropertiesUtil.getProperties("play.percent.90.score", processPersona)));

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