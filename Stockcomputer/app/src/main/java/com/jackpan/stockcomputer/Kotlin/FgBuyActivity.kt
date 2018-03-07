package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.widget.ListView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_fg_buy.*
import org.jsoup.Jsoup

class FgBuyActivity : BaseAppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fgbuy_tse -> {

                getFg(URL_FGBUY_TSE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgbuy_tse_w -> {
                getFg(URL_FGBUY_TSE_W)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse -> {
                getFg(URL_FGSELL_TSE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse_w ->{
                getFg(URL_FGSELL_TEST_W)
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }
    var URL_FGBUY_TSE :String ="https://tw.stock.yahoo.com/d/i/fgbuy_tse.html"
    var URL_FGBUY_TSE_W :String = "https://tw.stock.yahoo.com/d/i/fgbuy_tse_w.html"
    var URL_FGSELL_TSE : String = "https://tw.stock.yahoo.com/d/i/fgsell_tse.html"
    var URL_FGSELL_TEST_W :String = "https://tw.stock.yahoo.com/d/i/fgsell_tse_w.html"

    lateinit var mAdbertView : AdView
    lateinit var mFgBuyListView : ListView
    var mFgBuyArrayList =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fg_buy)
        mFgBuyListView = findViewById(R.id.fgbuylistview)

        setAdmob()
        getFg(URL_FGBUY_TSE)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun setAdmob(){
        mAdbertView = findViewById(R.id.adbertADView)
        val mAdRequest =AdRequest.Builder().build()
        mAdbertView.loadAd(mAdRequest)


    }
    fun getFg(url:String){
        mFgBuyArrayList.clear()
        var mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("讀取中")
        mProgressDialog.setMessage("請稍候")
        mProgressDialog.setCancelable(true)
        mProgressDialog.show()

        Thread(){
            run {
                try {
                    val doc = Jsoup.connect(url).get()
                    for (element in doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]")) {
                        for (tr in element.select("tr")) {
//                            Log.d(javaClass.simpleName, "run: " + tr.text())
                            mFgBuyArrayList.add(tr.text())
                        }
                        Log.d(javaClass.simpleName, "run: " + element.select("tr").get(0).select("td").text())

                    }

                }catch (e:Exception){

                }

                runOnUiThread { //
                    mFgBuyArrayList.forEach {
//                        Log.d(javaClass.simpleName, "run: " + it)

                    }
                                  mProgressDialog.dismiss()


                }
            }

        }.start()
    }
}
