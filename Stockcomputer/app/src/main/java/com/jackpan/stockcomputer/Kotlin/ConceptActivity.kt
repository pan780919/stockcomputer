package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.R

import kotlinx.android.synthetic.main.activity_concept.*

class ConceptActivity : AppCompatActivity() {
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concept)
        mAdView = findViewById(R.id.adView_page)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
    }

}
