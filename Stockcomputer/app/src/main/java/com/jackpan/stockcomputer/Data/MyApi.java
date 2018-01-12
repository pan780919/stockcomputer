package com.jackpan.stockcomputer.Data;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;


public class MyApi {
    public static final String AdLocusKey = "ae0e79da3bc5d0c3ede9c907214713a9d1aa86dd";
    private static final String TAG = "MyApi";

    public static boolean isInteger(String value) {//正規表達式
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(value).matches();
    }

    public static void getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.d(TAG, "printHashKey()", e);
        }
    }


    public static void loadImage(final String path,
                                 final ImageView imageView, final Context context) {

        new Thread() {

            @Override
            public void run() {

                try {
                    URL imageUrl = new URL(path);
                    HttpURLConnection httpCon =
                            (HttpURLConnection) imageUrl.openConnection();
                    InputStream imageStr = httpCon.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStr);
                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            imageView.setImageBitmap(bitmap);
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }.start();

    }

}
