package vicky;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import vicky.Data.Order;
import vicky.Data.ReceiveOrder;
import vicky.util.GZIP;
import vicky.util.JSONEntity;
import vicky.util.TransferData;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;


/**
 * 该类为示例的服务调用放，请求者
 */
@ConfigurationProperties(prefix = "server")
@RestController
@RequestMapping("/")
@Slf4j
public class RestCustom {


    private String host;
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    @Autowired
    RestTemplate rs;



    @RequestMapping("/order")
    public String getOrder()
    {
        try{

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host+"getOrder").build();
            URI uri = uriComponents.encode().toUri();

            JSONEntity obj= rs.getForObject(uri,JSONEntity.class);
            //处理单个dto
            ReceiveOrder obj1= TransferData.getInstance().transferData(obj,ReceiveOrder.class);
            return  TransferData.getInstance().getMapper().writeValueAsString(obj1);
        }catch(Exception e)
        {
            log.error("order:",e);
         }
        return null;

    }

    @RequestMapping("/orders")
    public String getOrders()
    {
        try{

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host+"getOrders").build();
            URI uri = uriComponents.encode().toUri();

            JSONEntity obj= rs.getForObject(uri,JSONEntity.class);
            //处理dto的集合

            List obj1=(List)TransferData.getInstance().transferData(obj,ReceiveOrder.class);
            return TransferData.getInstance().getMapper().writeValueAsString(obj1);
        }catch(Exception e)
        {
            log.error("orders:",e);


        }
        return null;

    }



    @RequestMapping("/orderstoCompress")
    public String getOrderstoCompress()
    {

        try{

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host+"getOrderstoCompress").build();
            URI uri = uriComponents.encode().toUri();

            String obj= rs.getForObject(uri,String.class);
            if(!"500".equalsIgnoreCase(obj))
            {
                //处理待压缩的dto的集合
                obj= GZIP.unCompress(obj);
                JSONEntity entity= TransferData.getInstance().getMapper().readValue(obj,JSONEntity.class);

                List<ReceiveOrder> obj1=(List)TransferData.getInstance().transferData(entity,ReceiveOrder.class);


                return  TransferData.getInstance().getMapper().writeValueAsString(obj1);
            }
        }catch(Exception e)
        {
            log.error("orderstoCompress:",e);
        }
        return null;

    }


    @RequestMapping("/ordersForMap")
    public String getOrdersForMap()
    {
        try{

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host+"getOrdersForMap").build();
            URI uri = uriComponents.encode().toUri();
            JSONEntity obj= rs.getForObject(uri,JSONEntity.class);
            //处理map集合存放dto
            Map obj1=(Map)TransferData.getInstance().transferData(obj,Long.class,ReceiveOrder.class);
            return  TransferData.getInstance().getMapper().writeValueAsString(obj1);

        }catch(Exception e)
        {
            log.error("ordersForMap:",e);
        }
        return null;

    }
    @RequestMapping(value="/add")
    public String add() throws JsonProcessingException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Referer", "http://localhost:9091/");
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);


            Order order = new Order();
            order.setId(3L);
            order.setStatus(true);
            order.setDetail("手机");
            order.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order.setCreateDay(new Date(System.currentTimeMillis()));
            order.setNum(new Integer(100));
            order.setMat(new BigDecimal(1.1));


            //HttpEntity entity=new HttpEntity(this.getMapper().writeValueAsString(order),headers);
            HttpEntity entity = new HttpEntity(order, headers);


            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host + "addOrder").build();
            URI uri = uriComponents.encode().toUri();

            String status = rs.postForObject(uri, entity, String.class);
            return status;
        }catch(Exception e)
        {
            log.error("add:",e);
        }
        return null;
    }



    @RequestMapping(value="/orderById/{id}")
    public String getOrderById(@PathVariable Long id) {

        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host + "getOrderById/{id}").build();
            URI uri = uriComponents.expand(id).encode().toUri();

            //另一种写法
