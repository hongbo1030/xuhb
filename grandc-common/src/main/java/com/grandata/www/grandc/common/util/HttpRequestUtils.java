package com.grandata.www.grandc.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpRequestUtils {
  private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

  private static final String charset = "UTF-8";

  /**
   * Creates {@link CloseableHttpClient} instance with default configuration.
   */
  public static CloseableHttpClient createDefault() {
    return HttpClientBuilder.create().build();
  }

  public static JSONObject httpPost(String url) {
    return httpPost(url, new JSONObject());
  }
  /**
   * http Post
   *
   * @param url
   * @param jsonParam
   * @return
   */
  public static JSONObject httpPost(String url, JSONObject jsonParam) {
    return httpPost(url, jsonParam, false);
  }

  /**
   * http post
   *
   * @param urlַ
   * @param jsonParam
   * @param noNeedResponse
   * @return
   */
  public static JSONObject httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
    CloseableHttpClient httpclient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpPost httpPost = new HttpPost(url);
    try {
      if (StringUtils.isNotBlank(jsonParam.toString())) {
        //
        StringEntity entity = new StringEntity(jsonParam.toString(), charset);
        entity.setContentEncoding(charset);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
      }
      CloseableHttpResponse response = httpclient.execute(httpPost);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      /** 正常返回 **/
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        try {
          if (noNeedResponse) {
            return null;
          }
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        } catch (ParseException e) {
          logger.error("post 请求异常:" + url, e);
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    } catch (IOException e) {
      logger.error("post 请求IO异常:" + url, e);
    }
    return jsonResult;
  }

  /**
   * http get
   *
   * @param url
   * @return
   */
  public static JSONObject httpGet(String url) {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpGet request = new HttpGet(url);
    try {
      CloseableHttpResponse response = httpClient.execute(request);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      /** 正常 **/
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    } catch (IOException e) {
      logger.error("get 请求IO异常:" + url, e);
    }
    return jsonResult;
  }

  /**
   * http put
   *
   * @param url
   * @return
   */
  public static JSONObject httpPut(String url) {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpPut request = new HttpPut(url);
    try {
      CloseableHttpResponse response = httpClient.execute(request);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        // 正常
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
        // 如果需要重定向，那么此时取出
        String Location = response.getLastHeader("Location").getValue();
        request = new HttpPut(Location);
        response = httpClient.execute(request);

        statusLine = response.getStatusLine();
        entity = response.getEntity();
        strResult = EntityUtils.toString(entity);
        if (statusLine.getStatusCode() == HttpStatus.SC_CREATED) {
          // 201。此时返回值为空
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        } else {
          if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
          }
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    } catch (IOException e) {
      logger.error("put 请求IO异常:" + url, e);
    }
    return jsonResult;
  }

  /**
   * http put
   *
   * @param url
   * @return
   */
  public static JSONObject httpPutFile2(String url, String localFile) {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    String strResult = "";

    HttpPut httpPost = new HttpPut(url);

    //head
    //httpPost.setHeader(null);
    //httpPost.setHeader("Content-Disposition", "abc");
    //httpPost.setHeader("Content-Type", "plain/text");

    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    entityBuilder.setCharset(Charset.forName(HTTP.UTF_8));
    entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

    //file
            File file = new File(localFile);
            entityBuilder.addPart("file1", new FileBody(file));
            //entityBuilder.addBinaryBody("file1", file);  //需要测试

            //String
            //entityBuilder.addPart("String1", new StringBody("测试中文1", contentType));

            //entityBuilder.addTextBody("method", "method方法");
            //entityBuilder.addTextBody("fileTypes", "fileTypes文件类型");

            //bytes
            //entityBuilder.addPart("bytes1", new ByteArrayBody("测试bytes".getBytes(), ContentType.DEFAULT_BINARY, "bytes1"));

    httpPost.setEntity(entityBuilder.build());

    HttpResponse response;
    try {
      response = httpClient.execute(httpPost);
      if(response.getStatusLine().getStatusCode() == 201){
        HttpEntity entity = response.getEntity();
        strResult = EntityUtils.toString(entity);
        System.out.println(strResult);
      }
    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  public static JSONObject httpPutFile(String url, String localFile) {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpPut request = new HttpPut(url);
    try {
      CloseableHttpResponse response = httpClient.execute(request);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        // 正常
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
        // 如果需要重定向，那么此时取出
        String Location = response.getLastHeader("Location").getValue();
        request = new HttpPut(Location);

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        org.apache.http.entity.FileEntity entityFile = new FileEntity(new File(localFile), contentType);
        entityFile.setContentEncoding(charset);
        entityFile.setContentType("application/json");
        request.setEntity(entityFile);

        response = httpClient.execute(request);

        statusLine = response.getStatusLine();
        entity = response.getEntity();
        strResult = EntityUtils.toString(entity);
        if (statusLine.getStatusCode() == HttpStatus.SC_CREATED) {
          // 201。此时返回值为空
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        } else {
          if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
          }
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    } catch (IOException e) {
      logger.error("put 请求IO异常:" + url, e);
    }
    return jsonResult;
  }

  public static JSONObject httpPutBytes(String url, byte[] bytes) throws IOException {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpPut request = new HttpPut(url);
    //try {
      CloseableHttpResponse response = httpClient.execute(request);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        // 正常
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
        // 如果需要重定向，那么此时取出
        String Location = response.getLastHeader("Location").getValue();
        request = new HttpPut(Location);

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        org.apache.http.entity.ByteArrayEntity entityBytes = new ByteArrayEntity(bytes);
        entityBytes.setContentEncoding(charset);
        entityBytes.setContentType("application/json");
        request.setEntity(entityBytes);

        response = httpClient.execute(request);

        statusLine = response.getStatusLine();
        entity = response.getEntity();
        strResult = EntityUtils.toString(entity);
        if (statusLine.getStatusCode() == HttpStatus.SC_CREATED) {
          // 201。此时返回值为空
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        } else {
          if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
          }
          if (StringUtils.isNotBlank(strResult)) {
            jsonResult = JSONObject.fromObject(strResult);
          }
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    //} catch (IOException e) {
    //  logger.error("put 请求IO异常:" + url, e);
    //}
    return jsonResult;
  }

  /**
   * http delete
   *
   * @param url
   * @return
   */
  public static JSONObject httpDelete(String url) {
    CloseableHttpClient httpClient = HttpRequestUtils.createDefault();
    JSONObject jsonResult = null;
    String strResult = "";
    HttpDelete request = new HttpDelete(url);
    try {
      CloseableHttpResponse response = httpClient.execute(request);

      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      strResult = EntityUtils.toString(entity);
      /** 正常 **/
      if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else if(statusLine.getStatusCode() >= 300) {
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      } else {
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        if (StringUtils.isNotBlank(strResult)) {
          jsonResult = JSONObject.fromObject(strResult);
        }
      }
    } catch (IOException e) {
      logger.error("delete 请求IO异常:" + url, e);
    }
    return jsonResult;
  }

  @Deprecated
  public static JSONObject postJsonData(String url, Map<String, String> params) {
    CloseableHttpClient httpclient = HttpRequestUtils.createDefault();
    HttpPost httpPost = new HttpPost(url);
    //
    List<NameValuePair> list = new ArrayList<NameValuePair>();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      System.out.println("key=" + key + " value=" + value);
      NameValuePair pair = new BasicNameValuePair(key, value);
      list.add(pair);
    }
    CloseableHttpResponse response = null;
    try {
      httpPost.setEntity(new UrlEncodedFormEntity(list));
      response = httpclient.execute(httpPost);
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject jsonObject = null;
    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      HttpEntity httpEntity = response.getEntity();
      String result = null;
      try {
        result = EntityUtils.toString(httpEntity);
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      jsonObject = JSONObject.fromObject(result);
    }
    return jsonObject;
  }

  public static JSONObject httpsPost(String url, JSONObject jsonParam) {
    CloseableHttpClient httpClient = null;
    HttpPost httpPost = null;
    JSONObject jsonObject = null;
    try {
      httpClient = new SSLClient();
      httpPost = new HttpPost(url);
      if (StringUtils.isNotBlank(jsonParam.toString())) {
        StringEntity entity = new StringEntity(jsonParam.toString(), charset);
        entity.setContentEncoding(charset);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
      }
      CloseableHttpResponse response = httpClient.execute(httpPost);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        HttpEntity httpEntity = response.getEntity();
        String result = null;
        try {
          result = EntityUtils.toString(httpEntity);
        } catch (ParseException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        jsonObject = JSONObject.fromObject(result);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return jsonObject;
  }

  public static JSONObject httpsGet(String url) {
    CloseableHttpClient httpClient = null;
    JSONObject jsonResult = null;
    try {
      httpClient = new SSLClient();
      HttpGet request = new HttpGet(url);
      CloseableHttpResponse response = httpClient.execute(request);

      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        String strResult = EntityUtils.toString(response.getEntity());
        jsonResult = JSONObject.fromObject(strResult);
      } else {
        logger.error("get 请求异常:" + url);
      }
    } catch (Exception e) {
      logger.error("get 请求异常:" + url, e);
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return jsonResult;
  }

  public static void main(String[] args) {
    JSONObject jsonParam = new JSONObject();
    JSONObject jsonResult = new JSONObject();
    String url = "";

    //http
    //post
    jsonParam = new JSONObject();
    jsonResult = new JSONObject();
    url = "";

    url = "http://192.168.10.203:8080/grandc/hive/exec.do";
    jsonParam.put("a", 0);
    jsonResult = HttpRequestUtils.httpPost(url, jsonParam);
    System.out.println(jsonResult);

    //get
    jsonParam = new JSONObject();
    jsonResult = new JSONObject();
    url = "";

    url = "http://192.168.10.203:8080/grandc/hive/list.do?name=a";
    jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);


    //https
    //post
    jsonParam = new JSONObject();
    jsonResult = new JSONObject();
    url = "";

    url = "https://192.168.10.203:8443/grandc/hive/exec.do";
    jsonParam.put("a", 0);
    jsonResult = HttpRequestUtils.httpsPost(url, jsonParam);
    System.out.println(jsonResult);

    //get
    jsonParam = new JSONObject();
    jsonResult = new JSONObject();
    url = "";

    url = "https://192.168.10.203:8443/grandc/hive/list.do?name=a";
    jsonResult = HttpRequestUtils.httpsGet(url);
    System.out.println(jsonResult);



    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
  }

}
