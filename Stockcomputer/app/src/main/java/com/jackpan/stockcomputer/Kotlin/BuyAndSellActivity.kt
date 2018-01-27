package com.jackpan.stockcomputer.Kotlin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class BuyAndSellActivity : BaseAppCompatActivity() {
    lateinit var mAdView : AdView
    lateinit var mButton : Button
    lateinit var mPriceEditText :EditText
    lateinit var mTotalEditText :EditText
    lateinit var mBuyPriceText : TextView
    lateinit var mSellPriceText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_and_sell)
        if(!checkNetWork()){
            return
        }
        setTitle(getString(R.string.title_activity_buy_and_sell))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mButton = findViewById(R.id.button)
        mPriceEditText =findViewById(R.id.buypriceedit)
        mTotalEditText = findViewById(R.id.totalpriceedit)
        mBuyPriceText = findViewById(R.id.buytext)
        mSellPriceText = findViewById(R.id.selltext)


        mButton.setOnClickListener(View.OnClickListener {setCalculate() })


    }
    fun  setCalculate(){
        var mPriceString :String = mPriceEditText.text.toString().trim()
        var mTotalString :String  = mTotalEditText.text.toString().trim()
        if(!mPriceString.isEmpty()&&!mTotalString.isEmpty()){
            var transactionFree :Double  = Math.round(mPriceString.toDouble()* mTotalString.toDouble() * 0.001425).toDouble()
            var free :Double=mPriceString.toDouble()* mTotalString.toDouble() * 0.003
            setLogger(free.toString())
            if(transactionFree<20){
                transactionFree = 20.0
            }
            var buyprice :Double = (mPriceString.toDouble() * mTotalString.toDouble())+transactionFree
            var sellPrice :Double = (mPriceString.toDouble() * mTotalString.toDouble())+transactionFree+free

            mBuyPriceText.text = buyprice.toString()+"(金額："+(mPriceString.toDouble() * mTotalString.toDouble())+"手續費："+transactionFree+")"
            mSellPriceText.text = sellPrice.toString()+
            "(金額："+(mPriceString.toDouble() * mTotalString.toDouble())+"手續費："+transactionFree+"證交稅:"+free+")"



        }else{
            var dilog =AlertDialog.Builder(this).create()
            dilog.setTitle("提示")
            dilog.setMessage("請輸入數量與價錢唷！！")
            dilog.setButton(AlertDialog.BUTTON_POSITIVE,"確定", DialogInterface.OnClickListener { dialogInterface, i ->
                dilog.dismiss()
            })
            dilog.show()




        }



    }

}
