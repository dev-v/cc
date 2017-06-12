package com.cc.other.hourse;


/**
 * @author wenlongchen
 * @since Dec 21, 2016
 */
public class HourseCaculate {
  public static final Hourse caculate(Hourse hourse){
    double price=hourse.getPrice();
    double downPrice=price*hourse.getDownPercent();
    double loanPrice=price-downPrice;
    double loanMonths=hourse.getLoanMonths();
    double monthRate=hourse.getRate()/12;
//    〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
    double monthPayment=(loanPrice*monthRate*Math.pow((1+monthRate),loanMonths));///(Math.pow((1+monthRate),loanMonths-1));
    return hourse;
  }
  
  public static void main(String[] args) {
    Hourse hourse=new Hourse(1020000,0.3,240,0.049);
    caculate(hourse);
  }
}

