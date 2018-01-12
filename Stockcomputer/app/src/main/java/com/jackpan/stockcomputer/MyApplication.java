package com.jackpan.stockcomputer;

import android.app.Application;
import android.content.Intent;

import com.adlocus.PushAd;
import com.jackpan.stockcomputer.Data.MyApi;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        Intent promotionIntent = new Intent(this, MainActivity.class);
        PushAd.enablePush(this, MyApi.AdLocusKey, promotionIntent );
        MyApi.getKeyHash(myApplication);

    }
}
