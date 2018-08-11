package com.jackpan.stockcomputer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HYXEN20141227 on 2016/6/20.
 */
public class MySharedPrefernces {


    public static final String  NAME = "MySharedPrefernces";

    //首頁-是否第一次使用
    public static final String KEY_IS_BUY = "isBuy";
    public static void saveIsBuyed(Context context, boolean isBuyed) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_IS_BUY, isBuyed).apply();
    }

    public static boolean getIsBuyed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(KEY_IS_BUY, false);
    }
    public static  final  String KEY_UESR_PHOTO = "user_photo";

    public static  void saveUserPhoto(Context context,String photo){
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_UESR_PHOTO, photo).apply();
    }

    public static String getUserPhoto(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_UESR_PHOTO, "");
    }
    public static final String KEY_USER_ID = "userid";


    public static  void saveUserId(Context context,String id){
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USER_ID, id).apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USER_ID, "");
    }
    public static final String KEY_USER_MAIL = "usermail";

    public static  void saveUserMail(Context context,String mail){
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USER_MAIL, mail).apply();
    }

    public static String getUserMail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USER_MAIL, "");
    }
    public static final String KEY_USER_Name = "username";

    public static  void saveUserName(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USER_Name, name).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USER_Name, "");
    }

    public static final String KEY_USER_LOGIN_STATE= "userloginstate";
    public static  void saveUserLoginState(Context context,int state){
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putInt(KEY_USER_LOGIN_STATE, state).apply();
    }

    public static int getUserLoginState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getInt(KEY_USER_LOGIN_STATE,0);
    }


}
