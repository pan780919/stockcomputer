package com.jackpan.stockcomputer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity;
import com.jackpan.stockcomputer.Activity.CalculateActivity;
import com.jackpan.stockcomputer.Activity.ShareStockNumberActivity;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAppCompatActivity{
    private static final String TAG = "MainActivity";
    private VpadnBanner vponBanner = null;
    //Vpon TODO:  Banner ID
    private String bannerId = "8a8081824eb5519a014eca83ab981d91" ;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> newlist= new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.newslistview) ListView listView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.adLayout)RelativeLayout adBannerLayout;
    @BindView(R.id.adView)AdView mAdView;
    @BindView(R.id.adbertADView)AdbertLoopADView adbertView;
    @BindView(R.id.ad)com.clickforce.ad.AdView clickforceAd;
    @BindView(R.id.fbImg)
    ImageView mFbImageView;
    @BindView(R.id.fbloginbutton)
    LoginButton mFbLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
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
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,newlist);
        listView.setAdapter(listAdapter);
        fbLogin();


    }

    //臉書登入
    private void fbLogin() {
        List<String> PERMISSIONS_PUBLISH = Arrays.asList("public_profile", "email", "user_friends");
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
                        Log.i("LoginActivity", response.toString());
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
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");

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
                    fbImg.setImageBitmap(null);

                }

                if (currentProfile != null) {
                    //登入
//                    fbName.setText(currentProfile.getName());
                    loadImage(String.valueOf(currentProfile.getProfilePictureUri(150, 150)), fbImg, LoginActivity.this);
                    MySharedPrefernces.saveUserPic(LoginActivity.this,String.valueOf(currentProfile.getProfilePictureUri(150, 150)));

                }

            }
        };
        profileTracker.startTracking();
        if (profileTracker.isTracking()) {
            Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "yes");
            if (Profile.getCurrentProfile() == null) return;

//            if(Profile.getCurrentProfile().getName()!=null)	fbName.setText(Profile.getCurrentProfile().getName());
            if (Profile.getCurrentProfile().getProfilePictureUri(150, 150) != null)
                loadImage(String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(150, 150)), fbImg, LoginActivity.this);
        } else
            Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "no");

    }


    @OnClick(R.id.nav_gallery)
    public void Click(){
        startActivity(new Intent(this,ProfitAndLossActvity.class));
    }
    @OnClick(R.id.nav_stock_share)
    public void shareStockActivity(){
        startActivity(ShareStockNumberActivity.class);
    }
    @OnClick(R.id.nav_manage)
    public  void PayActivity(){
        startActivity(new Intent(this, InAppBillingActivity.class));
    }
    @OnClick(R.id.nav_calculate)
    public void calculateActivity(){
        startActivity(CalculateActivity.class);
    }

    @OnClick(R.id.messenger_send_button)
    public void sendMessageButton(){
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

    private  void setVponBanner(){
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
    private void setAdbertBanner(){
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


        clickforceAd.getAd(6120,320,50,0.8,30);

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
                                    newlist.add(td.text());
                                    if(newlist.size()>=10){
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
                    Log.d(TAG, "run: "+e.getMessage());
                }



            }
        }.start();
    }
}
