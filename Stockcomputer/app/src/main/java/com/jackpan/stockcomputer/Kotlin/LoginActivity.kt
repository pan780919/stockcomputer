package com.jackpan.stockcomputer.Kotlin

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.LineLogin.Constants
import com.jackpan.stockcomputer.LineLogin.PostLoginActivity
import com.jackpan.stockcomputer.Manager.FacebookManager
import com.jackpan.stockcomputer.Manager.LineLoginManager.REQUEST_CODE
import com.jackpan.stockcomputer.MySharedPrefernces
import com.jackpan.stockcomputer.R
import com.linecorp.linesdk.LineApiResponse
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.api.LineApiClient
import com.linecorp.linesdk.api.LineApiClientBuilder
import com.linecorp.linesdk.auth.LineLoginApi

class LoginActivity : BaseAppCompatActivity() {
    @BindView(R.id.fbloginbutton)
    lateinit var mFbLoginBtn : LoginButton
    @BindView(R.id.login_button)
    lateinit var mLoginButton: TextView
    var callbackManager: CallbackManager? = null
    var loginManager: LoginManager? = null
    lateinit var lineApiClient: LineApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        AppEventsLogger.activateApp(this)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main2)
        ButterKnife.bind(this)
        if (!checkNetWork()){
            setLogger("網路無開啟！！")
            return
        }
        val apiClientBuilder = LineApiClientBuilder(applicationContext, Constants.CHANNEL_ID)
        lineApiClient = apiClientBuilder.build()
        FacebookManager.fbLogin(this,mFbLoginBtn,callbackManager)

    }

    override fun onResume() {
        super.onResume()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(resultCode!= Activity.RESULT_OK){
            return
        }
        val result = LineLoginApi.getLoginResultFromIntent(data)

        when (result.responseCode) {

            LineApiResponseCode.SUCCESS -> {

                val transitionIntent = Intent(this, PostLoginActivity::class.java)
//                transitionIntent.putExtra("line_profile", result.lineProfile)
//                transitionIntent.putExtra("line_credential", result.lineCredential)
//                startActivity(transitionIntent)
                Log.d("msg", result.lineProfile?.userId)
//                Log.d("msg", result.lineProfile?.pictureUrl.toString())
//                Log.d("msg", result.lineProfile?.displayName)
                MySharedPrefernces.saveUserLoginState(this,2)
                MySharedPrefernces.saveUserId(this,result.lineProfile?.userId)
                MySharedPrefernces.saveUserName(this, result.lineProfile?.displayName)
                MySharedPrefernces.saveUserPhoto(this, result.lineProfile?.pictureUrl.toString())
                LogoutTask().execute()
                this.finish()

            }

            LineApiResponseCode.CANCEL -> Log.e("ERROR", "LINE Login Canceled by user!!")

            else -> {
                Log.e("ERROR", "Login FAILED!")
                Log.e("ERROR", result.errorData.toString())
            }
        }

    }
    @OnClick(R.id.login_button)
    fun setLoginButton(){
        try {
            val LoginIntent = LineLoginApi.getLoginIntent(this, Constants.CHANNEL_ID)
            startActivityForResult(LoginIntent, REQUEST_CODE)
        }catch (e:Exception){
        }

    }

    inner class LogoutTask : AsyncTask<Void, Void, LineApiResponse<*>>() {

        override fun onPreExecute() {
        }

        override fun doInBackground(vararg params: Void): LineApiResponse<*> {
            return lineApiClient.logout()
        }

        override fun onPostExecute(apiResponse: LineApiResponse<*>) {

            if (apiResponse.isSuccess) {
                Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Logout Failed", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Logout Failed: " + apiResponse.errorData.toString())
            }
        }

    }
}
