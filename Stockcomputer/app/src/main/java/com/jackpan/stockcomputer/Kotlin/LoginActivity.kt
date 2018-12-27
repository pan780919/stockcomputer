package com.jackpan.stockcomputer.Kotlin

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
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
import java.util.concurrent.TimeUnit

class LoginActivity : BaseAppCompatActivity() {
    @BindView(R.id.fbloginbutton)
    lateinit var mFbLoginBtn : LoginButton
    @BindView(R.id.login_button)
    lateinit var mLoginButton: TextView
    var callbackManager: CallbackManager? = null
    var loginManager: LoginManager? = null
    lateinit var lineApiClient: LineApiClient
    lateinit var phoneButton :Button
    var mAuth: FirebaseAuth? = null
    var mobileNumber: String = ""
    var verificationID: String = ""
    var token_: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        AppEventsLogger.activateApp(this)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main2)
        ButterKnife.bind(this)
        phoneButton = findViewById(R.id.phonebutton)
        if (!checkNetWork()){
            setLogger("網路無開啟！！")
            return
        }

        val apiClientBuilder = LineApiClientBuilder(applicationContext, Constants.CHANNEL_ID)
        lineApiClient = apiClientBuilder.build()
        FacebookManager.fbLogin(this,mFbLoginBtn,callbackManager)
        phoneButton.setOnClickListener {
            loginTask()
        }

    }

    private fun loginTask() {

        var mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
                Log.d(javaClass.simpleName,credential!!.smsCode)
                Log.d(javaClass.simpleName,credential.toString())

                if (credential != null) {

                }
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Log.d(javaClass.simpleName,p0.toString())

            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verificationId, token)
                Log.d(javaClass.simpleName,verificationId.toString())
                Log.d(javaClass.simpleName,token.toString())


            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                // toast("Time out")
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+886911325323",            // Phone number to verify
                60,                  // Timeout duration
                TimeUnit.SECONDS,        // Unit of timeout
                this,                // Activity (for callback binding)
                mCallBacks)

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
                MySharedPrefernces.saveUserLoginState(this,2)
                MySharedPrefernces.saveUserId(this,result.lineProfile?.userId)
                MySharedPrefernces.saveUserName(this, result.lineProfile?.displayName)
                MySharedPrefernces.saveUserPhoto(this, result.lineProfile?.pictureUrl.toString())
                LogoutTask().execute()
                this.finish()

            }

            LineApiResponseCode.CANCEL -> Log.e("ERROR", "LINE Login Canceled by user!!")

            else -> {

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
            }
        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this@LoginActivity, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            val user = task.getResult().getUser()

                        } else {
                            if (task.getException() is FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                })
    }

    private fun verifyAuthentication(verificationID: String, otpText: String) {

        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, otpText) as PhoneAuthCredential
        signInWithPhoneAuthCredential(phoneAuthCredential)
    }
}
