// Copyright 2018 Mobvoi Inc. All Rights Reserved.

package com.mobvoi.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * HTTP 请求工具类
 * created by zhhgao@mobvoi.com on 2018/4/17
 */
public class HttpUtil {

  private static final Logger logger = Logger.getLogger(HttpUtil.class);
  public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

  public static String post(String url, String json, String contentType) {
    String resStr = "";
    //创建默认的httpClient实例.
    CloseableHttpClient httpclient = null;
    //接收响应结果
    CloseableHttpResponse response = null;
    try {
      //创建httppost
      httpclient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(url);
      httpPost.addHeader(HTTP.CONTENT_TYPE, contentType);
      StringEntity se = new StringEntity(json, "UTF-8");
      se.setContentEncoding("UTF-8");
      se.setContentType(contentType);//发送json需要设置contentType
      httpPost.setEntity(se);
      response = httpclient.execute(httpPost);
      //解析返结果
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        resStr = EntityUtils.toString(entity, "UTF-8");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        httpclient.close();
        response.close();
      } catch (Exception e) {
        logger.error("HttpUtil post http close error : " + e.getMessage());
      }
    }
    return resStr;
  }

}