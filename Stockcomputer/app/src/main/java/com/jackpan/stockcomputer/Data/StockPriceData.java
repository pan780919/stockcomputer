package com.jackpan.stockcomputer.Data;

public  class StockPriceData {
    public  String stockPriceDataArray;
    public String data;
    public String deal;
    public String money;
    public String opening;
    public String high;
    public String low;
    public String closeing;
    public String difference;
    public String turnover;

    public void setStockPriceDataArray(String stockPriceDataArray) {
        this.stockPriceDataArray = stockPriceDataArray;
    }

    public String getStockPriceDataArray() {
        return stockPriceDataArray;
    }


    public void setSolitArrayData(){
        String[] arrayString = getStockPriceDataArray().split("");
        arrayString[0] = data;
        arrayString[1] = deal;
        arrayString[2] = money;
        arrayString[3] = opening;
        arrayString[4] = high;
        arrayString[5] = low;
        arrayString[6] = closeing;
        arrayString[7] = difference;
        arrayString[8] = turnover;


    }
}
