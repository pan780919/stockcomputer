package com.jackpan.stockcomputer.Data;

public class CalculateData {

    public double stockPrice;
    public long stockTotal;
    public long price;
    public int state;


    public void setPrice(long price) {
        this.price = price;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setStockTotal(long stockTotal) {
        this.stockTotal = stockTotal;
    }

    public void setState(int state) {
        this.state = state;
    }
}
