package com.cc.tool;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public interface IConstant {
  /**
   * 分号：;
   */
  String p_colon = ";";

  String p_open = "(";

  String p_close = ")";

  String p_open_b = "{";

  String p_close_b = "}";

  String p_dollar = "$";

  String p_backslash_ = "/";

  String p_asterisk = "*";

  /**
   * 空字符串
   */
  String blank_ = "";

  /**
   * 一个空格
   */
  String empty_ = " ";

  /**
   * 逗号：,
   */
  String p_comma_ = ",";
  /**
   * 问号：?
   */
  String p_question_ = "?";

  /**
   * .
   */
  String p_dot_ = ".";

  /**
   * 百分号：%
   */
  String p_per_ = "%";

  /**
   * 正则格式：[\\[\\]]，常用于将数组替换成字符串 匹配字符 [和]
   */
  String reg_arr = "[\\[\\]]";

  String pos_zero_ = "{0}";
  String pos_one_ = "{1}";
  String pos_two_ = "{2}";
  String pos_three_ = "{3}";

  String charset_gb2312 = "gb2312";
  String charset_gbk = "gbk";
  String charset_utf8 = "utf-8";
  String charset_utf16 = "utf-16";
  String charset_iso88591 = "iso8859-1";
  String charset_unicode = "unicode";

  String num_zero_ = "0";
  String num_one_ = "1";
  String num_two_ = "2";
  String num_three_ = "3";
  String num_six_ = "6";

  String str_code = "code";
  String str_data = "data";
  String str_id = "id";
  String str_where = "where";
  String str_dialect = "dialect";
  String str_SELECT = "SELECT";

  String str_path = "path";
  String str_lang = "lang";
  String str_cn = "cn";
  String str_maxFiles = "maxFiles";
  String str_obj = "obj";
  String str_keyField = "keyField";
  String str_index = "index";
  String str_type = "type";
  String str_store = "store";
  String str_field = "field";
  String str_file = "file";
  String str_searchMaxDocs = "searchMaxDocs";

  String dbtype_mysql = "mysql";
  String dbtype_oracle = "oracle";

  Integer not_insert_items = 0;

  long TEMP_KEY_VAL = 8962175219863987239l;
}

