package com.jackpan.stockcomputer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.adlocus.PushAd;
import com.firebase.client.Firebase;
import com.jackpan.stockcomputer.Data.MyApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.jackpan.stockcomputer.util.AlarmReceiver;

import java.util.Calendar;

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
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Intent promotionIntent = new Intent(this, MainActivity.class);
        PushAd.enablePush(this, MyApi.AdLocusKey, promotionIntent);
        MyApi.getKeyHash(myApplication);
        setNotice();

    }

    public void setNotice() {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 7, 15, 14, 34);
//建立意圖
        Intent intent = new Intent();
//這裡的 this 是指當前的 Activity
//AlarmReceiver.class 則是負責接收的 BroadcastReceiver
        intent.setClass(this, AlarmReceiver.class);
//建立待處理意圖
        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//取得AlarmManager
//設定一個警報
//參數1,我們選擇一個會在指定時間喚醒裝置的警報類型
//參數2,將指定的時間以millisecond傳入
//參數3,傳入待處理意圖
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
    }
}
