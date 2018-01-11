package com.jackpan.stockcomputer.Data;

public class CalculateData {

    public double stockPrice;
    public int stockTotal;
    public long price;
    public int state;


    public void setPrice(long price) {
        this.price = price;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public void setState(int state) {
        this.state = state;
    }
}
