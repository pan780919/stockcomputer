package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

class StockValueAddedRateActivity : BaseAppCompatActivity() {
    lateinit var  mSearchNumberEdt :EditText
    lateinit var  mSearchNumberButton :Button
    lateinit var   dialog :ProgressDialog
     var mStockmoney :String  =""
     var  mStockNowPrice :String =""
    lateinit var  mIdeaText : TextView
    lateinit var  mRateText :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_value_added_rate)
        if (!checkNetWork()) {
            return
        }
        setTitle("股票殖利率試算")
        mSearchNumberEdt = findViewById(R.id.searchnumber_edt)
        mSearchNumberButton = findViewById(R.id.searchbutton)
        mIdeaText =findViewById(R.id.ideatext)
        mRateText = findViewById(R.id.ratetext)
        mSearchNumberButton.setOnClickListener(View.OnClickListener {
            var mNumber :String =mSearchNumberEdt.text.toString().trim()
            if(!mNumber.isEmpty()){


                stockCalculate(mNumber)
                getStockNowPrice(mNumber)


            }


        })
    }

     fun getStockNowPrice(number: String) {
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    setLogger("https://goodinfo.tw/StockInfo/StockDetail.asp?STOCK_ID="+number)
                    val doc = Jsoup.connect("https://goodinfo.tw/StockInfo/StockDetail.asp?STOCK_ID="+number).get()
//                    val t = doc.select("tr[align=center][height=26px][bgcolor=#e7f3ff]")[0]
                    //                    Log.d(TAG, "run: "+ t.select("td").get(0).text());
                    //                    for (Element td : t.select("td")) {
                    //                        Log.d(TAG, "run: "+td.text());
                    //                    }
                    val e = doc.select("tr[align=center][height=26px][bgcolor=white]")[0]
                    setLogger(e.select("td").get(0).text())
                    mStockNowPrice = e.select("td").get(0).text();
                    //                    Log.d(TAG, "run: "+ e.select("td").get(0).text());
                    //                    for (Element td : e.select("td")) {
                    //
                    //                        Log.d(TAG, "run: "+td.text());
                    //
                    //
                    //                    }

                    runOnUiThread(Runnable {
                        calculatePrice(mStockmoney,mStockNowPrice)
                    })

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()


    }

    fun stockCalculate(number: String) {
        dialog= ProgressDialog(this)

        dialog.setMessage("Please wait")
        dialog.setTitle("Loading")
        dialog.setCancelable(false)
        dialog.isIndeterminate=true
        dialog.show()
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
                    mStockmoney = price[1]


                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()
    }
    fun  calculatePrice(price:String,money:String){
        /**
         *股票殖利率= [現金股利] 除以 [股價]
         * price = [現金股利]
         *
         * money =[股價]
         *
         */
        setLogger(money)
        setLogger(price)

        if(!price.equals("")&& !money.equals("")){
            var rate :Double = price.toDouble()/money.toDouble()
            setLogger(rate.toString())
            mRateText.text = rate.toString()

            dialog.dismiss()




        }


    }

}
