package vicky.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TransferData implements ITransferData{

    private static TransferData data=new TransferData();
    private TransferData()
    {}
    public static TransferData getInstance()
    {
        return data;
    }


    public ObjectMapper getMapper()throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;

    }

    /**
     * @param obj 接收JSONEntity对象
     * @param c 要转换的dto的类型
     * @param <T> 返回list<dto> 或 dto的类型
     * @return 返回list<dto> 或 dto
     * @throws Exception 错误对象
     */
    public  <T> T transferData(JSONEntity obj, Class<T> c) throws Exception {
        Object data = obj.getData();

        ObjectMapper mapper = getMapper();
        if (data != null) {
            byte[] str = mapper.writeValueAsBytes(data);
            if (data instanceof List) {

                List<T> lists = mapper.readValue(str, new TypeReference<List<T>>() {
                    @Override
                    public Type getType() {
                        return new ParameterizedType() {
                            @Override
                            public Type[] getActualTypeArguments() {
                                return new Type[]{c};
                            }

                            @Override
                            public Type getRawType() {
                                return List.class;
                            }

                            @Override
                            public Type getOwnerType() {
                                return null;
                            }
                        };
                    }
                });
                return (T) lists;
            } else {
                T C = mapper.readValue(str, c);
                return C;
            }
        }
        return null;
    }

    /**
     * @param obj 接收JSONEntity对象
     * @param keytype 要转换的map的key的类型
     * @param vtype 要转换的map的value的类型（一般为dto对象啥的）
     * @return 返回map类型,例如map<Long,customdto>
     * @throws Exception  错误对象
     */
    public  Map transferData(JSONEntity obj, Class keytype, Class vtype) throws Exception {
        Object data = obj.getData();
        ObjectMapper mapper=getMapper();
        if (data != null) {
            byte[] str = mapper.writeValueAsBytes(data);
            if (data instanceof Map) {

                Map map = mapper.readValue(str, new TypeReference<Map>(){
                    @Override
                    public Type getType(){
                        return new ParameterizedType() {
                            @Override
                            public Type[] getActualTypeArguments() {
                                return new Type[]{
                                        keytype,vtype
                                };
                            }
                            @Override
                            public Type getRawType() {
                                return Map.class;
                            }

                            @Override
                            public Type getOwnerType() {
                                return null;
                            }
                        };
                    }

                });
                return map;

            }
        }

        return null;
    }

}
