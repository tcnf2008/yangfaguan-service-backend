// Copyright 2021 Haipeng.wang . All Rights Reserved.

package com.nau.util.http;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.nau.util.AsyncRunUtil;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * http 请求工具类
 *
 * @author Haipeng.wang NAU
 */
public final class XHttpUtil {

  /**
   * request head: 设置 read timeout
   */
  public static final String HEAD_KEY_READ_TIMEOUT = "dynamic-read-timeout";
  public static final MediaType DEFAULT_MEDIA_TYPE
      = MediaType.get("application/json; charset=utf-8");
  private static final Logger LOGGER = LoggerFactory.getLogger(XHttpUtil.class);
  private static final int DEFAULT_TIMEOUT = 10000;
  private static OkHttpClient okHttpClient = null;

  static {
    Builder clientBuilder = new OkHttpClient().newBuilder();

    clientBuilder
        .addInterceptor(new DynamicTimeoutInterceptor())
        .connectTimeout(60000, TimeUnit.MILLISECONDS)
        .readTimeout(60000, TimeUnit.MILLISECONDS)
        .writeTimeout(60000, TimeUnit.MILLISECONDS)
        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),
            SSLSocketClient.getX509TrustManager()) //配置
        .hostnameVerifier(SSLSocketClient.getHostnameVerifier()); //配置
    okHttpClient = clientBuilder.build();
  }

  public static XHttpBaseResponse get(String url) throws SocketTimeoutException {
    return get(url, DEFAULT_TIMEOUT);
  }

  public static XHttpBaseResponse get(String url, int timeout) throws SocketTimeoutException {
    return executeHttpMethod(HttpMethod.GET, url, null, null, null, timeout);
  }

  public static XHttpBaseResponse get(String url, Map<String, String> headerMap, int timeout)
      throws SocketTimeoutException {
    return executeHttpMethod(HttpMethod.GET, url, headerMap, null, null, timeout);
  }

  /**
   * getAudio.
   *
   * @param url     url
   * @param timeout timeout
   * @return response
   * @throws SocketTimeoutException 异常
   */
  public static XHttpBaseResponse getAudio(String url, int timeout) throws SocketTimeoutException {
    return executeHttpMethodAudio(HttpMethod.GET, url, null, null, null, timeout);
  }

  public static XHttpBaseResponse post(String url, String data) throws SocketTimeoutException {
    return post(url, data, null, DEFAULT_TIMEOUT);
  }

  /**
   * @param timeoutMS 毫秒数.
   */
  public static XHttpBaseResponse post(String url, String data, int timeoutMS)
      throws SocketTimeoutException {
    return post(url, data, null, timeoutMS);
  }

  /**
   * post json的请求方式： XHttpUtil.post(url,
   *         "{}",
   *         "application/json").
   * post x-www-form-urlencoded的请求方式： XHttpUtil.post(url,
   *         "ke1=v1&k2=v2",
   *         "application/x-www-form-urlencoded").
   */
  public static XHttpBaseResponse post(String url, String data, String contentType)
      throws SocketTimeoutException {
    return post(url, data, contentType, DEFAULT_TIMEOUT);
  }

  public static XHttpBaseResponse post(String url, String data, Map<String, String> headerMap,
      String contentType) throws SocketTimeoutException {
    return executeHttpMethod(HttpMethod.POST, url, headerMap, data, contentType, DEFAULT_TIMEOUT);
  }

  public static XHttpBaseResponse post(String url, String data, Map<String, String> headerMap,
      String contentType, Integer timeoutMs) throws SocketTimeoutException {
    return executeHttpMethod(HttpMethod.POST, url, headerMap, data, contentType, timeoutMs);
  }

  /**
   * @param data      json string
   * @param timeoutMS in ms.
   */
  public static XHttpBaseResponse post(String url, String data, String contentType, int timeoutMS)
      throws SocketTimeoutException {
    return executeHttpMethod(HttpMethod.POST, url, null, data, contentType, timeoutMS);
  }

