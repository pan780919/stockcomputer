package com.jackpan.stockcomputer;

import android.app.Application;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.adlocus.PushAd;
import com.firebase.client.Firebase;
import com.jackpan.stockcomputer.Data.MyApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
public class MyApplication extends MultiDexApplication {
    private static MyApplication myApplication;
    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        Firebase.setAndroidContext(this);

        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Intent promotionIntent = new Intent(this, MainActivity.class);
        PushAd.enablePush(this, MyApi.AdLocusKey, promotionIntent );
        MyApi.getKeyHash(myApplication);

    }
}
