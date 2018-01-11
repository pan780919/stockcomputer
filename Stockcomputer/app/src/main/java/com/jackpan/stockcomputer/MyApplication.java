package com.jackpan.stockcomputer;

import android.app.Application;
import android.content.Intent;

import com.adlocus.PushAd;
import com.jackpan.stockcomputer.Data.MyApi;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent promotionIntent = new Intent(this, MainActivity.class);
        PushAd.enablePush(this, MyApi.AdLocusKey, promotionIntent );
    }
}