//
//  public static XHttpBaseResponse postForm(String url, Map<String, String> headerMap,
//      Map<String, String> formMap, String contentType, int timeout) {
//    XHttpBaseResponse xHttpBaseResponse = new XHttpBaseResponse();
//
//    MediaType mediaType = DEFAULT_MEDIA_TYPE;
//    if (StringUtils.isNotBlank(contentType)) {
//      mediaType = MediaType.get(contentType);
//    }
//
//    if (timeout <= 0) {
//      timeout = DEFAULT_TIMEOUT;
//    }
//
//    if (headerMap == null) {
//      headerMap = new HashMap<>();
//      headerMap.put(HEAD_KEY_READ_TIMEOUT, String.valueOf(timeout)); // 设置超时时间
//    }
//
//    FormBody.Builder formBodyBuilder = new FormBody.Builder();
//    if (formMap != null && formMap.size() > 0) {
//      for (Entry<String, String> stringStringEntry : formMap.entrySet()) {
//        formBodyBuilder.add(stringStringEntry.getKey(), stringStringEntry.getValue());
//      }
//    }
//
//    RequestBody formBody = formBodyBuilder
//        .build();
//
//    Request.Builder requestBuilder = new Request.Builder();
//    requestBuilder.url(url);
//
//
//    for (Entry<String, String> entry : headerMap.entrySet()) {
//      requestBuilder.addHeader(entry.getKey(), entry.getValue());
//    }
//
//    Request request = requestBuilder
//        .post(formBody)
//        .build();
//
//    Call call = okHttpClient.newCall(request);
//    try {
//      Response response = call.execute();
//      System.out.println(response.body().string());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  /**
   * HttpMethods
   *
   * @param method @see HttpMethods
   */
  public static XHttpBaseResponse executeHttpMethod(HttpMethod method, String url,
      Map<String, String> headerMap, String postData,
      String contentType, Integer timeoutMs)
      throws SocketTimeoutException {
    XHttpBaseResponse xHttpBaseResponse = new XHttpBaseResponse();
    long startMS = System.currentTimeMillis();

    MediaType mediaType = DEFAULT_MEDIA_TYPE;
    if (StringUtils.isNotBlank(contentType)) {
      mediaType = MediaType.get(contentType);
    }

    if (timeoutMs == null) {
      timeoutMs = DEFAULT_TIMEOUT;
    }

    if (headerMap == null) {
      headerMap = new HashMap<>();
    }
    boolean needPrintHeaders = false;
    if (CollUtil.isNotEmpty(headerMap)) {
      needPrintHeaders = true;
    }
    headerMap.put(HEAD_KEY_READ_TIMEOUT, String.valueOf(timeoutMs)); // 设置超时时间

    RequestBody requestBody = null;
    if (HttpMethod.POST == method
        || HttpMethod.PUT == method
        || HttpMethod.DELETE == method) {
      requestBody = RequestBody.create(mediaType, postData);
    }
    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder
        .url(url)
        .method(method.name(), requestBody);

    for (Entry<String, String> entry : headerMap.entrySet()) {
      requestBuilder.addHeader(entry.getKey(), entry.getValue());
    }

    Request request = requestBuilder.build();

    try {
      try (Response response = okHttpClient.newCall(request).execute()) {
        xHttpBaseResponse.setStatusCode(response.code());
        xHttpBaseResponse.setHeaders(response.headers());
        xHttpBaseResponse.setStringContent(response.body().string());
        LOGGER.info("XhttpUtil {} {} {}ms {} requestData: {} ##result: {}",
            method.name(), url, System.currentTimeMillis() - startMS,
            needPrintHeaders ? "headers: " + JSON.toJSONString(headerMap) : "", postData,
            StringUtils.substring(xHttpBaseResponse.getStringContent(), 0, 10000));
      }
    } catch (SocketTimeoutException e) {
      LOGGER.error(
          String.format("TimeOutException XhttpUtil : %s %s  %d ms %s  data:%s ##result:%s",
              method.name(), url, System.currentTimeMillis() - startMS, needPrintHeaders ? "headers: " + JSON.toJSONString(headerMap) : "", postData,
              xHttpBaseResponse.getStringContent()));
      throw e;
    } catch (Exception ex) {
      LOGGER.error(
          String.format(" XhttpUtil : %s  %s %d ms %s data:%s ##result:%s   error: %s", ex,
              method.name(), url, System.currentTimeMillis() - startMS, needPrintHeaders ? "headers: " + JSON.toJSONString(headerMap) : "", postData,
              xHttpBaseResponse.getStringContent(),
              ex.getMessage()));
    }
    return xHttpBaseResponse;
  }

  /**
   * HttpMethods.
   *
   * @param method @see HttpMethods
   */
  public static XHttpBaseResponse executeHttpMethodAudio(HttpMethod method, String url,
      Map<String, String> headerMap, String data,
      String contentType, Integer timeout)
      throws SocketTimeoutException {
    XHttpBaseResponse httpBaseResponse = new XHttpBaseResponse();
    long startMS = System.currentTimeMillis();

    MediaType mediaType = DEFAULT_MEDIA_TYPE;
    if (StringUtils.isNotBlank(contentType)) {
      mediaType = MediaType.get(contentType);
    }

    if (timeout == null) {
      timeout = DEFAULT_TIMEOUT;
    }

    if (headerMap == null) {
      headerMap = new HashMap<>();
    }
    boolean needPrintHeaders = false;
    if (CollUtil.isNotEmpty(headerMap)) {
      needPrintHeaders = true;
    }
    headerMap.put(HEAD_KEY_READ_TIMEOUT, String.valueOf(timeout)); // 设置超时时间

    RequestBody requestBody = null;
    if (HttpMethod.POST == method
        || HttpMethod.PUT == method
        || HttpMethod.DELETE == method) {
      requestBody = RequestBody.create(mediaType, data);
    }
    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder
        .url(url)
        .method(method.name(), requestBody);

    for (Entry<String, String> entry : headerMap.entrySet()) {
      requestBuilder.addHeader(entry.getKey(), entry.getValue());
    }

    Request request = requestBuilder.build();

    try {
      try (Response response = okHttpClient.newCall(request).execute()) {
        httpBaseResponse.setStatusCode(response.code());
        httpBaseResponse.setHeaders(response.headers());
//        if (!"audio".equals(response.body().contentType().type())) {
//          throw new BusinessException(url + " 非音频格式");
//        }
        InputStream is = null;
        int len = 0;
        byte[] buf = new byte[2048];
        try {
          is = response.body().byteStream();
          ByteArrayOutputStream fos = new ByteArrayOutputStream();
          while ((len = is.read(buf)) != -1) {
            fos.write(buf, 0, len);
          }
          fos.flush();
          httpBaseResponse.setBytes(fos.toByteArray());
        } catch (Exception e) {
          throw new RuntimeException(e.getMessage());
        } finally {
          if (is != null) {
            is.close();
          }
        }
        LOGGER.info("XhttpUtil {} {} {}ms {} requestData: {} ##result: {}",
            method.name(), url, System.currentTimeMillis() - startMS,
            needPrintHeaders ? "headers: " + JSON.toJSONString(headerMap) : "", data,
            StringUtils.substring(httpBaseResponse.getStringContent(), 0, 5000));
      }
    } catch (SocketTimeoutException e) {
      LOGGER.error(
          String.format("TimeOutException XhttpUtil : %s  %s   data:%s    ### result:%s  time:%dms",
              method.name(), url, data,
              httpBaseResponse.getStringContent(), System.currentTimeMillis() - startMS));
      throw e;
    } catch (Exception ex) {
      LOGGER.error(
          String.format(" XhttpUtil : %s  %s   data:%s    ### result:%s  time:%d ms  error: %s ",
              method.name(), url, data,
              httpBaseResponse.getStringContent(), System.currentTimeMillis() - startMS,
              ex.getMessage()), ex);
    }
    return httpBaseResponse;
  }

  /**
   * 请求.
   * @param request 请求
   * @return 响应
   */
  public static Response newCall(Request request) {
    Response response = null;
    try {
      response = okHttpClient.newCall(request).execute();
    } catch (Exception e) {
      LOGGER.error("request exception {}", request, e);
    }
    return response;
  }

  public enum HttpMethod {
    CONNECT, DELETE, HEAD, TRACE, OPTIONS,
    GET, POST, PUT, PATCH
  }

  public static class XHttpBaseResponse {

    private int statusCode = -1;
    private String stringContent = "";
    private Headers headers;
    private byte[] bytes;

    public XHttpBaseResponse() {
    }

    public int getStatusCode() {
      return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }

    public String getStringContent() {
      return this.stringContent;
    }

    public void setStringContent(String stringContent) {
      this.stringContent = stringContent;
    }

    public Headers getHeaders() {
      return headers;
    }

    public void setHeaders(Headers headers) {
      this.headers = headers;
    }

    public byte[] getBytes() {
      return Arrays.copyOf(bytes, bytes.length);
    }

    public void setBytes(byte[] bytes) {
      this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public String toString() {
      return "XHttpBaseResponse{" +
          "statusCode=" + statusCode +
          ", stringContent='" + stringContent + '\'' +
          '}';
    }

    /**
     * 成功获取到返回内容，返回内容不为空.
     * @return
     */
    public boolean successGotContent() {
      if (this.getStatusCode() != 200 || StringUtils.isBlank(this.getStringContent())) {
        return false;
      }
      return true;
    }
  }

  /**
   * 动态设置timeout
   */
  public static class DynamicTimeoutInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Chain newChain = chain;
      try {
        String connectTimeout = request.header("dynamic-connect-timeout");
        if (StringUtils.isNotBlank(connectTimeout)) {
          newChain = chain.withConnectTimeout(Integer.parseInt(connectTimeout),
              TimeUnit.MILLISECONDS);
          LOGGER.trace("set dynamic connetTimeout, timeout:{}ms", connectTimeout);
        }

        String readTimeout = request.header("dynamic-read-timeout");
        if (StringUtils.isNotBlank(readTimeout)) {
          newChain = chain.withReadTimeout(Integer.parseInt(readTimeout), TimeUnit.MILLISECONDS);
          LOGGER.trace("set dynamic readTimeout, timeout:{}ms", readTimeout);
        }

        String writeTimeout = request.header("dynamic-write-timeout");
        if (StringUtils.isNotBlank(writeTimeout)) {
          newChain = chain.withWriteTimeout(Integer.parseInt(writeTimeout), TimeUnit.MILLISECONDS);
          LOGGER.trace("set dynamic writeTimeout, timeout:{}ms", writeTimeout);
        }
      } catch (Exception e) {
        LOGGER.error("set timeout error {}", e.toString(), e);
      }
      return newChain.proceed(request);
    }
  }
}

