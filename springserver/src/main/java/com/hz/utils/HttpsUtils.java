package com.hz.utils;

import com.hz.demo.entity.HttpResult;
import com.hz.demo.entity.Server;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 调用第三方接口的方法
 */
@Component
public class HttpsUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpsUtils.class);

    static RequestConfig proConfig;
    static HttpHost httpProxy;
    static HttpHost targetHost;
    static HttpClientContext context;
    static PoolingHttpClientConnectionManager cm;
    static {
        init();
    }
    public static void init() {
        LayeredConnectionSocketFactory layeredConnectionSocketFactory;
        try {
            layeredConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("https", layeredConnectionSocketFactory)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(200);// 线程池最大数量
            cm.setDefaultMaxPerRoute(30); // 相同路由请求的最大连接数量
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
        }
    }
    static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        /**
         * 获取方法（必须重载）
         * @return
         */
        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }
    }

    //代理模式
    public HttpResult doGet(Server server, String url, Map<String, Object> param) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doGet(closeableHttpClient,url, param);
    }

    public static HttpResult doGet(CloseableHttpClient httpClient,String url, Map<String, Object> param) {
        HttpResult httpResult = new HttpResult();
        long startTime = System.currentTimeMillis();
        // 创建Httpclient对象
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URLEncoder.encode(url,"utf-8");
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key).toString());
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            if (proConfig!=null){
                httpGet.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpGet,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000).setConnectionRequestTimeout(6000)
                        .build();
                httpGet.setConfig(config);
                response = httpClient.execute(httpGet);
            }
            // 判断返回状态是否为200
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        logger.info("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }

    //代理模式
    public  HttpResult doPost(Server server, String url, Map<String, Object> param) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doPost(closeableHttpClient,url,param);
    }

    public  HttpResult doPostWithHeader(Server server, String url, Map<String, Object> param, String header){
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doPostWithHeader(closeableHttpClient,url,param,header);
    }

    public  HttpResult doPostWithHeader(CloseableHttpClient httpClient,String url, Map<String, Object> param,String header){
        long startTime = System.currentTimeMillis();
        // 创建Httpclient对象
        CloseableHttpResponse response = null;
        HttpResult httpResult = new HttpResult();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Foxit-App-Name",header);

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            if (proConfig!=null){
                httpPost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpPost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpPost.setConfig(config);
                response = httpClient.execute(httpPost);
            }

            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);

        return httpResult;
    }
    public  HttpResult doPost(CloseableHttpClient httpClient,String url, Map<String, Object> param) {

        long startTime = System.currentTimeMillis();

        // 创建Httpclient对象
        CloseableHttpResponse response = null;
        HttpResult httpResult = new HttpResult();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            if (proConfig!=null){
                httpPost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpPost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(7000)
                        .setSocketTimeout(10000)
                        .build();
                httpPost.setConfig(config);
                response = httpClient.execute(httpPost);
            }
            //response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }

    //代理模式
    public  HttpResult doPostJSON(Server server, String url, String jsonString) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doPostJSON(closeableHttpClient,url, jsonString);
    }
    public  HttpResult doPostJSON(CloseableHttpClient httpClient,String url, String jsonString){

        long startTime = System.currentTimeMillis();

        HttpPost httpPost = new HttpPost(url);
        HttpResponse response;

        HttpResult httpResult = new HttpResult();
        //解决中文乱码问题
        StringEntity entity = new StringEntity(jsonString,"utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try {
            if (proConfig!=null){
                httpPost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpPost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setSocketTimeout(10000)
                        .build();
                httpPost.setConfig(config);
                response = httpClient.execute(httpPost);
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }

    //代理模式
    public  HttpResult doPutJSON(Server server, String url, String jsonString) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doPutJSON(closeableHttpClient,url, jsonString);
    }
    public  HttpResult doPutJSON(CloseableHttpClient httpClient,String url, String jsonParam){

        long startTime = System.currentTimeMillis();

        HttpPut httpPut = new HttpPut(url);
        //解决中文乱码问题
        StringEntity entity = new StringEntity(jsonParam,"utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
        HttpResponse response;
        HttpResult httpResult = new HttpResult();
        try {
            if (proConfig != null) {
                httpPut.setConfig(proConfig);
                response = httpClient.execute(targetHost, httpPut, context);
            } else {
                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpPut.setConfig(config);
                response = httpClient.execute(httpPut);
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }
    //代理模式
    public  HttpResult doPut(Server server, String url, Map<String, Object> param) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doPut(closeableHttpClient,url,param);
    }

    public  HttpResult doPut(CloseableHttpClient httpClient,String url, Map<String, Object> param) {

        long startTime = System.currentTimeMillis();

        CloseableHttpResponse response = null;
        HttpResult httpResult = new HttpResult();
        try {
            // 创建Http Post请求
            HttpPut httpPut = new HttpPut(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpPut.setEntity(entity);
            }
            // 执行http请求
            if (proConfig!=null){
                httpPut.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpPut,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpPut.setConfig(config);
                response = httpClient.execute(httpPut);
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }

        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);

        return httpResult;
    }

    //代理模式
    public  HttpResult doDelete(Server server, String url, Map<String, Object> param) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return doDelete(closeableHttpClient,url,param);
    }

    public  HttpResult doDelete( CloseableHttpClient httpClient,String url, Map<String, Object> param) {

        long startTime = System.currentTimeMillis();

        HttpDeleteWithBody httpDelete;
        CloseableHttpResponse response;
        HttpResult httpResult = new HttpResult();
        try {
            httpDelete = new HttpDeleteWithBody(url);
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpDelete.setEntity(entity);
            }
            if (proConfig!=null){
                httpDelete.setConfig(proConfig);
                response= httpClient.execute(targetHost,httpDelete,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpDelete.setConfig(config);
                response = httpClient.execute(httpDelete);
            }

            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }

        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;

    }

    //代理模式
    public  HttpResult postFiles(Server server, String filePostUrl,
                                   Map<String, Object> params,
                                   Map<String, byte[]> filesMap, String fileName) {
        CloseableHttpClient closeableHttpClient = setProxy(server);
        return postFiles(closeableHttpClient,filePostUrl,params,filesMap,fileName);
    }
    public  HttpResult postFiles(CloseableHttpClient httpClient,String filePostUrl,
                                   Map<String, Object> params,
                                   Map<String, byte[]> filesMap,String fileName) {

        HttpResult httpResult = new HttpResult();
        CloseableHttpResponse response=null;
        HttpPost httppost = new HttpPost(filePostUrl);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        // 是否多个文件
        if (filesMap != null) {
            for (String key : filesMap.keySet()) {
                entityBuilder.addBinaryBody(fileName, filesMap.get(key),
                        ContentType.DEFAULT_BINARY, key);
            }
        }

        // 是否有表单参数
        if (params != null) {
            for (String key : params.keySet()) {
                entityBuilder.addTextBody(key, params.get(key).toString(),ContentType.TEXT_PLAIN.withCharset("UTF-8"));
            }
        }

        HttpEntity httpEntity = entityBuilder.build();

        httppost.setEntity(httpEntity);
        try {
            if (proConfig!=null){
                httppost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httppost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(7000)
                        .build();
                httppost.setConfig(config);
                response = httpClient.execute(httppost);
            }

            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return httpResult;
    }


    //代理模式上传
    public  HttpResult upload(Server server, String filePostUrl,
                                Map<String, String> params,
                                Map<String, File> filesMap, String fileName) {
        HttpResult httpResult = new HttpResult();
        CloseableHttpClient httpClient = setProxy(server);
        try {
            HttpResponse response;
            HttpPost httppost = new HttpPost(filePostUrl);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            // 是否多个文件
            if (filesMap != null) {
                for (String key : filesMap.keySet()) {
                    entityBuilder.addBinaryBody(fileName, filesMap.get(key),
                            ContentType.DEFAULT_BINARY, key);
                }
            }
            // 是否有表单参数
            if (params != null) {
                for (String key : params.keySet()) {
                    entityBuilder.addTextBody(key, params.get(key),ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                }
            }
            httppost.setEntity(entityBuilder.build());
            if (proConfig!=null){
                httppost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httppost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httppost.setConfig(config);
                response = httpClient.execute(httppost);
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        return httpResult;
    }

    public  HttpResult doPostHead(Server server, String url, Map<String, Object> param) {
        String result;
        HttpResult httpResult = new HttpResult();
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = setProxy(server);
        // 创建Httpclient对象
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //String resultString;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpPost.setEntity(entity);
            }
            if (proConfig!=null){
                httpPost.setConfig(proConfig);
                response = httpClient.execute(targetHost,httpPost,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpPost.setConfig(config);
                response = httpClient.execute(httpPost);
            }
            // 执行http请求
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            Header[] headers = response.getHeaders("X-Pagination-Page-Count");
            for (Header header : headers) {
                result = header.getValue();
            }
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setResult(result);
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }

    public  HttpResult doDeleteJSON(Server server, String url, String jsonParam) {
        HttpResult httpResult = new HttpResult();
        long startTime = System.currentTimeMillis();

        CloseableHttpClient httpClient = setProxy(server);
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");
        //解决中文乱码问题
        StringEntity entity = new StringEntity(jsonParam,"utf-8");
        httpDelete.setEntity(entity);
        CloseableHttpResponse httpResponse = null;
        try {
            if (proConfig!=null){
                httpDelete.setConfig(proConfig);
                httpResponse = httpClient.execute(targetHost,httpDelete,context);
            }else {
                RequestConfig config=RequestConfig.custom()
                        .setConnectTimeout(6000)
                        .setSocketTimeout(6000)
                        .build();
                httpDelete.setConfig(config);
                httpResponse = httpClient.execute(httpDelete);
            }
            httpResult.setCode(httpResponse.getStatusLine().getStatusCode());
            httpResult.setResult(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
        } catch (Exception e) {
            httpResult.setCode(999999);
            httpResult.setResult(e.toString());
            logger.error(e.toString());
        }finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error(e.toString());
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }
        logger.debug("[耗时：" + (System.currentTimeMillis() - startTime) + "毫秒] " + url);
        return httpResult;
    }

    public  CloseableHttpClient setProxy(Server server){
        //Log.debug(server.getProxy());
        HttpRequestRetryHandler handler = (arg0, retryTimes, arg2) -> {
            if (retryTimes > 2) {
                return false;
            }
            /*if (arg0 instanceof UnknownHostException || arg0 instanceof ConnectTimeoutException
                    || !(arg0 instanceof SSLException) || arg0 instanceof NoHttpResponseException) {
                return true;
            }*/

            HttpClientContext clientContext = HttpClientContext.adapt(arg2);
            HttpRequest request = clientContext.getRequest();
            // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
            return !(request instanceof HttpEntityEnclosingRequest);
        };

        CloseableHttpClient httpClient;
        if (server.getPort()!=null && !"".equals(server.getPort())){
            String serverServer = server.getServer();
            String address = serverServer.substring(8);
            String https = serverServer.substring(0,5);
            targetHost =new HttpHost(address,443,https);
            httpProxy = new HttpHost(server.getProxy(),Integer.parseInt(server.getPort())) ;
            proConfig = RequestConfig.custom().setProxy(httpProxy).setConnectTimeout(6000)
                    .setSocketTimeout(6000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(proConfig).setConnectionManagerShared(true).setConnectionManager(cm).setRetryHandler(handler).build();
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(server.getProxy(), Integer.parseInt(server.getPort())),
                    new UsernamePasswordCredentials(server.getProxyUser(), server.getProxyPass()));

            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(httpProxy, basicAuth);

            context = HttpClientContext.create();
            context.setCredentialsProvider(credentialsProvider);
            context.setAuthCache(authCache);
        }else {
            //proConfig = RequestConfig.custom().setProxy(httpProxy).build();
            httpClient = HttpClientBuilder.create().setConnectionManagerShared(true).setConnectionManager(cm).setRetryHandler(handler).build();
            //httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    public  boolean doPing(String proxy,String port,String user,String pass){
        int statusCode;
        String address = "online.cpdf360.cn";
        String https = "https";

        targetHost =new HttpHost(address,443,https);
        try {
            httpProxy = new HttpHost(proxy,Integer.parseInt(port)) ;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        RequestConfig proConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).setProxy(httpProxy).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(proConfig).build();
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(proxy, Integer.parseInt(port)),
                new UsernamePasswordCredentials(user, pass));
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(httpProxy, basicAuth);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credentialsProvider);
        context.setAuthCache(authCache);
        try {
            HttpGet httpGet = new HttpGet("https://online.cpdf360.cn");
            CloseableHttpResponse httpResp = httpClient.execute(targetHost,httpGet,context);
            statusCode = httpResp.getStatusLine().getStatusCode();
            return statusCode == HttpStatus.SC_OK;

        } catch (IOException e) {
            return false;
        }
    }

}


