package vicky.util.resttemplate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@ConfigurationProperties(prefix = "com.cfcc.distribute.restpool")
public class RestClientProperties {


      private boolean enabled=true;
     private int poolingHttpClientConnectionManager=30;
      private int maxTotal=100;
      private int defaultMaxPerRout=50;
     private int retryHandler=3;
      private int connectTimeout=5000;
     private int readTimeout=5000;
    private int connectionRequestTimeout=200;
     private boolean buggerRequestBody=false;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPoolingHttpClientConnectionManager() {
        return poolingHttpClientConnectionManager;
    }

    public void setPoolingHttpClientConnectionManager(int poolingHttpClientConnectionManager) {
        this.poolingHttpClientConnectionManager = poolingHttpClientConnectionManager;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRout() {
        return defaultMaxPerRout;
    }

    public void setDefaultMaxPerRout(int defaultMaxPerRout) {
        this.defaultMaxPerRout = defaultMaxPerRout;
    }

    public int getRetryHandler() {
        return retryHandler;
    }

    public void setRetryHandler(int retryHandler) {
        this.retryHandler = retryHandler;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public boolean isBuggerRequestBody() {
        return buggerRequestBody;
    }

    public void setBuggerRequestBody(boolean buggerRequestBody) {
        this.buggerRequestBody = buggerRequestBody;
    }
}
