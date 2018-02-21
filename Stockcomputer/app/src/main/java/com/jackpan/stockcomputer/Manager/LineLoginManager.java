package com.jackpan.stockcomputer.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

public class LineLoginManager {
    public static  final  String ChannelID = "1560012315";
    public static  final  int REQUEST_CODE = 1;

    public static  void LineLogin(Context context){

        try{
            // App-to-app login
            Intent loginIntent = LineLoginApi.getLoginIntent(context, ChannelID);
            ((Activity)context).startActivityForResult(loginIntent, REQUEST_CODE);

        }
        catch(Exception e) {
            Log.e("ERROR", e.toString());
        }
    }
    public static void getLineLoginResult(Context context,int requestCode, Intent data,Class<?> clas){
        if(requestCode!= REQUEST_CODE){
            return;
        }
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful

                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();

                Intent transitionIntent = new Intent(context, clas);
                transitionIntent.putExtra("line_profile", result.getLineProfile());
                transitionIntent.putExtra("line_credential", result.getLineCredential());
                ((Activity)context).startActivity(transitionIntent);
                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user!!");
                break;

            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }

    }




}
