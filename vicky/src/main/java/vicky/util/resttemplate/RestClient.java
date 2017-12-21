package vicky.util.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(RestClientProperties.class)
@Slf4j
@EnableAutoConfiguration
public class RestClient {

    @Autowired
    RestClientProperties proerty;

    @Bean
    @LoadBalanced
    @Scope("prototype")
    @Lazy
    RestTemplate build() throws Exception{

        RestTemplate restTemplate;

       PoolingHttpClientConnectionManager poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager(proerty.getPoolingHttpClientConnectionManager(), TimeUnit.SECONDS);
       poolingHttpClientConnectionManager.setMaxTotal(proerty.getMaxTotal());
       poolingHttpClientConnectionManager.setDefaultMaxPerRoute(proerty.getDefaultMaxPerRout());



        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= proerty.getRetryHandler()) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };



        HttpClientBuilder httpClientBuilder=HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);

        List headers=new ArrayList();

        headers.add(new BasicHeader("Accept-Encoding","gzip,deflate"));
        headers.add(new BasicHeader("Accecp-Language","zh-CN,zh;q=0.8,en;q=0.6"));
        headers.add(new BasicHeader("Connection","keep-alive"));

        httpClientBuilder.setDefaultHeaders(headers);
        HttpClient httpClient=httpClientBuilder.build();


        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory=new HttpComponentsClientHttpRequestFactory(httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(proerty.getConnectTimeout());
        httpComponentsClientHttpRequestFactory.setReadTimeout(proerty.getReadTimeout());
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(proerty.getConnectionRequestTimeout());
        httpComponentsClientHttpRequestFactory.setBufferRequestBody(proerty.isBufferRequestBody());
        List<HttpMessageConverter<?>> messageConverters=new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        restTemplate=new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
         restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());


     return restTemplate;
    }




    @Bean(name="asyncRestTemplate")
    @LoadBalanced
    AsyncRestTemplate buildAsyn() throws Exception{

        AsyncRestTemplate template = new AsyncRestTemplate();
        List<HttpMessageConverter<?>> messageConverters=new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        template.setMessageConverters(messageConverters);



        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(proerty.getMaxTotal());
        cm.setDefaultMaxPerRoute(proerty.getDefaultMaxPerRout());

        List headers=new ArrayList();
        headers.add(new BasicHeader("Accept-Encoding","gzip,deflate"));
        headers.add(new BasicHeader("Accecp-Language","zh-CN,zh;q=0.8,en;q=0.6"));
        headers.add(new BasicHeader("Connection","keep-alive"));

        CloseableHttpAsyncClient httpAsyncClients = HttpAsyncClients.custom()
                .setConnectionManager(cm)
                .setDefaultHeaders(headers)
                .build();

        HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory
                =new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClients);
        httpComponentsAsyncClientHttpRequestFactory.setConnectTimeout(proerty.getConnectTimeout());
        httpComponentsAsyncClientHttpRequestFactory.setReadTimeout(proerty.getReadTimeout());
        httpComponentsAsyncClientHttpRequestFactory.setConnectionRequestTimeout(proerty.getConnectionRequestTimeout());
        httpComponentsAsyncClientHttpRequestFactory.setBufferRequestBody(proerty.isBufferRequestBody());



        template.setAsyncRequestFactory(httpComponentsAsyncClientHttpRequestFactory);
        return template;
    }

    }
