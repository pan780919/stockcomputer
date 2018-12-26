package com.jackpan.stockcomputer.Kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity

class QuotesActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
    }
}
