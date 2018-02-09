package com.jackpan.stockcomputer.Kotlin

import android.content.Intent
import android.os.Bundle
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class LoginActivity : BaseAppCompatActivity() {
    @BindView(R.id.fbloginbutton)
    lateinit var mFbLoginBtn : LoginButton
    var callbackManager: CallbackManager? = null
    var loginManager: LoginManager? = null

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

    }

    override fun onResume() {
        super.onResume()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

}
