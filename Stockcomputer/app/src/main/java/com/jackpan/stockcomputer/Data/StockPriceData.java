package com.jackpan.stockcomputer.Data;

import android.util.Log;

public class StockPriceData {
    private static final String TAG = "StockPriceData";
    public String stockPriceDataArray;
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
        Log.d(TAG, "setStockPriceDataArray: " + stockPriceDataArray);
        this.stockPriceDataArray = stockPriceDataArray;
    }

    public String getStockPriceDataArray() {
        return stockPriceDataArray;
    }


    public void getSolitArrayData() {
        String[] arrayString = getStockPriceDataArray().split(" ");
        Log.d(TAG, "getSolitArrayData: " + arrayString[0]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[1]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[2]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[3]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[4]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[5]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[6]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[7]);
        Log.d(TAG, "getSolitArrayData: " + arrayString[8]);
        data = arrayString[0];
        deal = arrayString[1];
        money = arrayString[2];
        opening = arrayString[3];
        high = arrayString[4];
        low = arrayString[5];
        closeing = arrayString[6];
        difference = arrayString[7];
        turnover = arrayString[8];


    }
}
