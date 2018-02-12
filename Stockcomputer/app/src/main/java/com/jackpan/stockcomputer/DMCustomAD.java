package com.jackpan.stockcomputer;

import android.app.Activity;
import android.util.Log;

import com.clickforce.ad.Listener.AdViewLinstener;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

/**
 * Created by YaoChang on 2017/7/20.
 */



public class DMCustomAD  implements CustomEventBanner {
    private com.clickforce.ad.AdView ad;

    @Override
    public void destroy() {

        if (ad != null)
            ad.releaseAd();
    }

    @Override
    public void requestBannerAd(final CustomEventBannerListener listener,
                                final Activity activity, String label, String serverParameter,
                                AdSize adSize, MediationAdRequest request, Object customEventExtra) {

        Log.d("label", label);
        Log.d("Parameter", serverParameter);


        ad = new com.clickforce.ad.AdView(activity);
        ad.getAd(Integer.parseInt(serverParameter),320,50,0.8);
        listener.onReceivedAd(ad);

        ad.setOnAdViewLoaded(new AdViewLinstener() {
            @Override
            public void OnAdViewLoadFail() {

            }

            @Override
            public void OnAdViewLoadSuccess() {

                ad.show();
            }
        });

    }
}
