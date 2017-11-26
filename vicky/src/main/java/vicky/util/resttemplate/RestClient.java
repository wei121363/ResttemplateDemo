package vicky.util.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

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
    //@LoadBalanced
    RestTemplate build() throws Exception{

        RestTemplate restTemplate;

       PoolingHttpClientConnectionManager poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager(proerty.getPoolingHttpClientConnectionManager(), TimeUnit.SECONDS);
       poolingHttpClientConnectionManager.setMaxTotal(proerty.getMaxTotal());
       poolingHttpClientConnectionManager.setDefaultMaxPerRoute(proerty.getDefaultMaxPerRout());


        HttpClientBuilder httpClientBuilder=HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(proerty.getDefaultMaxPerRout(),true))
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

     //log.info("RestClient初始化环境");
     return restTemplate;
    }

}
