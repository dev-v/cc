package com.cc.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

/**
 * @author wenlongchen
 * @since Jun 23, 2016
 */
public class Util {

  private static final Logger logger = Logger.getLogger(Util.class);

  private static final ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal =
      new ThreadLocal<SimpleDateFormat>();

  public static final char SYMBOL_UNDERLINE = '_';
  public static final char SYMBOL_POINT = '.';
  public static final char SYMBOL_COMMA = ',';
  private static final Pattern isBlankPattern = Pattern.compile("^\\s*$");

  /**
   * <pre>
   * 1秒=1000毫秒(ms), 1毫秒=1／1000秒(s)； 
   * 1秒=1000000 微秒(μs), 1微秒=1／1000000秒(s)； 
   * 1秒=1000000000 纳秒(ns),1纳秒=1／1000000000秒(s)； 
   * 1秒=1000000000000皮秒 1皮秒==1/1000000000000秒。
   * </pre>
   */
  private static final long TIME_NANO_MILI = 1000 * 1000;

  /**
   * 纳秒转毫秒
   * 
   * @return
   */
  public static final long timeNano2Mili(long nanoSeconds) {
    return nanoSeconds / TIME_NANO_MILI;
  }

  /**
   * 毫秒转纳秒
   * 
   * @param miliSeconds
   * @return
   */
  public static final long timeMili2Nano(long miliSeconds) {
    return miliSeconds * TIME_NANO_MILI;
  }

  /**
   * <pre>
   * 验证某个字符串是否为null或空字符串
   * </pre>
   * 
   * @param str
   * @return
   */
  public static final boolean isBlank(String str) {
    return str == null || (isBlankPattern.matcher(str).find());
  }

  /**
   * <pre>
   * 将下划线、点等风格映射为java属性命名风格
   * 目前仅支持下划线、点
   * </pre>
   */
  public static final String toJavaStyle(String name) {
    if (!isBlank(name)) {
      char[] ns = name.trim().toCharArray();
      char[] temp = new char[ns.length];
      char c;
      int j = 0;
      boolean prevIsUnderline = false;
      for (int i = 0, len = ns.length; i < len; i++) {
        c = ns[i];
        if (c == SYMBOL_UNDERLINE || c == SYMBOL_POINT) {
          prevIsUnderline = (j != 0);
        } else {
          temp[j++] = prevIsUnderline ? Character.toUpperCase(c) : Character.toLowerCase(c);
          prevIsUnderline = false;
        }
      }
      return String.valueOf(temp).trim();
    }
    return name;
  }

  /**
   * <pre>
   * 将已分隔符分割的字符串转换为java风格并映射到map集合中
   * 键为分割的每一个原始字符 值为转换后的字符
   * </pre>
   * 
   * @param str
   * @param delimiter
   * @return {column:property}
   */
  public static final Map<String, String> mapperJavaStyle(String str, String delimiter) {
    Map<String, String> map = new HashMap<String, String>();
    if (!isBlank(str)) {
      String[] strings = str.split(delimiter);
      for (String temp : strings) {
        map.put(temp, toJavaStyle(temp));
      }
    }
    return map;
  }

  /**
   * <pre>
   * 判断某个数字num的二进制表示的第place位是否为1；
   * 位置从0开始
   * </pre>
   * 
   * @param num
   * @param place
   * @return
   */
  public static final boolean isBitSet(Long num, int place) {
    return num != null && (((num >> place) & 1) == 1);
  }


  /**
   * <pre>
   * 格式化日期
   * 形如：yyyy-MM-dd hh:mm:ss
   * </pre>
   * 
   * @param calendar
   * @return
   */
  public static final String formatDate(Calendar calendar) {
    return getDateFormat().format(calendar.getTime());
  }

  public static final Long dateToTime(String datetime) {
    try {
      if (isBlank(datetime))
        return null;
      return getDateFormat().parse(datetime).getTime();
    } catch (ParseException e) {
      logger.error(e);
      return null;
    }
  }

  private static final SimpleDateFormat getDateFormat() {
    SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();
    if (simpleDateFormat == null) {
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      simpleDateFormatThreadLocal.set(simpleDateFormat);
    }
    return simpleDateFormat;
  }

  /**
   * 字符串首字母大写
   * 
   * @param str
   * @return
   */
  public static String initialUpeercase(String str) {
    return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
  }

  /**
   * 字符串首字母小写
   * 
   * @param str
   * @return
   */
  public static String initialLowercase(String str) {
    return String.valueOf(str.charAt(0)).toLowerCase() + str.substring(1);
  }


  /**
   * 根据文件路径获取文件内容
   * 
   * @param filePath
   */
  public static String getFileContent(String filePath, String charset) {
    StringBuilder sb = new StringBuilder();
    String str;
    try {
      BufferedReader br =
          new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
      while ((str = br.readLine()) != null) {
        sb.append(str);
      }
      br.close();
    } catch (Exception e) {
      logger.error(e);
    }
    return sb.toString();
  }

  /**
   * 将一个浮点数转换为百分比 末位四舍五入
   * 
   * @param val 要转换的值
   * @param decimal 保留的小树位数 不能超过10位
   * @return
   */
  public static String toPercent(double val, int decimal) {
    long dm = (long) Math.pow(10, decimal);
    long temp = Math.round(dm * 100 * val);
    // 整数部分
    String result = String.valueOf(temp / dm);
    String remain = String.valueOf(temp % dm);
    if (decimal == 0) {
      return result + IConstant.p_per_;
    }
    int[] prefix = new int[decimal - remain.length()];
    return result + IConstant.p_dot_
        + Arrays.toString(prefix).replace(", ", "").substring(1).replace("]", "") + remain
        + IConstant.p_per_;
  }

