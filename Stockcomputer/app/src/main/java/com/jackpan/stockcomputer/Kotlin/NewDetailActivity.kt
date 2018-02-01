package com.jackpan.stockcomputer.Kotlin
//import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

class NewDetailActivity : BaseAppCompatActivity() {
    var mTitleList =ArrayList<String>()
    lateinit var mTitleTextView:TextView
    lateinit var mTimeTextView :TextView
    lateinit var mFormTextView :TextView
    lateinit var mDetailTextView : TextView
    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_detail)
        setTitle(getString(R.string.title_activity_news_detail))
        checkNetWork()
        mTitleTextView = findViewById(R.id.newstile)
        mTimeTextView = findViewById(R.id.newstime)
        mFormTextView = findViewById(R.id.newsfrom)
        mDetailTextView = findViewById(R.id.newsdetail)
        mAdView = findViewById(R.id.adView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
        setLogger(intent.getStringExtra("url"))
        getNewsDetail(intent.getStringExtra("url"))

    }
    fun getNewsDetail(s:String){
//        var mProgress = ProgressDialog(this)
//        mProgress.setMessage("資料讀取中")
//        mProgress.setTitle("請稍等片刻！！")
//        mProgress.show()
        thread {
            try {

                val doc = Jsoup.connect(s).get()
                for (element in doc.select("table[class=yui-text-left yui-table-wfix ynwsart]>tbody>tr>td>span")) {
                    /**
                     * 抓標題
                     */
                    mTitleList.add(element.text())
                }
                for (element in doc.select("p")) {
                    if (element.text() != "") {
                        /**
                         * 抓內容
                         */

                        mTitleList.add(element.text())
                    }

                }
//                mProgress.dismiss()

            } catch (e: IOException) {
                e.printStackTrace()
            }
            runOnUiThread{
                mTitleTextView.text= mTitleList[0]

                mTimeTextView.text = mTitleList[1]
                mFormTextView.text = mTitleList[2]
                if (mTitleList.size>=4){
                    mDetailTextView.text = mTitleList[3]

                }

            }
        }


    }
}
