package com.jackpan.stockcomputer.Kotlin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class ZeroStockActivity : BaseAppCompatActivity() {
    lateinit var mPrcieEditText :EditText
    lateinit var mTotalEditText :EditText
    lateinit var mStockTotalText :TextView
    lateinit var mStockPriceText :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zero_stock)
        var mTitleTextView :TextView = findViewById(R.id.titletextview)
        mPrcieEditText = findViewById(R.id.price_edittext)
        mTotalEditText = findViewById(R.id.total_edittext)
        mStockTotalText = findViewById(R.id.stocktotal_text)
        mStockPriceText = findViewById(R.id.stockpricet_text)
        var mCalculate :Button = findViewById(R.id.button_calculate)
        mTitleTextView.text = getString(R.string.activity_zerostock_title)

        mCalculate.setOnClickListener(View.OnClickListener { calculatePrcie() })

    }
    fun calculatePrcie(){
        var price :String
        var total :String
        price = mPrcieEditText.text.toString().trim()
        total = mTotalEditText.text.toString().trim()
        if (!price.isEmpty()||!total.isEmpty()){
            if(!price.isEmpty()){

                var priceDouble :Double  = Math.round(14035/price.toDouble()).toDouble()
                mStockTotalText.text =priceDouble.toString()
                var priceString :String = Math.round(((priceDouble * price.toDouble())+ (priceDouble * 0.001425 * price.toDouble()))).toString()
                mStockPriceText.text = priceString

            }
            if (!total.isEmpty()){

            }

        }else{
            setAlertDilog("提示","至少輸入一項才能試算唷！！")

            return
        }

    }
    fun setAlertDilog(title:String,message:String){
        val mAlertDilog = AlertDialog.Builder(this).create()
        mAlertDilog.setTitle(title)
        mAlertDilog.setMessage(message)
        mAlertDilog.setButton(AlertDialog.BUTTON_POSITIVE,"確定", DialogInterface.OnClickListener { dialogInterface, i ->
            mAlertDilog.dismiss()
        })
        mAlertDilog.show()


    }
}
