package com.jackpan.stockcomputer.Manager;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInReadAd;

public class VponAdManager {
    private static final String TAG = "VponAdManager";
    // 請將 License Key 換成 Vpon BD 提供您的 License Key

    public static void  setScrollViewAd(VpadnInReadAd inReadAd,FrameLayout frameLayout){

        // 建立廣告請求
        VpadnAdRequest adRequest = new VpadnAdRequest();
        adRequest.setAdContainer(frameLayout);

        inReadAd.setAdListener(new VpadnAdListener() {
            @Override
            public void onVpadnReceiveAd(VpadnAd vpadnAd) {
                Log.d(TAG, "onVpadnReceiveAd: "+vpadnAd.toString());
            }

            @Override
            public void onVpadnFailedToReceiveAd(VpadnAd vpadnAd, VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
                Log.d(TAG, "onVpadnFailedToReceiveAd: "+vpadnErrorCode.toString());
            }

            @Override
            public void onVpadnPresentScreen(VpadnAd vpadnAd) {

            }

            @Override
            public void onVpadnDismissScreen(VpadnAd vpadnAd) {

            }

            @Override
            public void onVpadnLeaveApplication(VpadnAd vpadnAd) {

            }
        });

        // 拉取廣告
        inReadAd.loadAd(adRequest);



    }




}
