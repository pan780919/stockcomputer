package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class BuyAndSellActivity : BaseAppCompatActivity() {
    lateinit var mAdView : AdView
    lateinit var mButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_and_sell)
        checkNetWork()
        setTitle(getString(R.string.title_activity_buy_and_sell))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mButton = findViewById(R.id.button)
        mButton.setOnClickListener(View.OnClickListener {setOnClick("111") })


    }
    fun  setOnClick(s :String){
        showToast(s)
    }

}
