package com.cc.db.core.source;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author wenlongchen
 * @since Aug 19, 2016
 */
public class DBConfigUtil {
  private static Logger logger = Logger.getLogger(DBConfigUtil.class);

  private static final Map<DataSource, JdbcTemplate> JDBC_TEMPLATE = new HashMap<>();

  public static final DataSource dataSource(JdbcConfig config) {
    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setJdbcUrl(config.getUrl());
    dataSource.setUser(config.getUsername());
    dataSource.setPassword(config.getPassword());
    dataSource.setMaxIdleTime(60 * 60 * 8);
    try {
      dataSource.setDriverClass(config.getDriver());
    } catch (PropertyVetoException e) {
      logger.error(e);
    }
    return dataSource;
  }

  public static final JdbcTemplate getJdbcTemplate(DataSource dataSource) {
    JdbcTemplate template = JDBC_TEMPLATE.get(dataSource);
    if (template == null) {
      template = new JdbcTemplate(dataSource);
      JDBC_TEMPLATE.put(dataSource, template);
    }
    return template;
  }
}

