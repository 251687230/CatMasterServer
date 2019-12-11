package com.zous.catmaster.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * bean util
 */
public class BeanUtils {
    static Logger log = LoggerFactory.getLogger(BeanUtils.class);

    static private Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    static public Map<String ,Object> toMap(String key ,Object obj){
        Map<String ,Object> empty = new HashMap<String, Object>();
        empty.put(key, obj);
        return empty;
    }



    /**
     * 将给定的对象转换为json字符串，转换失败返回null
     * @param obj
     * @return
     */
    static public String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.warn("---- > switch object to json string is failed ,and that object is : " + obj.toString() ,e);
        }
        return  null;
    }

    static public JsonObject toJosnObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static byte[] toJsonAsBytes(Object obj) {
        String json = toJson(obj);
        return Objects.isNull(json) ? new byte[0] : json.getBytes();
    }

    public static JsonArray toJosnArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }

    /**
     *	 从给定的json字符中，遍历查找第一个给定字段的值
     * @param json
     * @param field
     * @return
     */
    static public String deepExtractData(String json ,String field) {
        JsonObject jo = toJosnObject(json);
        Set<String> keys = jo.keySet();
        if(keys.contains(field)) {
            String result = jo.get(field).toString();
            //除去前后字符 引号；
            return  result.startsWith("\"") && result.endsWith("\"") ? result.substring(1,result.length() -1) : result;
        }
        for(String key : keys) {
            try {
                String s = deepExtractData(jo.getAsJsonObject(key).toString(), field);
                if(s != null) {
                    return s;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 	将给定的json字符串转换为给定的class对象，失败返回null
     * @param json
     * @param cls
     * @return
     */
    static public <T> T toBean(String json ,Class<? extends T> cls) {
        try {
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            log.warn("---- > switch json string to bean is failed ,and that object class is : " + cls + " ,and that json string is : " + json,e);
        }
        return null;
    }

    static public <T> T toBean(byte[] json ,Class<? extends T> cls) {
        return toBean(new String(json), cls);
    }

    static public <T> T toBean(byte[] json ,Charset charset ,Class<? extends T> cls) {
        try {
            return toBean(new String(json ,charset.name()), cls);
        } catch (UnsupportedEncodingException e) {
            log.warn("---- > switch json string to bean is failed ,and that object class is : " + cls + " ,and that json string is : " + json
                    + " ;cause is is no given character : " + charset,e);
            return null;
        }
    }


    private static Field[] getDeclaredFields(Object original) {
        return original.getClass().getDeclaredFields();
    }




}
