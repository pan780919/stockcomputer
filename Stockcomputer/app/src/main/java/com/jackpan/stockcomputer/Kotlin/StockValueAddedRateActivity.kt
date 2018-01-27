package com.jackpan.stockcomputer.Kotlin
import android.os.Bundle
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class StockValueAddedRateActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_value_added_rate)
        if(!checkNetWork()){
            return
        }
    }

}
