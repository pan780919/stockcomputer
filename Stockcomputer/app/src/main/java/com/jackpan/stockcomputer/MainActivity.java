package com.jackpan.stockcomputer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.clickforce.ad.Listener.AdViewLinstener;
import com.facebook.AccessToken;
import com.facebook.ads.InterstitialAd;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import com.jackpan.stockcomputer.Kotlin.CheckVersionActivity;
import com.jackpan.stockcomputer.Kotlin.ConceptActivity;
import com.jackpan.stockcomputer.Kotlin.DividendActivity;
import com.jackpan.stockcomputer.Kotlin.FgBuyActivity;
import com.jackpan.stockcomputer.Kotlin.LoginActivity;
import com.jackpan.stockcomputer.Kotlin.MemberCenterActivity;
import com.jackpan.stockcomputer.Kotlin.NewDetailActivity;
import com.jackpan.stockcomputer.Kotlin.QueryStockPriceActivity;
import com.jackpan.stockcomputer.Kotlin.QuotesActivity;
import com.jackpan.stockcomputer.Kotlin.SeBuyActivity;
import com.jackpan.stockcomputer.Kotlin.StockValueAddedRateActivity;
import com.jackpan.stockcomputer.Kotlin.WorldIdxActivity;
import com.jackpan.stockcomputer.Kotlin.ZeroStockActivity;
import com.jackpan.stockcomputer.Manager.FacebookManager;
import com.jackpan.stockcomputer.Manager.LineLoginManager;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    private InterstitialAd interstitialAd;

    private static final int LOGINSTATE = 0;
    ArrayList<String> memberList = new ArrayList<>();

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
    AdView adbertView;
//    AdbertLoopADView adbertView;

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
    private com.facebook.ads.AdView mFbAdView;
    @BindView(R.id.loginbutton)
    Button mLoginButton;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            test4();
            handler.postDelayed(this, 1000 * 30);// 间隔30秒
        }

    };

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
        getStockTime();
//        getbuy();
//        getStop();
        interstitialAd = new InterstitialAd(this, "383959162037550_522507098182755");
        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
        //        getbuy();
