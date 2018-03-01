package com.jackpan.stockcomputer.Kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_check_version.*



class CheckVersionActivity : BaseAppCompatActivity() {
    @BindView(R.id.versionnametext)
    lateinit var mVersionName :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_version)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        title = "檢查版本更新"
        getNowVersion()

    }
    fun getNowVersion(){
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            setLogger(pInfo.versionCode.toString())
            setLogger( pInfo.versionName)
            mVersionName.text = pInfo.versionName

        }catch (e:Exception){


        }
    }
    fun checkVersion(){
        var appName :String = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)))

        }catch (e:Exception){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appName)))

        }
    }
    @OnClick(R.id.checkbutton)
    fun UpdateVersion(){
        checkVersion()

    }


}
