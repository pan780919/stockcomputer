package com.jackpan.stockcomputer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adbert.AdbertListener;
import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.ExpandVideoPosition;
import com.clickforce.ad.Listener.AdViewLinstener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{
    private AdView mAdView;
    private static final String TAG = "MainActivity";

    private RelativeLayout adBannerLayout;
    private VpadnBanner vponBanner = null;
    //Vpon TODO:  Banner ID
    private String bannerId = "8a8081824eb5519a014eca83ab981d91" ;
    private  com.clickforce.ad.AdView clickforceAd;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> newlist= new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.newslistview) ListView listView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setAdmobBanner();
        setVponBanner();
        setAdbertBanner();
        setClickForce();
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
        setNewsData();
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,newlist);
        listView.setAdapter(listAdapter);
    }
    @OnClick(R.id.nav_gallery)
    public void Click(){
        startActivity(new Intent(this,ProfitAndLossActvity.class));
    }
    @OnClick(R.id.messenger_send_button)
    public void sendMessageButton(){
        onMessengerButtonClicked();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private  void setVponBanner(){

        //get your layout view for Vpon banner
        adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
        //create VpadnBanner instance
        vponBanner = new VpadnBanner(this, bannerId, VpadnAdSize.SMART_BANNER, "TW");
        VpadnAdRequest adRequest2 = new VpadnAdRequest();
        //set auto refresh to get banner
        adRequest2.setEnableAutoRefresh(true);
        //load vpon banner
        vponBanner.loadAd(adRequest2);
        //add vpon banner to your layout view
        adBannerLayout.addView(vponBanner);
    }
    private void setAdmobBanner() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    private void setAdbertBanner(){
        AdbertLoopADView adbertView =(AdbertLoopADView)findViewById(R.id.adbertADView);
        adbertView.setMode(AdbertOrientation.NORMAL);
        adbertView.setExpandVideo(ExpandVideoPosition.BOTTOM);
        adbertView.setFullScreen(false);
        adbertView.setBannerSize(AdSize.BANNER);
        adbertView.setAPPID("20170619000001", "90cebe8ef120c8bb6ac2ce529dcb99af");
        adbertView.setListener(new AdbertListener() {
            @Override
            public void onReceive(String msg) {
                Log.d(TAG, "onReceive: " + msg);
            }

            @Override
            public void onFailedReceive(String msg) {
                Log.d(TAG, "onFailedReceive: " + msg);

            }
        });
        adbertView.start();
    }
    private  void setClickForce(){

        clickforceAd = (com.clickforce.ad.AdView)findViewById(R.id.ad);
        clickforceAd.getAd(6120,320,50,0.8,30);



        //Ad Load Callback
        clickforceAd.setOnAdViewLoaded(new AdViewLinstener() {
            @Override
            public void OnAdViewLoadFail() {
                Log.d(TAG, "請求廣告失敗");
            }
            @Override
            public void OnAdViewLoadSuccess() {
                Log.d(TAG, "成功請求廣告");

                //顯示banner廣告
                clickforceAd.show();
            }
        });

        clickforceAd.outputDebugInfo = true;

    }

    private void onMessengerButtonClicked() {
        // The URI can reference a file://, content://, or android.resource. Here we use
        // android.resource for sample purposes.
        Uri uri =
                Uri.parse("android.resource://com.facebook.samples.messenger.send/" + R.drawable.ic_audiotrack);

        // Create the parameters for what we want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                        .setMetaData("{ \"image\" : \"tree\" }")
                        .build();

        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.
            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
        } else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    this,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
    }

    private  void setNewsData(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/news_list/url/d/e/N3.html?q=&pg=1").get();
                    for (Element table : doc.select("table#newListContainer")) {
                        for (Element tbody : table.select("tbody")) {
                            for (Element tr : tbody.select("tr")) {
                                for (Element td : tr.select("td[valign=top]>a.mbody")) {
                                    Log.d(TAG, "run: "+td.text());
                                    Log.d(TAG, "run: "+td.getElementsByTag("a").attr("href").toString());
//                                    Log.d(TAG, "run: "+td.html());
//                                    Log.d(TAG, "run: "+td.toString());
//                                    for (Element span : td.select("span")) {
//                                        for (Element a : span.getElementsByTag("a")) {
//                                        d    Log.d(TAG, "run: "+a.attr("href").toString());
//                                        }
//                                    }););
                                    newlist.add(td.text());
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: "+e.getMessage());
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                                 listAdapter.notifyDataSetChanged();
                                mProgressDialog.dismiss();


                    }
                });
            }
        }.start();
    }
}
