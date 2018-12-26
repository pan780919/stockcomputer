package com.jackpan.stockcomputer.Kotlin
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.widget.ListView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_quotes.*
import kotlinx.android.synthetic.main.activity_world_idx.*
import org.jsoup.Jsoup
import java.io.IOException

class QuotesActivity : BaseAppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.quotes_1 -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_2 -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_3 -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_4 -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_5-> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_6 -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    val rank_tse = "https://tw.stock.yahoo.com/d/i/rank.php?t=vol&e=tse"
    val rank_up = "https://tw.stock.yahoo.com/d/i/rank.php?t=up&e=tse"
    val rank_down = "https://tw.stock.yahoo.com/d/i/rank.php?t=down&e=tse"
    val rank_pdis = "https://tw.stock.yahoo.com/d/i/rank.php?t=pdis&e=tse"
    val rank_pdi = "https://tw.stock.yahoo.com/d/i/rank.php?t=pri&e=tse"
    val rank_amt = "https://tw.stock.yahoo.com/d/i/rank.php?t=amt&e=tse"
    lateinit var mListView: ListView
    lateinit var mAdView:AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
    }

    override fun onResume() {
        super.onResume()

    }
    fun initlayout(){
        mListView = findViewById(R.id.listview)
        mAdView = findViewById(R.id.ADView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
        qu_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun getList(s:String){
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/rank.php?t=pri&e=tse").get()
                    for (element in doc.select("table[border=0][cellspacing=1][cellpadding=3]")) {
                        for (tr in element.select("tr")) {
                        }
                    }
                    //
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()

    }
}
