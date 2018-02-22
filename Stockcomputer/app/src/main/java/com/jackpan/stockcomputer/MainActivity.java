package com.jackpan.stockcomputer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.ExpandVideoPosition;
import com.clickforce.ad.Listener.AdViewLinstener;
import com.facebook.AccessToken;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.Gson;
import com.jackpan.libs.mfirebaselib.MfiebaselibsClass;
import com.jackpan.libs.mfirebaselib.MfirebaeCallback;
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity;
import com.jackpan.stockcomputer.Activity.CalculateActivity;
import com.jackpan.stockcomputer.Activity.ShareStockNumberActivity;
import com.jackpan.stockcomputer.Data.MemberData;
import com.jackpan.stockcomputer.Data.MyApi;
import com.jackpan.stockcomputer.Data.NewsData;
import com.jackpan.stockcomputer.Kotlin.BuyAndSellActivity;
import com.jackpan.stockcomputer.Kotlin.LoginActivity;
import com.jackpan.stockcomputer.Kotlin.MemberCenterActivity;
import com.jackpan.stockcomputer.Kotlin.NewDetailActivity;
import com.jackpan.stockcomputer.Kotlin.QueryStockPriceActivity;
import com.jackpan.stockcomputer.Kotlin.StockValueAddedRateActivity;
import com.jackpan.stockcomputer.Kotlin.ZeroStockActivity;
import com.jackpan.stockcomputer.Manager.FacebookManager;
import com.jackpan.stockcomputer.Manager.LineLoginManager;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends BaseAppCompatActivity implements MfirebaeCallback {
    private static final String TAG = "MainActivity";
    private VpadnBanner vponBanner = null;
    //Vpon TODO:  Banner ID
    private String bannerId = "8a8081824eb5519a014eca83ab981d91";
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private ProgressDialog mProgressDialog;
    private ArrayList<NewsData> newlist = new ArrayList<>();
    private MyAdapter mAdapter;
    private MfiebaselibsClass mfiebaselibsClass;

    private static final int LOGINSTATE = 0 ;

    private RewardedVideoAd mRewardedVideoAd;

    private ArrayList<String> nextPage = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    ListView mListview;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.adLayout)
    RelativeLayout adBannerLayout;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.adbertADView)
    AdbertLoopADView adbertView;
    @BindView(R.id.ad)
    com.clickforce.ad.AdView clickforceAd;
    @BindView(R.id.fbImg)
    ImageView mFbImageView;
    @BindView(R.id.adView_page)
    AdView mPageAdView;
    @BindView(R.id.useraccount)
    TextView mUserAccountTextView;
    @BindView(R.id.userid)
    TextView mUserIdTextView;
    private Context context;
    @BindView(R.id.timetetx)
    TextView mStockTimeText;
    @BindView(R.id.adViewContainer)
    RelativeLayout adViewContainer;
    private  com.facebook.ads.AdView mFbAdView;
    @BindView(R.id.loginbutton)
    Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mfiebaselibsClass = new MfiebaselibsClass(this, MainActivity.this);
        mfiebaselibsClass.userLoginCheck();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkNetWork();
        toolbar.setTitle(getResources().getString(R.string.activty_main_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setAdmobBanner();
        setPageAdView();
        setVponBanner();
        setAdbertBanner();
        setClickForce();
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;
        }

        setNewsData();
        mAdapter = new MyAdapter(newlist);
        mListview.setAdapter(mAdapter);
        setmFbAdView();
//        test("2344");
//        test2("2344");
//        getNewDetil();
//        setStockData();
//        setWarningStock();
        getStockTime();
        setRewardedVideoAd();
    }
    private void setRewardedVideoAd(){
        MobileAds.initialize(this,
                "ca-app-pub-7019441527375550~1705354228");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd("ca-app-pub-7019441527375550/1968113408",
                new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {

                // Reward the user.
                Log.d(TAG, "onRewarded: "+ "  amount: " +
                        reward.getAmount());

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                Log.d(TAG, "onRewardedVideoAdFailedToLoad: "+errorCode);
            }

            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }
        });

    }


    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private  void setmFbAdView(){

        adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        mFbAdView = new com.facebook.ads.AdView(this, "383959162037550_415939618839504", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                adViewContainer.addView(mFbAdView);
        mFbAdView.loadAd();

    }
    /**
     * 設定 基本會員中心資料
     */
    private void setMemberData(String Key, String name,
                               String photo) {
        HashMap<String, String> memberMap = new HashMap<>();
        memberMap.put(MemberData.KEY_ID, Key);
        memberMap.put(MemberData.KEY_NAME, name);
        memberMap.put(MemberData.KEY_PHOTO, photo);
        memberMap.put(MemberData.KEY_POINT,"100");
        memberMap.put(MemberData.KEY_MEMBERLV,MemberData.MEMBER_LV_1);
        mfiebaselibsClass.setFireBaseDB(MemberData.KEY_URL, Key, memberMap);



    }
    /**
     * 設定 會員中心資料
     */

    private void setMemberData(String Key, String firstname, String lastname,
                               String email, String birthday, String gender,
                               String memberlv, String photo, String location) {
        HashMap<String, String> memberMap = new HashMap<>();
        memberMap.put(MemberData.KEY_ID, Key);
        memberMap.put(MemberData.KEY_FIRST_NAME, firstname);
        memberMap.put(MemberData.KEY_LAST_NAME, lastname);
        memberMap.put(MemberData.KEY_EMAIL, email);
        memberMap.put(MemberData.KEY_BIRTHDAY, birthday);
        memberMap.put(MemberData.KEY_GENDER, gender);
        memberMap.put(MemberData.KEY_MEMBERLV, memberlv);
        memberMap.put(MemberData.KEY_PHOTO, photo);
        memberMap.put(MemberData.KEY_LOCATION, location);


        mfiebaselibsClass.setFireBaseDB(MemberData.KEY_URL + Key, Key, memberMap);


    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if(resultCode == RESULT_OK){
            if(resultCode == LOGINSTATE){
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
        int state = MySharedPrefernces.getUserLoginState(context);
        if(state==1){
            FacebookManager.checkFbState(context,mFbImageView,mUserIdTextView,mUserAccountTextView);

        }else if(state==2){
            LineLoginManager.checkState(context,mFbImageView,mUserIdTextView,mUserAccountTextView);

        }
        String id = MySharedPrefernces.getUserId(context);
        String name = MySharedPrefernces.getUserName(context);
        String photo = MySharedPrefernces.getUserPhoto(context);
        setMemberData(id,name,photo);
        Log.d(TAG, "onResume: "+id);
        if(!id.equals("")){
            mLoginButton.setText("已登入");
        }else {
            mLoginButton.setText("登入");
            mFbImageView.setImageDrawable(null);
            mUserAccountTextView.setText("");
            mUserIdTextView.setText("");

        }
        Log.d(TAG, "onResume: "+MemberData.KEY_URL+"/"+id);
        mfiebaselibsClass.getFirebaseDatabase(MemberData.KEY_URL+"/"+id,id);

    }
    @OnClick(R.id.fbImg)
    public  void mRewardedVideoAdClick(){
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
    @OnClick(R.id.nav_share)
    public void shareTo(){
        MyApi.shareTo(context);

    }

    @OnClick(R.id.loginbutton)
    public  void setLoginActivity(View v){
        String id = MySharedPrefernces.getUserId(context);
        if(!id.equals("")){
                startActivity(MemberCenterActivity.class);
        }else {

            startActivity(LoginActivity.class);

        }


    }

    @OnClick(R.id.nav_camera)
    public void setBuyAndSellActivity() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(BuyAndSellActivity.class);
    }

    @OnClick(R.id.nav_stockprice)
    public void setStockPiceActiviy() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(QueryStockPriceActivity.class);

    }

    @OnClick(R.id.nav_gallery)
    public void Click() {
        if (!checkUserId(context)) {
            return;
        }

        startActivity(new Intent(this, ProfitAndLossActvity.class));
    }
    @OnClick(R.id.nav_member)
    public void toMemberCenter(){
        String id = MySharedPrefernces.getUserId(context);
        if(!id.equals("")){
            startActivity(MemberCenterActivity.class);
        }else {
            startActivity(LoginActivity.class);

        }
    }
    @OnClick(R.id.nav_stock_share)
    public void shareStockActivity() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(ShareStockNumberActivity.class);
    }

    @OnClick(R.id.nav_manage)
    public void PayActivity() {
        startActivity(new Intent(this, InAppBillingActivity.class));
    }

    @OnClick(R.id.nav_calculate)
    public void calculateActivity() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(CalculateActivity.class);
    }

    //    @OnClick(R.id.messenger_send_button)
