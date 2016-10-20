package com.cc.tool;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author wenlongchen
 * @since Jun 23, 2016
 */
public class UtilTest {

    @Test
    public void isBlank() {
        assertTrue(Util.isBlank(""));
        assertTrue(Util.isBlank(" "));
        assertTrue(Util.isBlank("   "));
        assertTrue(Util.isBlank(null));
        assertFalse(Util.isBlank(" asfd "));
        assertFalse(Util.isBlank("safa "));
        assertFalse(Util.isBlank(" afas"));
        assertFalse(Util.isBlank("safsa"));
        assertFalse(Util.isBlank("d"));
    }

    @Test
    public void toJavaStyle() {
        assertNull(Util.toJavaStyle(null));
        assertEquals("", Util.toJavaStyle(""));
        assertEquals("  ", Util.toJavaStyle("  "));

        assertEquals("userName", Util.toJavaStyle("User_name"));
        assertEquals("userName", Util.toJavaStyle(" _user_name  "));
        assertEquals("userName", Util.toJavaStyle("__uSer_Name"));
        assertEquals("userName", Util.toJavaStyle("usER___Name"));
        assertEquals("userName", Util.toJavaStyle("usER___Name___"));

        assertEquals("userName", Util.toJavaStyle("user.name"));
        assertEquals("userName", Util.toJavaStyle("  .user.name"));
        assertEquals("userName", Util.toJavaStyle("..uSer.Name"));
        assertEquals("userName", Util.toJavaStyle("usER...Name"));
        assertEquals("userName", Util.toJavaStyle("usER...Name..."));

        assertEquals("userName", Util.toJavaStyle("usER._.Name..."));
    }

    @Test
    public void binaryPlace() {
        Long num = 133l;
        assertTrue(Util.isBitSet(num, 0));
        assertTrue(Util.isBitSet(num, 2));
        assertTrue(Util.isBitSet(num, 7));
        assertFalse(Util.isBitSet(num, 8));
    }
}

