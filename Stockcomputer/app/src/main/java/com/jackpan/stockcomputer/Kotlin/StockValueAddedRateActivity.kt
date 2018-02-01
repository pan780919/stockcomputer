package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

class StockValueAddedRateActivity : BaseAppCompatActivity() {
    lateinit var  mSearchNumberEdt :EditText
    lateinit var  mSearchNumberButton :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_value_added_rate)
        if (!checkNetWork()) {
            return
        }
        setTitle("股票殖利率試算")
        mSearchNumberEdt = findViewById(R.id.searchnumber_edt)
        mSearchNumberButton = findViewById(R.id.searchbutton)
        mSearchNumberButton.setOnClickListener(View.OnClickListener {
            var mNumber :String =mSearchNumberEdt.text.toString().trim()
            if(!mNumber.isEmpty()){

                stockCalculate(mNumber)

            }


        })
    }


    fun stockCalculate(number: String) {

        object : Thread() {
            override fun run() {
                super.run()
                val price = ArrayList<String>()
                try {
                    setLogger("https://tw.stock.yahoo.com/d/s/dividend_$number.html")
                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/d/s/dividend_$number.html").get()
                    for (table in doc.select("table[width=630][align=center]>tbody")) {

                        for (element in table.select("table[cellspacing=1]")) {
                            for (element1 in element.select("tbody")) {
                                // 抓抬頭
                                //                                Log.d(TAG, "run: "+element1.select("tr>td.ttt").size());
                                for (element2 in element1.select("tr>td.ttt")) {
                                    //                                    Log.d(TAG, "run: "+element2.text());

                                }
                                // 抓內容
                                for (element2 in element1.select("tr[bgcolor=#FFFFFF]")) {
                                    //                                    Log.d(TAG, "run: "+element2.select("td").size());
                                    for (td in element2.select("td")) {
                                        //                                        Log.d(TAG, "run: "+td.text());
                                        price.add(td.text())
//                                        setLogger(td.text())
                                    }
                                }


                            }
                        }
                    }
                    setLogger(price[1])


                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()
    }


}
