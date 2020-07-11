package com.roll.rpc.gather.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.roll.rpc.gather.common.RpcRequest;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roll
 * created on 2019-09-03 17:21
 */
public class SerializationUtil {
    private static Map<Class<?>, Schema<?>> cacheSchema = new HashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    /**
     * 序列化(对象->字节数组)
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchame(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchame(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private static <T> Schema<T> getSchame(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cacheSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cacheSchema.put(cls, schema);
        }
        return schema;
    }

    /**
     * JSON序列化(对象->字节数组)
     */
    public static <T> byte[] jsonSerialize(T obj) {
        return JSONObject.toJSONBytes(obj);
    }

    /**
     * JSON反序列化
     */
    public static <T> T jsonDeserialize(byte[] data, Class<T> cls) {
        return JSONObject.parseObject(data, cls);
    }

    public static void main(String[] args) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("interFaceName");
        rpcRequest.setMethodName("methodName");
        rpcRequest.setParameters(null);
        rpcRequest.setRequestId("requestId");
        rpcRequest.setServiceVersion("v1");
        long currentTime  = System.currentTimeMillis();
        for (int i = 0;i <1000000;i++){
            serialize(rpcRequest);
        }
        System.out.println(System.currentTimeMillis() - currentTime);

        long currentTime1  = System.currentTimeMillis();
        for (int i = 0;i <1000000;i++){
            jsonSerialize(rpcRequest);
        }
        System.out.println(System.currentTimeMillis() - currentTime1);
    }

}
