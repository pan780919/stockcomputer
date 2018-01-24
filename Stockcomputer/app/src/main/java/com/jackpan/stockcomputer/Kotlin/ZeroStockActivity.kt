package com.jackpan.stockcomputer.Kotlin

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zero_stock)
        var mTitleTextView :TextView = findViewById(R.id.titletextview)
        mPrcieEditText = findViewById(R.id.price_edittext)
        mTotalEditText = findViewById(R.id.total_edittext)
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

                var priceLong :Long  = 14035/price.toLong()
                setLogger(priceLong.toString())

            }
            if (!total.isEmpty()){

            }

        }else{
            setLogger("至少輸入一項才能試算唷！！")

            return
        }

    }
}
