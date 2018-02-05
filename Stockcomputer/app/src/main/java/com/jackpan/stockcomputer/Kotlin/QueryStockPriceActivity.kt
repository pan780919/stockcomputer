package com.jackpan.stockcomputer.Kotlin

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class QueryStockPriceActivity : BaseAppCompatActivity() {
    lateinit var mAdView: AdView
    lateinit var mStockSearch: EditText
    lateinit var mSearchBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_stock_price)
        if (!checkNetWork()) {
            return
        }
        title = getString(R.string.title_activity_querystockprice)
        setAdView()

        mStockSearch = findViewById(R.id.stocknumber_edt)
        mSearchBtn = findViewById(R.id.searchbtn)
        mSearchBtn.setOnClickListener(View.OnClickListener {
            var number :String = mStockSearch.text.toString().trim()
            if(!number.isEmpty()){
                setDatePickerDialog(number)

            }

        })

    }

    fun setAdView() {

        mAdView = findViewById(R.id.adView)
        val mAdRequest = AdRequest.Builder().build()
        mAdView.loadAd(mAdRequest)
    }

    fun setDatePickerDialog(number: String) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            var YearString: String
            var MonthString: String
            var DayString: String
            YearString = year.toString()
            if ((monthOfYear + 1) <= 9) {
                MonthString = "0" + (monthOfYear + 1).toString()
            } else {
                MonthString = (monthOfYear + 1).toString()

            }
            if ((dayOfMonth) <= 9) {
                DayString = "0" + (dayOfMonth).toString()
            } else {
                DayString = (dayOfMonth).toString()
            }
            setStockData(YearString, MonthString, DayString,number)

        }, year, month, day)
        dpd.show()

    }

    private fun setStockData(year: String, month: String, day: String,number:String) {
        setLogger(year)
        setLogger(month)
        setLogger(day)
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("http://www.tse.com.tw/exchangeReport/STOCK_DAY?response=html&date=" + year + month + day + "+&stockNo=" + number).timeout(30000).get()

                    setLogger(doc.select("table>tbody>tr").size.toString())
                    for (i in 0..0) {
                        //                        for (int i1 = 0; i1 < doc.select("table>tbody>tr").get(i).select("td").size(); i1++) {
                        //                            Log.d(TAG, "run: "+doc.select("table>tbody>tr").get(i).select("td").get(i));
                        //                        }
                        for (td in doc.select("table>tbody>tr")) {
                            setLogger(td.text())
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    setLogger(e.message)

                }catch (e:SocketTimeoutException){
                    e.printStackTrace()
                    setLogger(e.message)
                }


            }
        }.start()

    }
}
