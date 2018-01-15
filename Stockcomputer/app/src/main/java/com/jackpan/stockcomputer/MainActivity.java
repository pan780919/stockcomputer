package com.jackpan.stockcomputer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adbert.AdbertListener;
import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.ExpandVideoPosition;
import com.clickforce.ad.Listener.AdViewLinstener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.jackpan.libs.mfirebaselib.MfiebaselibsClass;
import com.jackpan.libs.mfirebaselib.MfirebaeCallback;
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity;
import com.jackpan.stockcomputer.Activity.CalculateActivity;
import com.jackpan.stockcomputer.Activity.ShareStockNumberActivity;
import com.jackpan.stockcomputer.Data.MyApi;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAppCompatActivity implements MfirebaeCallback {
    private static final String TAG = "MainActivity";
    private VpadnBanner vponBanner = null;
    //Vpon TODO:  Banner ID
    private String bannerId = "8a8081824eb5519a014eca83ab981d91";
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> newlist = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private MfiebaselibsClass mfiebaselibsClass;
    private ProfileTracker profileTracker;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.newslistview)
    ListView listView;
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
    @BindView(R.id.fbloginbutton)
    LoginButton mFbLoginButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        context = this;
        mfiebaselibsClass = new MfiebaselibsClass(this, MainActivity.this);
        mfiebaselibsClass.userLoginCheck();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        checkNetWork();
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
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newlist);
        listView.setAdapter(listAdapter);
        fbLogin();
//        test("2344");
        test2("2344");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //臉書登入
    private void fbLogin() {
        List<String> PERMISSIONS_PUBLISH = Arrays.asList("public_profile", "email", "user_friends","user_location","user_birthday", "user_likes");
        mFbLoginButton.setReadPermissions(PERMISSIONS_PUBLISH);
        mFbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
                setUsetProfile();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoginActivity", object.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                setLogger(error.getMessage());


            }

        });


    }

    private void handleFacebookAccessToken(AccessToken token) {


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        auth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void setUsetProfile() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (oldProfile != null) {
                    //登出後
//                    fbName.setText("");
                    mFbImageView.setImageBitmap(null);

                }

                if (currentProfile != null) {
                    //登入
//                    fbName.setText(currentProfile.getName());
                    MyApi.loadImage(String.valueOf(currentProfile.getProfilePictureUri(150, 150)), mFbImageView,context);

                }

            }
        };
        profileTracker.startTracking();
        if (profileTracker.isTracking()) {
            if (Profile.getCurrentProfile() == null) return;
            if (Profile.getCurrentProfile().getProfilePictureUri(150, 150) != null) {
                MyApi.loadImage(String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(150, 150)), mFbImageView, context);

            }

        } else
            Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "no");

    }


    @OnClick(R.id.nav_gallery)
    public void Click() {
        startActivity(new Intent(this, ProfitAndLossActvity.class));
    }

    @OnClick(R.id.nav_stock_share)
    public void shareStockActivity() {
        startActivity(ShareStockNumberActivity.class);
    }

    @OnClick(R.id.nav_manage)
    public void PayActivity() {
        startActivity(new Intent(this, InAppBillingActivity.class));
    }

    @OnClick(R.id.nav_calculate)
    public void calculateActivity() {
        startActivity(CalculateActivity.class);
    }

    @OnClick(R.id.messenger_send_button)
    public void sendMessageButton() {
        onMessengerButtonClicked();

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

    private void setAdbertBanner() {
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
    private void test2(String number){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
//                    Log.d(TAG, "run: "+" "+number+".htm");
                    Document doc = Jsoup.connect("https://goodinfo.tw/StockInfo/StockDetail.asp?STOCK_ID=2344").get();
                    Element t = doc.select("tr[align=center][height=26px][bgcolor=#e7f3ff]").get(0);
                    Log.d(TAG, "run: "+ t.select("td").get(0).text());
//                    for (Element td : t.select("td")) {
//                        Log.d(TAG, "run: "+td.text());
//                    }
                    Element e = doc.select("tr[align=center][height=26px][bgcolor=white]").get(0);
                    Log.d(TAG, "run: "+ e.select("td").get(0).text());
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


    private void test(String number){
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<String> price = new ArrayList<String>();
                try {
                    Log.d(TAG, "run: "+"https://tw.stock.yahoo.com/d/s/dividend_"+number+".html");
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/d/s/dividend_"+number+".html").get();
                    for (Element table : doc.select("table[width=630][align=center]>tbody")) {

                        for (Element element : table.select("table[cellspacing=1]")) {
                            for (Element element1 : element.select("tbody")) {
                                // 抓抬頭
//                                Log.d(TAG, "run: "+element1.select("tr>td.ttt").size());
                                for (Element element2 : element1.select("tr>td.ttt")) {
//                                    Log.d(TAG, "run: "+element2.text());

                                }
                                // 抓內容
                                for (Element element2 : element1.select("tr[bgcolor=#FFFFFF]")) {
//                                    Log.d(TAG, "run: "+element2.select("td").size());
                                    for (Element td : element2.select("td")) {
//                                        Log.d(TAG, "run: "+td.text());
                                        price.add(td.text());
                                    }
                                }


                            }
                        }
                    }
                    Log.d(TAG, "run: "+price.get(1));
//                    for (String s : price) {
//                        Log.d(TAG, "run: "+s);
//                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }


    private void setNewsData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("https://tw.stock.yahoo.com/news_list/url/d/e/N3.html?q=&pg=1").get();
                    for (Element table : doc.select("table#newListContainer")) {
                        for (Element tbody : table.select("tbody")) {
                            for (Element tr : tbody.select("tr")) {
                                for (Element td : tr.select("td[valign=top]>a.mbody")) {
//                                    Log.d(TAG, "run: " + td.text());
//                                    Log.d(TAG, "run: " + td.getElementsByTag("a").attr("href").toString());
                                    newlist.add(td.text());
                                    if (newlist.size() >= 10) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                listAdapter.notifyDataSetChanged();
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


    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.d(TAG, profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            Log.d(TAG, "getFacebookData: "+object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            Log.d(TAG, "getFacebookData: "+object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            Log.d(TAG, "getFacebookData: "+object.getString("email"));

            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            Log.d(TAG, "getFacebookData: "+object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            Log.d(TAG, "getFacebookData: "+MyApi.birthdayToTimeStamp(object.getString("birthday")));
            MyApi.DateComparison(System.currentTimeMillis(),System.currentTimeMillis());

            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            Log.d(TAG, "getFacebookData: "+object.getJSONObject("location").getString("name"));
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
        } else {
        }
    }

    @Override
    public void getDatabaseData(Object o) {

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
}
