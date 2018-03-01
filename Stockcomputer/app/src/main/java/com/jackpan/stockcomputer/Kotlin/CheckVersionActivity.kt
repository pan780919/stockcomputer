package com.jackpan.stockcomputer.Kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_check_version.*



class CheckVersionActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_version)
        setSupportActionBar(toolbar)
        title = "檢查版本更新"

    }
    fun getNowVersion(){
        var mVersionName :String
        var mVersionCode :String
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            pInfo.versionCode.toString()
            pInfo.versionName
        }catch (e:Exception){


        }
    }
    fun updateVersion(){
        var appName :String = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)))

        }catch (e:Exception){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appName)))

        }
    }

}
