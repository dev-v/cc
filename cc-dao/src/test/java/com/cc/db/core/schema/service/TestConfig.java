package com.cc.db.core.schema.service;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.cc.db.core.source.DBConfigUtil;
import com.cc.db.core.source.JdbcConfig;

/**
 * @author wenlongchen
 * @since Jan 22, 2017
 */
@TestConfiguration
public class TestConfig {

  @Bean
  @ConfigurationProperties(prefix = "db.datasource.oracle.mainapp")
  public JdbcConfig jdbcConfig() {
    return new JdbcConfig();
  }

  @Primary
  @Bean(name="mainappDataSource")
  public DataSource mainappDataSource() {
    JdbcConfig config = jdbcConfig();
    return DBConfigUtil.dataSource(config);
  }

  @Bean
  @ConfigurationProperties(prefix = "db.datasource.mysql.dajobschema")
  public JdbcConfig dajobschema() {
    return new JdbcConfig();
  }

  @Bean(name="dajobschemaDataSource")
  public DataSource dajobschemaDataSource() {
    JdbcConfig config = dajobschema();
    return DBConfigUtil.dataSource(config);
  }

  @Bean
  @ConfigurationProperties(prefix = "db.datasource.mysql.crm")
  public JdbcConfig crmJdbcConfig() {
    return new JdbcConfig();
  }

  @Bean
  @ConfigurationProperties(prefix = "db.datasource.oracle.wm")
  public JdbcConfig wmJdbcConfig() {
    return new JdbcConfig();
  }

  @Bean(name="crmDataSource")
  public DataSource crmDataSource() {
    JdbcConfig config = crmJdbcConfig();
    return DBConfigUtil.dataSource(config);
  }

  @Bean(name="wmDataSource")
  public DataSource wmDataSource() {
    JdbcConfig config = wmJdbcConfig();
    return DBConfigUtil.dataSource(config);
  }
}