//    public void sendMessageButton() {
//        onMessengerButtonClicked();
//
//    }
    @OnClick(R.id.nav_price)
    public void setStockPriceActivity() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(StockValueAddedRateActivity.class);
    }

    @OnClick(R.id.zerostock)
    public void zeroStockActivity() {
        if (!checkUserId(context)) {
            return;
        }
        startActivity(ZeroStockActivity.class);
    }

    @OnClick(R.id.rightbutton)
    public void nextPageButton() {
        if (nextPage.size() >= 2) {
            setNewsData(nextPage.get(1), true);
        } else {
            setNewsData(nextPage.get(0), true);

        }
    }

    @OnClick(R.id.liftbutton)
    public void returnButton() {
        if (nextPage.size() >= 2) {
            setNewsData(nextPage.get(1), false);
        } else {
            showToast("已經是最後一頁！！");
            return;

        }
    }

    @OnItemClick(R.id.listView)
    public void listViewOnItemClick(int i) {
        Bundle b = new Bundle();
        b.putString("url", newlist.get(i).getNewsDetail());
        startActivity(NewDetailActivity.class, b);


    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setVponBanner() {
        //get your layout view for Vpon banner
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
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setPageAdView() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mPageAdView.loadAd(adRequest);
    }

    private void setAdbertBanner() {
        adbertView.setMode(AdbertOrientation.NORMAL);
        adbertView.setExpandVideo(ExpandVideoPosition.BOTTOM);
        adbertView.setFullScreen(false);
        adbertView.setBannerSize(AdSize.BANNER);
        adbertView.setAPPID("20170619000001", "90cebe8ef120c8bb6ac2ce529dcb99af");
        adbertView.start();
    }

    private void setClickForce() {


        clickforceAd.getAd(6120, 320, 50, 0.8, 30);

        //Ad Load Callback
        clickforceAd.setOnAdViewLoaded(new AdViewLinstener() {
            @Override
            public void OnAdViewLoadFail() {
                Log.d(TAG, "請求廣告失敗");
            }

            @Override
            public void OnAdViewLoadSuccess() {
                //顯示banner廣告
                clickforceAd.show();
            }
        });

        clickforceAd.outputDebugInfo = true;

    }

    //
//    private void onMessengerButtonClicked() {
//        // The URI can reference a file://, content://, or android.resource. Here we use
//        // android.resource for sample purposes.
//        Uri uri =
//                Uri.parse("android.resource://com.jackpan.stockcomputer/" + R.drawable.tree);
//        Log.d(TAG, "onMessengerButtonClicked: "+uri);
//
//        // Create the parameters for what we want to send to Messenger.
//        ShareToMessengerParams shareToMessengerParams =
//                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
//                        .setMetaData("{ \"image\" : \"tree\" }")
//                        .build();
//        Log.d(TAG, "onMessengerButtonClicked: "+mPicking);
//
//        if (mPicking) {
//            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
//            // the content to Messenger.
//            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
//        } else {
//            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
//            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
//            // to be upgraded, this will direct the user to the play store.
//            MessengerUtils.shareToMessenger(
//                    this,
//                    REQUEST_CODE_SHARE_TO_MESSENGER,
//                    shareToMessengerParams);
//        }
//    }
    private void test2(String number) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//                    Log.d(TAG, "run: "+" "+number+".htm");
                    Document doc = Jsoup.connect("https://goodinfo.tw/StockInfo/StockDetail.asp?STOCK_ID=2344").get();
                    Element t = doc.select("tr[align=center][height=26px][bgcolor=#e7f3ff]").get(0);
//                    Log.d(TAG, "run: "+ t.select("td").get(0).text());
//                    for (Element td : t.select("td")) {
//                        Log.d(TAG, "run: "+td.text());
//                    }
                    Element e = doc.select("tr[align=center][height=26px][bgcolor=white]").get(0);
//                    Log.d(TAG, "run: "+ e.select("td").get(0).text());
//                    for (Element td : e.select("td")) {
//
//                        Log.d(TAG, "run: "+td.text());
//
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


    }

    private void setWarningStock() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("http://jow.win168.com.tw/Z/ZE/ZEW/ZEW.djhtm").get();
                    for (int i = 2; i < doc.select("table[class=t01]>tbody>tr").size(); i++) {
                        for (Element script : doc.select("table[class=t01]>tbody>tr").get(i).getElementsByTag("script")) {
                            MyApi.stockStringReplace(script.html().toString());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


    }

    private void getStockTime() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/").get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mStockTimeText.setText(doc.select("td[width=270][class=ssbody]").text());

                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }
        }.start();
    }

    private void setStockData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("http://www.tse.com.tw/exchangeReport/STOCK_DAY?response=html&date=20180128&stockNo=3019").get();
