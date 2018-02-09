package com.jackpan.stockcomputer.Manager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.jackpan.stockcomputer.Data.MyApi;
import com.jackpan.stockcomputer.MySharedPrefernces;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FacebookManager {
    private static final String TAG = "FacebookManager";
    private ProfileTracker profileTracker;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;

    //臉書登入
    private void fbLogin(Context context,LoginButton mFbLoginButton, CallbackManager callbackManager) {
        List<String> PERMISSIONS_PUBLISH = Arrays.asList("public_profile", "email", "user_friends","user_location","user_birthday", "user_likes");
        mFbLoginButton.setReadPermissions(PERMISSIONS_PUBLISH);
        mFbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(context,loginResult.getAccessToken());
//                setUsetProfile();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoginActivity", object.toString());
                        // Get facebook data from login
//                        Bundle bFacebookData = getFacebookData(object);
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


            }

        });

    }

    private void handleFacebookAccessToken(Context context,AccessToken token) {


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
//                updateWithToken(newAccessToken);
            }
        };

        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        auth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
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

    private void checkFbState(Context context ,ImageView mFbImageView, TextView mUserIdTextView, TextView mUserAccountTextView) {
        if (Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();
            mUserAccountTextView.setText(name);
            mUserIdTextView.setText(id);
            MyApi.loadImage(String.valueOf(userPhoto), mFbImageView, context);
            MySharedPrefernces.saveUserId(context, id);
        } else {
            mFbImageView.setImageDrawable(null);
            mUserAccountTextView.setText("");
            mUserIdTextView.setText("");
            MySharedPrefernces.saveUserId(context, "");
        }

    }


    private void setUsetProfile(Context context ,ImageView mFbImageView, TextView mUserIdTextView, TextView mUserAccountTextView) {
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
                    MySharedPrefernces.saveUserPhoto(context, String.valueOf(currentProfile.getProfilePictureUri(150, 150)));
                    MyApi.loadImage(String.valueOf(currentProfile.getProfilePictureUri(150, 150)), mFbImageView, context);
                    String id = currentProfile.getId();
                    String name = currentProfile.getName();
                    mUserIdTextView.setText(id);
                    mUserAccountTextView.setText(name);

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


}
