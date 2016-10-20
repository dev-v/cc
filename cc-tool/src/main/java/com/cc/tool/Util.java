package com.cc.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
}

