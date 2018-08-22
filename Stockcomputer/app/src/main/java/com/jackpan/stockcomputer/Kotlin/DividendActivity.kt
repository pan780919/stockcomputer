package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.R

import kotlinx.android.synthetic.main.activity_dividend.*

class DividendActivity : AppCompatActivity() {
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_dividend)
        title = "配股配息計算"
        setAdView()

    }
    fun  setAdView(){
        mAdView = findViewById(R.id.adView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
    }

}
