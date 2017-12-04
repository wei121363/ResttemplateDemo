package vicky.Data;

import vicky.util.JSONEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreasteData {



    public static Map values=new HashMap();

    public static Order createOrder()
    {

        if(values.get("order")==null)

        { Order   order = new Order();
            order.setId(1L);
            order.setStatus(true);
            order.setDetail("电脑");
            order.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order.setCreateDay(new Date(System.currentTimeMillis()));
            order.setNum(new Integer(100));
            order.setMat(new BigDecimal(1.1));

            //子类
            Order   order1= new Order();
            order1.setId(2L);
            order1.setStatus(false);
            order1.setDetail("苹果电脑");
            order1.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order1.setCreateDay(new Date(System.currentTimeMillis()));
            order1.setNum(new Integer(200));
            order1.setMat(new BigDecimal(2.2));
            order.setChild(order1);
            //子类集合
            People people=new People();
            people.setAge(22);
            people.setName("Lucy");
            people.setSex(1);

            People people1=new People();
            people.setAge(32);
            people.setName("Tom");
            people.setSex(2);

            List peoples=new ArrayList();
            peoples.add(people);
            peoples.add(people1);
            order.setChilds(peoples);
            return order;}else
        {
            Order order=(Order)values.get("order");
            return order;
        }


    }
    public static List createOrders()
    {
        if(values.get("orders")==null) {
            Order order = new Order();
            order.setId(1L);
            order.setStatus(true);
            order.setDetail("电脑电脑电脑电脑电      脑电脑电脑电脑电脑电脑电脑电脑电脑");
            order.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order.setCreateDay(new Date(System.currentTimeMillis()));
            order.setNum(new Integer(100));
            order.setMat(new BigDecimal(1.1));


            Order order1 = new Order();
            order1.setId(2L);
            order1.setStatus(false);
            order1.setDetail("苹果电脑苹果电脑苹果电脑苹果电脑苹果电脑苹果电脑苹果电脑苹果电脑");
            order1.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order1.setCreateDay(new Date(System.currentTimeMillis()));
            order1.setNum(new Integer(200));
            order1.setMat(new BigDecimal(2.2));
            order.setChild(order1);


            List orders = new ArrayList();
            orders.add(order);
            orders.add(order1);
            return orders;

        }else
        {
            List orders=(List) values.get("orders");
            return orders;
        }

    }


    public static Map createOrdersForMap()
    {

        if(values.get("orderForMap")==null) {

            Order order = new Order();
            order.setId(1L);
            order.setStatus(true);
            order.setDetail("电脑");
            order.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order.setCreateDay(new Date(System.currentTimeMillis()));
            order.setNum(new Integer(100));
            order.setMat(new BigDecimal(1.1));


            Order order1 = new Order();
            order1.setId(2L);
            order1.setStatus(false);
            order1.setDetail("苹果电脑");
            order1.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            order1.setCreateDay(new Date(System.currentTimeMillis()));
            order1.setNum(new Integer(200));
            order1.setMat(new BigDecimal(2.2));
            order.setChild(order1);


            Map orders = new HashMap();

            orders.put(order.getId(), order);
            orders.put(order1.getId(), order1);
             return orders;
        }else
        {
            Map orders=(Map) values.get("orderForMap");
            return orders;
        }

    }

    public static Map createOrdersForMap1()
    {
     List list=new ArrayList();

        Order order = new Order();
        order.setId(1L);
        order.setStatus(true);
        order.setDetail("电脑");
        order.setUpdatetime(new Timestamp(System.currentTimeMillis()));
        order.setCreateDay(new Date(System.currentTimeMillis()));
        order.setNum(new Integer(100));
        order.setMat(new BigDecimal(1.1));
        list.add(order);

        Order order1 = new Order();
        order1.setId(2L);
        order1.setStatus(false);
        order1.setDetail("苹果电脑");
        order1.setUpdatetime(new Timestamp(System.currentTimeMillis()));
        order1.setCreateDay(new Date(System.currentTimeMillis()));
        order1.setNum(new Integer(200));
        order1.setMat(new BigDecimal(2.2));
        order.setChild(order1);
        list.add(order1);

        Map orders = new HashMap();

        orders.put(order.getId(), list);
        orders.put(2L, "abcdefg");

        return orders;
    }


}
