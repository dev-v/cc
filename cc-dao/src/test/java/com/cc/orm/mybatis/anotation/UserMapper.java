package com.cc.orm.mybatis.anotation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import com.cc.db.orm.mybatis.mapper.BaseSql;

/**
 * @author wenlongchen
 * @since Jun 22, 2016
 */
public interface UserMapper {

  /**
   * <pre>
   * 直接使用basesql中定义的简单查询 
   * 对应定义了已METHOD开头的常量
   * </pre>
   * 
   * @param id 
   * @return
   */
  @SelectProvider(type = UserSql.class, method = BaseSql.METHOD_getByKey)
  User getUserById(@Param("id") Long id);

  /**
   * <pre>
   * 使用自定义查询
   * 需要定义复杂查询条件时使用
   * </pre>
   * 
   * @return
   */
  @SelectProvider(type = UserSql.class, method = "selectByCustom")
  User getUser(@Param("id") String id,@Param("name") String name);

  /**
   * <pre>
   * 根据主键集合查询数据
   * </pre>
   * @param ids
   * @return
   */
  @SelectProvider(type = UserSql.class, method = BaseSql.METHOD_selectByKeys)
  List<User> getUsers(@Param("id") Collection<Long> ids);

  final class UserSql extends BaseSql {
    public UserSql() {
      /**
       * <pre>
       * args0:表名
       * args1:查询列表 符合规范的数据库字段命名和java属性命名方式的 将进行自动映射 
       *  支持下划线和点分割
       * </pre>
       */
      super("user","id,abc_name,tttAddress");
      
      // 非规范的数据库字段命名 需手动映射为java属性 mapper只适用于查询列表
      mapper("abc_name", "name");
      
      // 非规范的数据库字段命名 需手动映射为java属性 此mapper适用于查询列表和insert、update操作
      operateMapper("tttAddress", "address");
    }

    /**
     * <pre>
     * 自定义查询
     * </pre>
     * 
     * @param param
     * @return
     */
    public final String selectByCustom(Map<String, String> param) {
      // getQueryList 获取一般查询的查询列表 getTable()获取表名
      SQL sql = new SQL().SELECT(getQueryList()).FROM(getTable());

      String condition = conditionEQ("name", param);
      if (condition != null) {
        sql.OR().WHERE(condition);
      }
      
      condition = conditionGE("id", param);
      if (condition != null) {
        sql.AND().WHERE(condition);
      }

      return sql.toString();
    }
  }
}
