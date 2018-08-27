package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.R

import kotlinx.android.synthetic.main.activity_dividend.*

class DividendActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    lateinit var mInput_stock_price:EditText
    lateinit var mInput_stock_now_price :EditText
    lateinit var mInput_stock_amoumt :EditText
    lateinit var mMoneybutton :Button
    lateinit var mInput_money_now_price :EditText
    lateinit var mInput_money_amoumt :EditText
    lateinit var mStockbutton :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_dividend)
        title = "配股配息計算"
        setAdView()
        initLayout()


    }
    fun  setAdView(){
        mAdView = findViewById(R.id.adView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
    }
    fun initLayout(){
        mInput_stock_price = findViewById(R.id.input_stock_price)
        mInput_stock_now_price = findViewById(R.id.input_stock_now_price)
        mInput_stock_amoumt = findViewById(R.id.input_money_amoumt)
        mInput_money_now_price = findViewById(R.id.input_money_price)
        mInput_money_amoumt = findViewById(R.id.input_money_amoumt)
        mMoneybutton = findViewById(R.id.moneybutton)
        mStockbutton = findViewById(R.id.stockbutton)
    }

    fun  calStock(){

    }

    fun  calMoney(){

    }
}
