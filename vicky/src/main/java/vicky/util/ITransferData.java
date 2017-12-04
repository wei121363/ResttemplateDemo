package vicky.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;


/**
 * @author vicky
 * 数据转换类json转dto
 */
public interface ITransferData {


    public ObjectMapper getMapper()throws Exception;


    public default <T> T transferData(Object obj, Class<T> c) throws Exception {
        return null;
    };

    public static Map transferData(Object obj, Class keytype, Class vtype) throws Exception {
        return null;
    };
}
