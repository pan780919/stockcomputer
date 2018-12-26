package com.jackpan.stockcomputer.Kotlin
import android.os.Bundle
import android.util.Log
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException

class QuotesActivity : BaseAppCompatActivity() {
    val rank_tse = "https://tw.stock.yahoo.com/d/i/rank.php?t=vol&e=tse"
    val rank_up = "https://tw.stock.yahoo.com/d/i/rank.php?t=up&e=tse"
    val rank_down = "https://tw.stock.yahoo.com/d/i/rank.php?t=down&e=tse"
    val rank_pdis = "https://tw.stock.yahoo.com/d/i/rank.php?t=pdis&e=tse"
    val rank_pdi = "https://tw.stock.yahoo.com/d/i/rank.php?t=pri&e=tse"
    val rank_amt = "https://tw.stock.yahoo.com/d/i/rank.php?t=amt&e=tse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
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
