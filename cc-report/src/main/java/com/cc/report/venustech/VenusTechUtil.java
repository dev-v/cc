package com.cc.report.venustech;
public class VenusTechUtil {
	public static String Long2Ip(long ip) {
	    String sIp = "0.0.0.0";
	    if (!(ip > 4294967295L || ip < 0)) {
	        long a = ip >>> 24 & 0xff;
	        long b = ip >>> 16 & 0xff;
	        long c = ip >>> 8 & 0xff;
	        long d = ip & 0xFf;
	        sIp = a + "." + b + "." + c + "." + d;
	    }
	    return sIp;
	}
}
