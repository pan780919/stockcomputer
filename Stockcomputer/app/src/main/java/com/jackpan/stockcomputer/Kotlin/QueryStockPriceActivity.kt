package com.jackpan.stockcomputer.Kotlin

import android.app.DatePickerDialog
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import java.util.*

class QueryStockPriceActivity : BaseAppCompatActivity() {
    lateinit var mAdView :AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_stock_price)
        if (!checkNetWork()) {
            return
        }
            title = getString(R.string.title_activity_querystockprice)

            setAdView()
            setDatePickerDialog()
        }

    fun setAdView(){

        mAdView = findViewById(R.id.adView)
        val mAdRequest =AdRequest.Builder().build()
        mAdView.loadAd(mAdRequest)
    }

    fun setDatePickerDialog(){

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            setLogger("" + dayOfMonth + " " + monthOfYear+1 + ", " + year)
        }, year, month, day)
        dpd.show()


    }
}
