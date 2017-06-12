package com.cc.db.core.schema.type;

import com.cc.db.core.schema.bo.Column;
import com.cc.db.core.schema.bo.Table;

/**
 * @author wenlongchen
 * @since Jan 23, 2017
 */
public interface Conventer {
  
  /**
   * <pre>
   * 将旧的数据库类型转换为新的并返回
   * </pre>
   * @param oldType
   * @return
   */
  Column convent(Column oldColumn);
  
  Table convent(Table oldTable);
}