//        UriComponentsBuilder.newInstance().scheme("http")
//                .host("9091").path("getOrderById/{id}").build().expand("3").encode();


            JSONEntity entity = rs.getForObject(uri, JSONEntity.class);
            ReceiveOrder obj1 = TransferData.getInstance().transferData(entity, ReceiveOrder.class);
            return TransferData.getInstance().getMapper().writeValueAsString(obj1);
        }catch (Exception e)
        {
            log.error("orderByIdB:",e);
        }
        return null;

    }


    @RequestMapping(value="/upload")
    public String fileupload() {

//        Setting this property to false will enable streaming mode.
//        This will result in a ClientHttpRequest that either streams directly to the underlying HttpURLConnection
//        (if the Content-Length is known in advance), or that will use "Chunked transfer encoding"
//        (if the Content-Length is not known in advance). That is:
        //大文件时需要设置以下属性


        // SimpleClientHttpRequestFactory fa= (SimpleClientHttpRequestFactory)rs.getRequestFactory();
        //fa.setBufferRequestBody(false);
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host + "uploadFile").build();
            URI uri = uriComponents.encode().toUri();

            String filePath = "/Users/vicky/workspace/idea/vicky/yy.txt";
            File file = new File(filePath);

            FileSystemResource resource = new FileSystemResource(file);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("jarFile", resource);
            param.add("aa", "yy.txt");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Referer", "http://localhost:9091/");
            MediaType type = MediaType.parseMediaType("multipart/form-data; charset=UTF-8");
            headers.setContentType(type);

            HttpEntity entity = new HttpEntity(param, headers);

            String result = rs.postForObject(uri, entity, String.class);

            //恢复属性
            //  fa.setBufferRequestBody(true);
            return result;
        }catch(Exception e)
        {
            log.error("upload:",e);
        }
           return null;
    }

    @RequestMapping(value="/download")
    public String download() {
        try {

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                    host + "downloadFile").build();
            URI uri = uriComponents.encode().toUri();
            byte[] result = rs.getForObject(uri, byte[].class);
            String str = new String(result, "UTF-8");


            return str;
        }catch(Exception e)
        {
            log.error("download:",e);
        }
        return null;

    }

    /**
     * 该方法让浏览器直接下载文件
     * @param req
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value="/downloadToBrowser")
    public void downloadToBrowser(HttpServletRequest req,HttpServletResponse resp) {
        RandomAccessFile raf=null;
        ServletOutputStream out=null;
        try {
            String filePath = "/Users/vicky/workspace/idea/vicky/yy1.txt";
            File file = new File(filePath);
            raf = new RandomAccessFile(filePath, "r");
            out = resp.getOutputStream();
            int buf_size = 8192;
            long pos = 0;
            long end = 0;
            long fLength = file.length();
            if (req.getHeader("Range") != null) {
                resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String range = req.getHeader("Range").replaceAll("bytes=", "");
                pos = Long.valueOf(range.split("-")[0]);
                end = Long.valueOf(range.split("-")[1]);
            } else {
                end = fLength - 1;
            }
            long length = end - pos + 1;

            resp.setHeader("pragma", "no-cache");
            resp.setHeader("cache-control", "private");
            resp.setDateHeader("Expires", 0);
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-Length", String.valueOf(length));
            //resp.setContentType("application/x-msdownload");
            //resp.setContentType("text/x-msdownload");
            resp.setContentType("application/octet-stream;charset="
                    + "UTF-8");// 解决在弹出文件下载框不能打开文件的问题

            resp.addHeader("Content-Disposition", "attachment; filename= " + file.getName());// 解决在弹出文件下载框不能打开文件的问题


            byte[] buf = new byte[buf_size];
            byte[] buf2 = null;
            int n = 0;
            int i = 0;
            raf.seek(pos);
            int p = (int) (length / buf_size);
            int b_size = (int) (length % buf_size);
            if (b_size > 0)
                p++;
            while (i < p) {
                i++;
                if (i == p && b_size > 0) {
                    buf2 = new byte[b_size];
                    n = raf.read(buf2, 0, b_size);
                    out.write(buf2);

                } else {
                    n = raf.read(buf, 0, buf_size);
                    out.write(buf);
                }
                pos += n;
                raf.seek(pos);


            }
            out.flush();

        }catch(Throwable ex)
        {
            //错误处理

        }finally {

            try {
                out.close();
                raf.close();

            } catch (IOException e) {
                log.error("downloadToBrowser",e);

            }

        }


    }


}

