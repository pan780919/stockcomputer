package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class BuyAndSellActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_and_sell)
//        ButterKnife.bind(this)
        var mTextView :TextView = findViewById(R.id.textView);
        mTextView.text = "test";

    }
}
