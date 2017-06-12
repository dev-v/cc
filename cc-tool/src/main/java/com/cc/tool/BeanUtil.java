package com.cc.tool;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author wenlongchen
 * @since Nov 8, 2016
 */
public class BeanUtil {

  /**
   * <pre>
   * 根据字段名称将对象source转换为目标对象targetClass
   * 不支持枚举和基础类型
   * </pre>
   * 
   * @param source
   * @param targetClass
   * @return
   */
  public static final <T> T o2o(Object source, Class<T> targetClass) {
    return JSON.toJavaObject((JSON) JSON.toJSON(source), targetClass);
  }

  public static final <T> List<T> o2o(Collection<?> source, Class<T> targetClass) {
    return JSONArray.parseArray(JSON.toJSONString(source), targetClass);
  }

  /**
   * <pre>
   * 根据字段名称将source中的属性值组合后设置到targetClass中
   * source中属性若有重复的 则后面的覆盖前面的
   * 若为集合 则将所有集合中的集合元素添加到一起返回
   * 不支持枚举和基础类型
   * </pre>
   * 
   * @param targetClass
   * @param source
   */
  public static final <T> T wrap(Class<T> targetClass, Object... source) {
    if (source == null) {
      return null;
    }
    JSON json = (JSON) JSON.toJSON(source[0]);
    if (json instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) json;
      JSONObject temp;
      for (int i = 1, len = source.length; i < len; i++) {
        temp = (JSONObject) JSON.toJSON(source[i]);
        if (temp != null)
          jsonObject.putAll(temp);
      }
      return jsonObject.toJavaObject(targetClass);
    } else if (json instanceof JSONArray) {
      JSONArray jsonArray = (JSONArray) json;
      JSONArray temp;
      for (int i = 1, len = source.length; i < len; i++) {
        temp = (JSONArray) JSON.toJSON(source[i]);
        if (temp != null)
          jsonArray.addAll(temp);
      }
      return jsonArray.toJavaObject(targetClass);
    } else {
      return json.toJavaObject(targetClass);
    }
  }

  /**
   * <pre>
   * 获取对象的属性值
   * 不支持枚举、基础类型、集合 参数为这些值时返回null
   * </pre>
   * 
   * @param obj
   * @param property
   * @return
   */
  public static <T> T getProperty(Object obj, String property, Class<T> clazz) {
    obj = JSON.toJSON(obj);
    if (obj instanceof JSONObject) {
      return ((JSONObject) obj).getObject(property, clazz);
    }
    return null;
  }

  /**
   * @param obj
   * @param property
   * @return
   * @see #getProperty(Object, String, Class)
   */
  public static String getProperty(Object obj, String property) {
    return getProperty(obj, property, String.class);
  }
}

