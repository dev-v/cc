package com.cc.other.hourse;


/**
 * @author wenlongchen
 * @since Dec 21, 2016
 */
public class Hourse {
  private double price;
  private double downPrice;
  private double downPercent;
  private double loanPrice;
  private double loanMonths;
  private double monthPayment;
  private double interest;
  private String address;
  private String description;
  private double rate;

  public Hourse(double price, double downPercent, double loanMonths, double rate) {
    setPrice(price);
    setDownPercent(downPercent);
    setLoanMonths(loanMonths);
    setRate(rate);
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public double getMonthPayment() {
    return monthPayment;
  }

  public void setMonthPayment(double monthPayment) {
    this.monthPayment = monthPayment;
  }

  public double getInterest() {
    return interest;
  }

  public void setInterest(double interest) {
    this.interest = interest;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getDownPercent() {
    return downPercent;
  }

  public void setDownPercent(double downPercent) {
    this.downPercent = downPercent;
  }

  public double getLoanPrice() {
    return loanPrice;
  }

  public void setLoanPrice(double loanPrice) {
    this.loanPrice = loanPrice;
  }

  public double getDownPrice() {
    return downPrice;
  }

  public void setDownPrice(double downPrice) {
    this.downPrice = downPrice;
  }

  public double getLoanMonths() {
    return loanMonths;
  }

  public void setLoanMonths(double loanMonths) {
    this.loanMonths = loanMonths;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }
}

