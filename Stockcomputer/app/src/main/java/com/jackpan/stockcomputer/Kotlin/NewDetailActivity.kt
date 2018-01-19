package com.jackpan.stockcomputer.Kotlin
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

class NewDetailActivity : BaseAppCompatActivity() {
    var mTitleList =ArrayList<String>();
    lateinit var mTitleTextView:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_detail)
        setTitle(getString(R.string.title_activity_news_detail))
        checkNetWork()
        mTitleTextView = findViewById(R.id.newsdetail);


        setLogger(intent.getStringExtra("url"))
        getNewsDetail(intent.getStringExtra("url"))

    }
    fun getNewsDetail(s:String){
        var mProgress =ProgressDialog(this)
        mProgress.setMessage("資料讀取中")
        mProgress.setTitle("請稍等片刻！！")
        mProgress.show()
        thread {
            try {

                val doc = Jsoup.connect(s).get()
                for (element in doc.select("table[class=yui-text-left yui-table-wfix ynwsart]>tbody>tr>td>span")) {
                    /**
                     * 抓標題
                     */
                    mTitleList.add(element.text())





                                            Log.d("test", "getNewDetil: "+element.text());
                }
                for (element in doc.select("p")) {
//                                            Log.d("test", "getNewDetil: "+element.toString());
                                            Log.d("test", "getNewDetil: "+element.text());
                    if (element.text() != "") {
                        /**
                         * 抓內容
                         */

                        mTitleList.add(element.text())
                    }

                }
                mProgress.dismiss()

            } catch (e: IOException) {
                e.printStackTrace()
            }
            runOnUiThread(){

                mTitleList.forEach {
                    mTitleTextView.setText(it)

                }
            }
        }


    }
}