//        getStop();
//
//
//        test9();
//        handler.postDelayed(runnable, 1000 * 30);
//        test10();
//        setWarningStock();
//        getStockSelect();
//        getStockSelectDetail();
        test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void getStop() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://histock.tw/holiday.aspx?id=TWSE").get();
                    for (Element element : doc.select("table[cellpadding=0][cellspacing=0]>tbody")) {
                        for (Element tr : element.select("tr")) {
                            for (Element element1 : tr.select("tr>tbody[class=tbGV][cellspacing=0]")) {
                                Log.d(TAG, "element1: " + element1.text());
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    private void setmFbAdView() {

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
        memberMap.put(MemberData.KEY_POINT, "100");
        memberMap.put(MemberData.KEY_MEMBERLV, MemberData.MEMBER_LV_1);
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
        if (resultCode == RESULT_OK) {
            if (resultCode == LOGINSTATE) {
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        int state = MySharedPrefernces.getUserLoginState(context);
        if (state == 1) {
            FacebookManager.checkFbState(context, mFbImageView, mUserIdTextView, mUserAccountTextView);

        } else if (state == 2) {
            LineLoginManager.checkState(context, mFbImageView, mUserIdTextView, mUserAccountTextView);

        }
        String id = MySharedPrefernces.getUserId(context);
        String name = MySharedPrefernces.getUserName(context);
        String photo = MySharedPrefernces.getUserPhoto(context);
        setMemberData(id, name, photo);
        if (!id.equals("")) {
            mLoginButton.setText("已登入");
            mfiebaselibsClass.getFirebaseDatabase(MemberData.KEY_URL + "/" + id, id);

        } else {
            mLoginButton.setText("登入");
            mFbImageView.setImageDrawable(null);
            mUserAccountTextView.setText("");
            mUserIdTextView.setText("");

        }

    }

    @OnClick(R.id.nav_word)
    public void setWorldIdxActivity() {
        startActivity(WorldIdxActivity.class);
    }

    @OnClick(R.id.nav_sebuy)
    public void seBuyActivity() {
        startActivity(SeBuyActivity.class);

    }

    @OnClick(R.id.nav_quotes)
    public void quotesActivity() {
        startActivity(QuotesActivity.class);

    }

    @OnClick(R.id.nav_fgbuy)
    public void fgBuyActivit() {
        startActivity(FgBuyActivity.class);

    }

    @OnClick(R.id.fbImg)
    public void mRewardedVideoAdClick() {

    }

    @OnClick(R.id.nav_share)
    public void shareTo() {
        MyApi.shareTo(context);

    }

    @OnClick(R.id.loginbutton)
    public  void setLoginActivity(View v){
        String id = MySharedPrefernces.getUserId(context);
        if (!id.equals("")) {
            startActivity(MemberCenterActivity.class);
        } else {

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
    public void toMemberCenter() {
        String id = MySharedPrefernces.getUserId(context);
        if (!id.equals("")) {
            startActivity(MemberCenterActivity.class);
        } else {
            startActivity(LoginActivity.class);

        }
    }

    @OnClick(R.id.nav_check)
    public void checkVersion() {
        startActivity(CheckVersionActivity.class);
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

    @OnClick(R.id.nav_dividend)
    public void DividendActivity() {
        startActivity(DividendActivity.class);
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
    @OnClick(R.id.nav_concept)
        public void setConceptActivity(){
            startActivity(ConceptActivity.class);
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

        AdRequest adRequest = new AdRequest.Builder().build();
        adbertView.loadAd(adRequest);
//        adbertView.setMode(AdbertOrientation.NORMAL);
//        adbertView.setExpandVideo(ExpandVideoPosition.BOTTOM);
//        adbertView.setFullScreen(false);
//        adbertView.setBannerSize(AdSize.BANNER);
//        adbertView.setAPPID("20170619000001", "90cebe8ef120c8bb6ac2ce529dcb99af");
//        adbertView.start();
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
    private void test4() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("11");
        progressDialog.setMessage("22");
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/us/worldidx.php").get();
                    for (Element element : doc.select("table[border=0][cellpadding=4][cellspacing=1][width=100%]")) {
                        for (int i = 3; i < 15; i++) {
                                Log.d(TAG, "worldidx: "+element.select("tr").get(i).text());
                        }

                        for (int i = 16; i < 24; i++) {
//                            Log.d(TAG, "worldidx: " + element.select("tr").get(i).text());
                        }
//                            for (Element tr : element.select("tr")) {
//                                Log.d(TAG, "run: "+tr.text());
//                            }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });

                    } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void test11() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/s/major_2368.html").get();
                    for (Element element : doc.select("table[border=0][cellspacing=0][cellpadding=0][align=center]")) {
                        for (Element element1 : element.select("table[border=0][width=100%][cellpadding=2][height=23]")) {
                            Log.d(TAG, "run: " + element1.text());
                        }
//                        for (Element element2 : element.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]")) {
//                            Log.d(TAG, "run: "+element2.text());
////                            for (Element td : element2.select("td")) {
////                                Log.d(TAG, "run: "+td.text());
////                            }
//                        }
//
                        for (int i = 0; i < element.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").size(); i++) {
                            for (Element tr : element.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").get(i).select("tr")) {
                                Log.d(TAG, "run: " + tr.text());
                            }
                        }
                    }

                    } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void test10() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://fund.bot.com.tw/z/ze/zeb/zeb.djhtm").get();
                    for (Element element : doc.select("table[border=0][cellspacing=1][cellpadding=1]")) {
                        Log.d(TAG, "run: " + element.select("tr").size());
                        for (int i = 1; i < element.select("tr").size(); i++) {
                            Log.d(TAG, "run: " + element.select("tr").get(i).text());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void test9() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://histock.tw/stock/broker8.aspx").get();
                    for (Element ctname : doc.select("div.ctname>h3")) {
                        Log.d(TAG, "run: " + ctname.text());
                    }
                    for (Element ul : doc.select("div.grid-body.p7.mb10")) {
//                        Log.d(TAG, "run: "+ul.text());
                        for (Element element : ul.select("ul")) {
//                            Log.d(TAG, "run: "+ul.select("ul").size());
                            for (int i = 0; i < element.select("li").size() - 1; i++) {
//                                Log.d(TAG, "run: "+element.select("li").get(i).text());
//                                Log.d(TAG, "run: "+i+"");
//                                Log.d(TAG, "run:"+element.select("li").get(i).text());
                                for (Element element1 : element.select("li").get(i).select("span")) {
                                    if (!element1.text().trim().equals("")) {
                                        Log.d(TAG, "run: " + element1.text().trim());

                                    }


                                }

                            }

                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }


    private void test3() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/major.html").get();
                    for (int i = 1; i < doc.select("table[width=650][border=0][cellspacing=0][cellpadding=0][align=center]").size() - 1; i++) {
//                            Log.d(TAG, "run: "+doc.select("table[width=650][border=0][cellspacing=0][cellpadding=0][align=center]").get(i).text());
                        for (int i1 = 1; i1 < doc.select("table[width=650][border=0][cellspacing=0][cellpadding=0][align=center]").get(i).select("tr").size(); i1++) {
                            Log.d(TAG, "run: " + doc.select("table[width=650][border=0][cellspacing=0][cellpadding=0][align=center]").get(i).select("tr").get(i1).text());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void test2() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/rank.php?t=pri&e=tse").get();
                    for (Element element : doc.select("table[border=0][cellspacing=1][cellpadding=3]")) {
                        for (Element tr : element.select("tr")) {
                            Log.d(TAG, "run: " + tr.text());


                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


    }

    private void test() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/credit.html").get();

                    for (Element element : doc.select("table[border=0][cellspacing=1][cellpadding=3]")) {
                        Log.d(TAG, "run: " + element.select("tr").size());
                        for (int i =1; i < element.select("tr").size()-1; i++) {
                            Log.d(TAG, "run: " + element.select("tr").get(i).text());
                        }
                    }
                    for (Element element : doc.select("table[border=0][cellspacing=7][cellpadding=2]")) {
                        for (Element tr : element.select("tr")) {
                            for (Element p : tr.getElementsByTag("p")) {
                                Elements title = p.select("[src]");

                                for (Element src : title) {
                                    if (src.tagName().equalsIgnoreCase("img")) {
                                    }
                                }
                            }
                        }
                    }
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
                    String url = "http://www.twse.com.tw/announcement/punish?response=html&startDate=20180801&endDate=20180817&stockNo=&sortKind=&querytype=&selectType=&proceType=&remarkType=";
                    String ur2 = "http://www.twse.com.tw/announcement/notice?response=html&startDate=20180801&endDate=20180817&stockNo=&sortKind=DATE&querytype=1&selectType=";
                    Document doc = Jsoup.connect(url).get();
                    Log.d(TAG, "run: " + doc.getElementsByTag("h2").text());
                    for (Element tr : doc.select("tr")) {
//                            Log.d(TAG, "run: "+tr.text());
                        for (Element td : tr.select("td")) {
                            Log.d(TAG, "run: " + td.text());

                        }
                    }
//                        Document doc = Jsoup.connect("http://jow.win168.com.tw/Z/ZE/ZEW/ZEW.djhtm").get();
//                        for (int i = 2; i < doc.select("table[class=t01]>tbody>tr").size(); i++) {
//                            for (Element script : doc.select("table[class=t01]>tbody>tr").get(i).getElementsByTag("script")) {
//                                MyApi.stockStringReplace(script.html().toString());
//                            }
//                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


    }

    private void getStockSelect() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/h/getclass.php#table7").get();
                    Log.d(TAG, "run: "+doc.select("table[border=0][cols=2][cellspacing=0][cellpadding=7]").size());
                    for (Element tr : doc.select("table[border=0][cols=2][cellspacing=0][cellpadding=7]").get(3).select("tr")) {
                        for (Element td : tr.select("td")) {
                            Log.d(TAG, "run: "+td.text());
                            String url = td.select("a").attr("href");
                            Log.d(TAG, "run: "+url);
                        }
                    }

                } catch (Exception e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }
        }.start();
    }
    private void getStockSelectDetail() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/s/list2.php?c=%AAo%B9q%A8%AE&rr=0.35693600%201535090033").get();
                    for (int i = 0; i < doc.select("table[border=1][cellspacing=0][cellpadding=2]").size(); i++) {
                       Elements element =  doc.select("table[border=1][cellspacing=0][cellpadding=2]");
                        for (Element tr : element.select("tr")) {
                            Log.d(TAG, "run: "+tr.text());
                        }

                    }


                } catch (Exception e) {
                    Log.d(TAG, Log.getStackTraceString(e));
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


    private void getbuy() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/fgbuy_tse_w.html").get();
                    for (Element element : doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]")) {
                        for (Element tr : element.select("tr")) {
                            Log.d(TAG, "run: " + tr.text());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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
        memberList.add(s);
        if (memberList.size() >= 5) {
            MySharedPrefernces.saveUserId(context, memberList.get(0));
            MySharedPrefernces.saveUserlv(context, memberList.get(1));
            MySharedPrefernces.saveUserName(context, memberList.get(2));
            MySharedPrefernces.saveUserPhoto(context, memberList.get(3));
            MySharedPrefernces.saveUserPoint(context, memberList.get(4));
            memberList.clear();
        }

    }

    @Override
    public void getDeleteState(boolean b, String s, Object o) {

    }

    @Override
    public void createUserState(boolean b) {

    }

    @Override
    public void useLognState(boolean b) {

    }

    @Override
    public void getuseLoginId(String s) {

    }

    @Override
    public void getuserLoginEmail(String s) {

    }

    @Override
    public void resetPassWordState(boolean b) {

    }

    @Override
    public void getFireBaseDBState(boolean b, String s) {

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
