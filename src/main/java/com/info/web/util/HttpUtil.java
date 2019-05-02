package com.info.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpUtil {
    //定义sms
    //dc 数据类型
    private static final int DATACODING = 15;
    //rf 响应格式
    private static final int REPSPONSEFORMAT = 2;
    //rd 是否需要状态报告
    private static final int REPORTDATA = 1;
    //tf 短信内容的传输编码
    private static final int TRANSFERENCODING = 3;

    private static class HttpUtilFactory {
        private final static HttpUtil HTTP_UTIL = new HttpUtil();
    }

    public static HttpUtil getInstance() {
        return HttpUtilFactory.HTTP_UTIL;
    }

    public String doPost(String url, String params)
            throws IOException {
        log.info("请求参数:" + params);
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(params, "utf-8");
        httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");

        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
            String resultStr = reader.readLine();
            log.info("resultStr=" + resultStr);
            while (null != resultStr) {
                result = resultStr;
                resultStr = reader.readLine();
            }
        } finally {
            ((CloseableHttpClient) httpClient).close();
        }
        return result;
    }


    public static String doPost(String url, Map<String, String> params) {
        String result = "";
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<>();
        for (String temp : params.keySet()) {
            formparams.add(new BasicNameValuePair(temp, params.get(temp)));
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);

            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "utf-8");
                }
            }
        } catch (Exception e) {
            log.error("HttpUtil dopostMap error: {}", e);
        }
        return result;
    }


    public static String getHttpMess(String surl, String inputParam, String requestMethod, String charset) {
        StringBuilder sbReturn = new StringBuilder();
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(surl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.connect();
            OutputStream output = connection.getOutputStream();
            OutputStreamWriter osWriter = new OutputStreamWriter(output);
            osWriter.write(inputParam.toCharArray(), 0, inputParam.length());
            osWriter.flush();
            osWriter.close();
            int statusCode = connection.getResponseCode();
            /* 4 判断访问的状态码 */
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream in;
            in = connection.getInputStream();
            BufferedReader data = new BufferedReader(
                    new InputStreamReader(in, charset));
            String tempbf;
            while ((tempbf = data.readLine()) != null) {
                sbReturn.append(tempbf);
                sbReturn.append("\r\n");
            }
            data.close();
            in.close();
        } catch (Exception e) {
            log.error("HttpUtil getHttpMess error: {}", e);
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                log.error("HttpUtil getHttpMess connection.disconnect error: {}", e);
                connection.disconnect();
            }
        }
        return sbReturn.toString();
    }
    /**
     * 天畅云 短信发送
     * @param url url
     * @param paramMap map
     * @return str
     */
    public static String sendPost(String url, Map<String, Object> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            paramMap.put("dc", DATACODING);
            paramMap.put("rf", REPSPONSEFORMAT);
            paramMap.put("rd", REPORTDATA);
            paramMap.put("tf", TRANSFERENCODING);
            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    // key
                    String key = ite.next();
                    Object value = paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
                System.out.println(param);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error("sendPost error:{}",e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
