package com.cc.orm.mybatis.plugin.pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.cc.orm.mybatis.plugin.pagination.dialect.AbstractDialect;
import com.cc.orm.mybatis.plugin.pagination.dialect.DialectEnum;
import com.cc.tool.IConstant;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

/**
 * @author wenlongchen
 * @since Nov 1, 2016
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {


  private static final int MAPPED_STATEMENT_INDEX = 0;
  private static final int PARAMETER_INDEX = 1;
  private static final int ROWBOUNDS_INDEX = 2;
  private static final String sql = "sql";
  private static final String SQLSOURCE_STRING = "sqlSource";
  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY =
      new DefaultObjectWrapperFactory();
  private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

  private AbstractDialect dialect;

  private static final Map<String, Builder> BUILDER_MAP = new HashMap<String, Builder>();

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Object intercept(final Invocation invocation) throws Throwable {
    final Object[] queryArgs = invocation.getArgs();
    final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
    final BoundSql boundSql = ms.getBoundSql(queryArgs[PARAMETER_INDEX]);
    final Object prameterObj = boundSql.getParameterObject();

    if (prameterObj instanceof Page) {
      Page page = (Page) prameterObj;
      int count = getCount(((Executor) invocation.getTarget()).getTransaction().getConnection(),
          boundSql, page, ms);
      page.setCount(count);
      if (count == 0) {
        page.emptyDatas();
      } else {
        queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        queryArgs[MAPPED_STATEMENT_INDEX] = getPageStatement(ms, boundSql, page);
        page.setDatas((List) invocation.proceed());
      }
      return page.getDatas();
    }
    return invocation.proceed();
  }

  private final MappedStatement getPageStatement(MappedStatement ms, BoundSql boundSql,
      Page page) {
    String id = ms.getId();
    Builder builder = BUILDER_MAP.get(id);

    if (builder == null) {
      builder = new Builder(ms.getConfiguration(), ms.getId(), new CCDefaultSqlSource(boundSql),
          ms.getSqlCommandType());
      builder.resource(ms.getResource());
      builder.fetchSize(ms.getFetchSize());
      builder.statementType(ms.getStatementType());
      builder.keyGenerator(ms.getKeyGenerator());
      if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
        StringBuffer keyProperties = new StringBuffer();
        for (String keyProperty : ms.getKeyProperties()) {
          keyProperties.append(keyProperty).append(",");
        }
        keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
        builder.keyProperty(keyProperties.toString());
      }

      builder.timeout(ms.getTimeout());

      builder.parameterMap(ms.getParameterMap());

      builder.resultMaps(ms.getResultMaps());
      builder.resultSetType(ms.getResultSetType());

      builder.cache(ms.getCache());
      builder.flushCacheRequired(ms.isFlushCacheRequired());
      builder.useCache(ms.isUseCache());
      BUILDER_MAP.put(id, builder);
    }

    ms = builder.build();

    MetaObject.forObject(boundSql, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
        DEFAULT_REFLECTOR_FACTORY).setValue(sql, dialect.getPageSql(boundSql.getSql(), page));
    MetaObject.forObject(ms, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
        DEFAULT_REFLECTOR_FACTORY).setValue(SQLSOURCE_STRING, new CCDefaultSqlSource(boundSql));

    return ms;
  }

  private static final int getCount(Connection connection, BoundSql boundSql, Page page,
      MappedStatement mappedStatement) throws SQLException {
    int count = 0;
    final String countSql = AbstractDialect.getCountSql(boundSql.getSql());
    final PreparedStatement countStmt = connection.prepareStatement(countSql);
    final DefaultParameterHandler handler =
        new DefaultParameterHandler(mappedStatement, page, boundSql);
    handler.setParameters(countStmt);
    final ResultSet rs = countStmt.executeQuery();
    if (rs.next()) {
      count = rs.getInt(1);
    }
    rs.close();
    countStmt.close();
    return count;
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    if(dialect==null){
      dialect=DialectEnum.getDialect(properties.getProperty(IConstant.str_dialect));
    }
  }
  
  /**
   * <pre>
   * 设置数据库方言
   * 该设置将覆盖通过配置文件配置的数据库方言
   * </pre>
   * @param dialect
   */
  public void setDialect(DialectEnum dialect){
    this.dialect=dialect.getDialect();
  }
}

