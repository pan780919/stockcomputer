package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class QueryStockPriceActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_stock_price)
        title = getString(R.string.title_activity_querystockprice)
    }
}