//                    for (Element element : doc.select("table>tbody>tr")) {
//                        Log.d(TAG, "run: "+element.text());
//
//
//                    }
                    Log.d(TAG, "run: " + doc.select("table>tbody>tr").size());
                    for (int i = 0; i < 1; i++) {
//                        for (int i1 = 0; i1 < doc.select("table>tbody>tr").get(i).select("td").size(); i1++) {
//                            Log.d(TAG, "run: "+doc.select("table>tbody>tr").get(i).select("td").get(i));
//                        }
                        for (Element td : doc.select("table>tbody>tr").get(0).select("td")) {
                            Log.d(TAG, "run: " + td.text());
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }

    private void setNewsData() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/news_list/url/d/e/N3.html?q=&pg=1").get();
                    for (Element mtext : doc.getElementsByClass("mtext")) {
                        nextPage.add(mtext.attr("onClick").toString());
                    }
                    if (!nextPage.get(0).equals("")) {


                        for (Element table : doc.select("table#newListContainer")) {

                            for (Element tbody : table.select("tbody")) {


                                for (Element tr : tbody.select("tr")) {

                                    for (Element element : tr.select("td[valign=top]>a.mbody")) {
                                        NewsData n = new NewsData();
                                        n.setNewsTitle(element.text());
                                        n.setNewsDetail(element.getElementsByTag("a").attr("href").toString());
                                        newlist.add(n);
                                    }
//                                        NewsData n = new NewsData();
//
//                                        n.setNewsTitle(element.text());
//                                        newlist.add(n);
//
//                                    for (Element td : tr.select("td[valign=top]>span.mbody")) {
//                                        Log.d(TAG, "b: "+td.text());
//                                        n.setNewsDetail(td.text());
//
//                                    }


                                    if (newlist.size() >= 10) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                mAdapter.notifyDataSetChanged();

                                                mProgressDialog.dismiss();

                                            }
                                        });

                                        return;
                                    }
                                }


                            }

                        }


                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: " + e.getMessage());
                }


            }
        }.start();
    }

    String next;
    String ntx;

    private void setNewsData(String s, boolean isNextPage) {

        newlist.clear();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    if (nextPage.size() >= 2) {
                        if (isNextPage == true) {
                            next = nextPage.get(1).replace("location=", "");
                            ntx = next.replace("\'", "");
                        } else {
                            next = nextPage.get(0).replace("location=", "");
                            ntx = next.replace("\'", "");
                        }


                    } else {
                        next = nextPage.get(0).replace("location=", "");
                        ntx = next.replace("\'", "");
                    }

                    Log.d(TAG, "run: " + "https://tw.stock.yahoo.com/news_list/url/d/e/N3.html" + ntx + "");

                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/news_list/url/d/e/N3.html" + ntx + "").get();

                    nextPage.clear();
                    for (Element mtext : doc.getElementsByClass("mtext")) {
                        nextPage.add(mtext.attr("onClick").toString());


                    }

                    if (!nextPage.get(0).equals("")) {
                        for (Element table : doc.select("table#newListContainer")) {

                            for (Element tbody : table.select("tbody")) {
                                for (Element tr : tbody.select("tr")) {
                                    Log.d(TAG, "run: " + tr.select("td[valign=top]>a.mbody").size());
                                    for (int i = 0; i < tr.select("td[valign=top]>a.mbody").size(); i++) {
                                        NewsData mNewsData = new NewsData();
                                        mNewsData.setNewsTitle(tr.select("td[valign=top]>a.mbody").get(i).text());
                                        mNewsData.setNewsDetail(tr.select("td[valign=top]>a.mbody").get(i).getElementsByTag("a").attr("href").toString());

                                        newlist.add(mNewsData);
                                        if (newlist.size() >= 10) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    mAdapter.notifyDataSetChanged();


                                                }
                                            });

                                            return;
                                        }
                                    }


                                }
                            }
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: " + e.getMessage());
                }


            }
        }.start();
    }

    private void getNewDetil(String s, NewsData d) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    Document doc = Jsoup.connect(s).get();
                    for (Element element : doc.select("table[class=yui-text-left yui-table-wfix ynwsart]>tbody>tr>td>span")) {
//                        Log.d(TAG, "getNewDetil: "+element.text());
                    }
                    for (Element element : doc.select("p")) {
//                        Log.d(TAG, "getNewDetil: "+element.toString());
//                        Log.d(TAG, "getNewDetil: "+element.text());
                        if (!element.text().equals("")) {
//                            newlist.add(mNewsData.setNewsDetail(element.text()));
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }



    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
        } else {
        }
    }

    @Override
    public void getDatabaseData(Object o) {
        Gson gson = new Gson();
        String s = gson.toJson(o);
        Log.d(TAG, "firebasedata: "+s);

    }

    @Override
    public void getDeleteState(boolean b, String s, Object o) {

    }

    @Override
    public void createUserState(boolean b) {

    }

    @Override
    public void useLognState(boolean b) {
        Log.d(TAG, "useLognState: " + b);

    }

    @Override
    public void getuseLoginId(String s) {
        Log.d(TAG, "getuseLoginId: " + s);

    }

    @Override
    public void getuserLoginEmail(String s) {

    }

    @Override
    public void resetPassWordState(boolean b) {

    }

    @Override
    public void getFireBaseDBState(boolean b, String s) {
        Log.d(TAG, "getFireBaseDBState: "+s);

    }

    @Override
    public void getFirebaseStorageState(boolean b) {

    }

    @Override
    public void getFirebaseStorageType(String s, String s1) {

    }

    @Override
    public void getsSndPasswordResetEmailState(boolean b) {

    }

    @Override
    public void getUpdateUserName(boolean b) {

    }

    @Override
    public void getUserLogoutState(boolean b) {

    }

    public class MyAdapter extends BaseAdapter {
        private ArrayList<NewsData> mDatas;

        public MyAdapter(ArrayList<NewsData> datas) {
            mDatas = datas;
        }

        public void updateData(ArrayList<NewsData> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            NewsData data = mDatas.get(position);
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.layout_homepage, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder.mTitleTextView.setText(data.getNewsTitle() + "");
            viewHolder.mDetailTextView.setText("(詳全文...)");

            return convertView;
        }

    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView mTitleTextView;
        @BindView(R.id.detail)
        TextView mDetailTextView;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
