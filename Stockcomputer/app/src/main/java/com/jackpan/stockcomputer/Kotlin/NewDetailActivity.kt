package com.jackpan.stockcomputer.Kotlin
import android.os.Bundle
import android.util.Log
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

class NewDetailActivity : BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_detail)
        setTitle(getString(R.string.title_activity_news_detail))
        checkNetWork()

        thread {
            try {

                val doc = Jsoup.connect("https://tw.finance.yahoo.com/news_content/url/d/a/20180118/%E7%A8%85%E6%94%B9%E4%B8%89%E8%AE%80%E9%81%8E%E9%97%9C-%E6%9C%83%E8%A8%88%E5%B8%AB%E8%AE%9A%E8%AA%A0%E6%84%8F%E5%8D%81%E8%B6%B3-%E7%9C%8B%E5%A5%BD%E8%82%A1%E5%88%A9%E6%89%80%E5%BE%97%E5%88%86%E9%9B%A2%E8%AA%B2%E7%A8%85%E5%B8%B6%E4%BE%864%E5%88%A9%E5%A4%9A-121341195.html").get()
                for (element in doc.select("table[class=yui-text-left yui-table-wfix ynwsart]>tbody>tr>td>span")) {
                    //                        Log.d(TAG, "getNewDetil: "+element.text());
                }
                for (element in doc.select("p")) {
                    //                        Log.d(TAG, "getNewDetil: "+element.toString());
                    //                        Log.d(TAG, "getNewDetil: "+element.text());
//                    if (element.text() != "") {
//                        n.add(element.text())
//                    }
                    Log.d("test", "run: " + element.text())

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}
