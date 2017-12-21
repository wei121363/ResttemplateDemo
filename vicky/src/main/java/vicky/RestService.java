package vicky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vicky.Data.CreasteData;
import vicky.Data.Order;
import vicky.Data.People;
import vicky.util.GZIP;
import vicky.util.JSONEntity;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * 该类为服务的提供方，提供者
 * @author vicky
 */
@RestController
@RequestMapping("/")
@Slf4j
public class RestService {

    /**
     * 提供字符串类型的方法，get方式获取
     * @return 返回字符串类型
     */
    @RequestMapping("/getStr")
    public String getStr()
    {
        return "vicky";
    }
    /**
     *返回单个dto 。get方式获取.
     * @return 返回JSONEntiy对象
     */
    @RequestMapping("/getOrder")
    public JSONEntity getOrder()
    {
        JSONEntity entity = new JSONEntity();
        try {
            Order order = CreasteData.createOrder();
            entity.setStatus(200);
            entity.setData(order);

     }catch(Exception e)
     {
         entity.setStatus(500);
         entity.setErrors("业务出错");
         log.error("getOrder",e);
     }
     System.out.println("服务端方法已经调用＝＝＝＝＝＝＝＝＝＝＝＝＝");
          return entity;
    }


    /**根据参数id返回对应的dto，get方式获取
     * @param id 接收请求中的参数key
     * @return 返回包装好的JSONEntity
     */
    @RequestMapping("/getOrderById/{id}")
    public JSONEntity getOrderById(@PathVariable Long id)
    {
        JSONEntity entity = new JSONEntity();

        try {
            List<Order> orders = CreasteData.createOrders();

            for (Order obj :orders)
            {
                if(obj.getId().equals(id))
                {
                    entity.setData(obj);
                    break;
                }
            }

            entity.setStatus(200);

        }catch(Exception e)
        {
            entity.setStatus(500);
            entity.setErrors("业务出错");


        }
        return entity;
    }


    /**
     * 返回list<dto>集合，get方式获取
     * @return 返回包装好的JSONEntity
     *
     */
    @RequestMapping("/getOrders")
    public JSONEntity getOrders()
    {
        JSONEntity entity = new JSONEntity();

        try {
            List order = CreasteData.createOrders();
            entity.setStatus(200);
            entity.setData(order);

        }catch(Exception e)
        {
            entity.setStatus(500);
            entity.setErrors("业务出错");
            log.error("getOrders",e);

        }

        return entity;
    }

    /**
     * 返回带压缩的数据
     * @return 本方法返回String类型压缩数据
     */
    @RequestMapping("/getOrderstoCompress")
    public String getOrderstoCompress()
    {
        JSONEntity entity = new JSONEntity();

        try {
            List order = CreasteData.createOrders();
            entity.setStatus(200);
            entity.setData(order);
            String entitystr=  GZIP.compress(entity.toString());
            return entitystr;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("getOrderstoCompress",e);

        }
        return "500";

    }
    /**
     * 返回Map<long,dto>类型的集合
     * @return  返回包装好的JSONEntity
     */
    @RequestMapping("/getOrdersForMap")
    public JSONEntity getOrdersForMap()
    {
        JSONEntity entity = new JSONEntity();

        try {
            Map order = CreasteData.createOrdersForMap();
            entity.setStatus(200);
            entity.setData(order);

        }catch(Exception e)
        {
            entity.setStatus(500);
            entity.setErrors("业务出错");
            log.error("getOrdersForMap",e);

        }

        return entity;
    }


    /**
     *
     *传递复杂类型，map中key类型相同，value类型不同
     * @return
     */
    @RequestMapping("/getOrdersForMapContainsList")
    public JSONEntity getOrdersForMapContainsList()
    {

        JSONEntity entity = new JSONEntity();

        try {
            //createOrdersForMap1构造数据对象
            Map order = CreasteData.createOrdersForMap1();
            entity.setStatus(200);
            entity.setData(order);

        }catch(Exception e)
        {
            entity.setStatus(500);
            entity.setErrors("业务出错");
            log.error("getOrdersForMap",e);

        }

        return entity;

    }


    /**
     * 该方法目的是为了展示RequestMapping注解的属性，此外，展示@RequestBody直接接收提交的对象
     * @param order 请求提交的对象参数
     * @return 返回成功或失败
     */
    @RequestMapping(value="/addOrder",
            method =RequestMethod.POST, //接收post类型的请求（resttemplate.post.....）
            produces="application/json", //返回json类型的数据（converter其作用）
            headers="Referer=http://localhost:9091/" ) //接收请求头是此Referer值的请求
    public String add(@RequestBody Order order) {
        try {

            CreasteData.values.put("order", order);
            List list = CreasteData.createOrders();
            list.add(order);
            CreasteData.values.put("orders", list);
            CreasteData.values.put("orderForMap", CreasteData.createOrdersForMap());

            return "200";
        }catch(Exception e)
        {

            log.error("addOrder",e);
        }
        return "500";
    }


    /**
     * 接收文件类型的服务
     * @param file 文件对象
     * @param name 其他参数传递举例
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String upload( @RequestPart("jarFile") MultipartFile file,@RequestPart("aa") String name) {

        OutputStream fos=null;
        InputStream fis=null;
        try {
            // 下面是测试代码
            String str = new String(file.getBytes(), "UTF-8");
            //接到文件保存到另外一个文件中
            String filePath = "/Users/vicky/workspace/idea/vicky/yy1.txt";
            File ishave = new File(filePath);
            if (!ishave.exists()) {
                boolean flag = ishave.createNewFile();
                System.out.println("File created: " + flag);
            }
             fos = new FileOutputStream(filePath);
            byte b[] = new byte[1024];
            int read = 0;
             fis = file.getInputStream();

            read = fis.read(b);
            while (read != -1) {
                fos.write(b, 0, read);
                read = fis.read(b);
            }


            // TODO 处理文件内容...
            return "ok";
        }catch(Exception e)
        {
            log.error("uploadFile",e);
        }finally {

            try {
                fis.close();
                fos.close();

            } catch (IOException e) {
                 log.error("uploadFile->final:",e);
            }
        }
        return "error";
    }

    /**
     * 下载文件，适用场景不明确
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/downloadFile" ,method = RequestMethod.GET)
    public byte[] download()  {
        FileInputStream fis = null;
        byte[] byte1=null;
        try {
            String filePath = "/Users/vicky/workspace/idea/vicky/yy1.txt";
            File file = new File(filePath);

            int size = (int) file.length();
           byte1 = new byte[size];

             fis = new FileInputStream(filePath);
            fis.read(byte1, 0, size);
            fis.close();
        }catch (Exception e)
        {
            log.error("downloadFile",e);

        }finally {
            if(fis!=null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                     log.error("download->final:",e);

                }

            }
        }
        return byte1;

    }


    @RequestMapping("/fivetime")
    public JSONEntity tenTime(){
        JSONEntity entity=new JSONEntity();

        try {
            Thread.sleep(5000);
            entity.setData(CreasteData.createOrder());
            entity.setStatus(200);



        } catch (InterruptedException e) {
            entity.setStatus(500);
            e.printStackTrace();
        }
        return entity;
    }
}