  /**
   * 将一个浮点数转换为百分比 保留两位小数点 末位四舍五入
   * 
   * @param val
   * @return
   */
  public static String toPercent(double val) {
    return toPercent(val, 2);
  }

  /**
   * 将list集合中的字符转换为字符串，格式用逗号，隔开
   * 
   * @param blank 集合为空时返回的字符
   */
  public static String toString(Collection<String> list, String blank) {
    String str = IConstant.blank_;
    for (String s : list) {
      str += s + IConstant.p_comma_;
    }
    int len = str.length();
    if (len > 0) {
      return str.substring(0, str.length() - 1);
    }
    return blank;
  }

  /**
   * 方法用于获取一个连接的内容
   * 
   * @param url 连接地址
   * @param charset 字符集,为null时默认为gb2312
   */
  public static String getUrlContent(String url, String charset) {
    if (charset == null)
      charset = IConstant.charset_gb2312;
    URL url_;
    StringBuilder sb = null;
    BufferedReader br = null;
    try {
      url_ = new URL(url);
      br = new BufferedReader(
          new InputStreamReader(url_.openConnection().getInputStream(), charset));
      sb = new StringBuilder(256);
      String str = null;
      while ((str = br.readLine()) != null) {
        sb.append(str);
      }
    } catch (Exception e) {
      logger.error(e);
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException e) {
        logger.error(e);
      }
    }
    return sb == null ? IConstant.blank_ : sb.toString();
  }

  /**
   * 转换为字符串列表并用指定的符合分隔
   * 
   * @param arr
   * @param delimiter
   * @return
   */
  public static final String join(Collection<String> arr, String delimiter) {
    StringBuilder sb = new StringBuilder(IConstant.blank_);
    for (String str : arr) {
      sb.append(str).append(delimiter);
    }
    int len = sb.length() - 1;
    if (len < 0)
      return IConstant.blank_;
    sb.deleteCharAt(len);
    return sb.toString();
  }

  public static final String long2Ip(long ip) {
    String sIp = "0.0.0.0";
    if (!(ip > 4294967295l || ip < 0)) {
      sIp = (ip >>> 24 & 0xff) + IConstant.p_dot_ + (ip >>> 16 & 0xff) + IConstant.p_dot_
          + (ip >>> 8 & 0xff) + IConstant.p_dot_ + (ip & 0xFf);
    }
    return sIp;
  }


  /**
   * 将IP转换为long
   */
  public static final long ip2Long(String ip) {
    ip = ip.trim();
    String[] ips = ip.split("\\.");
    long ipLong = 0l;
    for (int i = 0; i < 4; i++) {
      ipLong = ipLong << 8 | Integer.parseInt(ips[i]);
    }
    return ipLong;
  }

  /**
   * <pre>
   * 是否为基础类型或charsequence
   * </pre>
   * 
   * @param params
   * @return
   */
  public static boolean isPremitive(Object params) {
    Class<? extends Object> clazz = params.getClass();
    return clazz.isPrimitive() || CharSequence.class.isAssignableFrom(clazz);
  }

  /**
   * <pre>
   * 将一个map转换为不区分大小写的map
   * </pre>
   * 
   * @param map
   * @return
   */
  public static final <T> Map<String, T> convertToCaseInsensitiveMap(Map<String, T> map) {
    CaseInsensitiveMap<String, T> insensitiveMap = new CaseInsensitiveMap<>();
    if (map != null) {
      insensitiveMap.putAll(map);
    }
    return insensitiveMap;
  }


  /**
   * <pre>
   * 将已分隔符分割的字符串转换为java风格并映射到CaseInsensitiveMap集合中
   * 键为分割的每一个原始字符 值为转换后的字符
   * </pre>
   *
   * @param str
   * @param delimiter
   * @return {column:property}
   */
  public static final Map<String, String> mapperJavaStyleToCaseInsensitiveMap(String str,
      String delimiter) {
    CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
    if (!isBlank(str)) {
      String[] strings = str.split(delimiter);
      for (String temp : strings) {
        map.put(temp.trim(), toJavaStyle(temp));
      }
    }
    return map;
  }

  /**
   * <pre>
   * 判定一个字符串的开头是否为指定字符串
   * 不区分大小写
   * </pre>
   * 
   * @param src
   * @param dest
   * @return
   */
  public static final boolean startsIgnoreCase(String src, String dest) {
    if (src.length() < dest.length()) {
      return false;
    }
    return src.substring(0, dest.length()).equalsIgnoreCase(dest);
  }

  /**
   * <pre>
   * 判定一个字符串的结尾是否为指定字符串
   * 不区分大小写
   * </pre>
   * 
   * @param src
   * @param dest
   * @return
   */
  public static final boolean endsIgnoreCase(String src, String dest) {
    if (src.length() < dest.length()) {
      return false;
    }
    return src.substring(src.length() - dest.length()).equalsIgnoreCase(dest);
  }

  /**
   * <pre>
   * 判定一个字符串是否等于指定的任何一个字符串
   * 不区分大小写
   * </pre>
   * 
   * @param src
   * @param dest
   * @return
   */
  public static final boolean equalsIgnoreCase(String src, String... dest) {
    for (String d : dest) {
      if (src.equalsIgnoreCase(d)) {
        return true;
      }
    }
    return false;
  }
}

