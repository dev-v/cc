package com.cc;


/**
 * @author wenlongchen
 * @since Feb 15, 2017
 */
public class Test {

  public static enum Status {
    DO_NOT_USE_00, // 0
    DO_NOT_USE_01, // 1
    DO_NOT_USE_02, // 2
    DO_NOT_USE_03, // 3
    DO_NOT_USE_04, // 4
    DO_NOT_USE_05, // 5
    DO_NOT_USE_06, // 6
    DO_NOT_USE_07, // 7
    INTERNAL, // 8
    DO_NOT_USE_09, // 9
    DO_NOT_USE_10, // 10

    S11_FUTURE, // 11
    S12_NOTIFIED, // 12
    S13_PENDING_ACH, // 13
    S14_PENDING_GRACE_PERIOD, // 14
    S15_PENDING_LATE16_31, // 15
    S16_CLOSED_UNPAID, // 16
    S17_CLOSED_PAID, // 17
    S18_CLOSED_PAID_OFF, // 18
    S19_CLOSED_CHARGEDOFF, // 19
    S20_CANCELED, // 20
    S21_VIRTUAL_LPAY, // 21
    ;
  }
  
  public static void main(String[] args) {
    StringBuilder sb=new StringBuilder("CASE");
    for(Status status:Status.values()){
      sb.append("\r\n  WHEN lpay.STATUS="+status.ordinal()+" THEN '"+status.name()+"'");
    }
    sb.append("\r\nELSE lpay.STATUS END");
    System.out.println(sb.toString());
  }
}

