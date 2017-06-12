package com.cc.tool;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * @author wenlongchen
 * @since Feb 22, 2017
 */
public class CipherUtilTest {
  private static final Logger LOG=Logger.getLogger(CipherUtilTest.class);
  private static final String SALT =
      "6dbf62d5b35c4";

  @Test
  public void test() {
    String data="123456";
    String key="562";
    LOG.info(key);
    String encrtyptData=CipherUtil.DESEncrypt(data, key);
    LOG.info(encrtyptData);
    String decryptData=CipherUtil.DESDecrypt(encrtyptData, key);
    LOG.info(decryptData);
    assertEquals(data,decryptData);
  }

}

