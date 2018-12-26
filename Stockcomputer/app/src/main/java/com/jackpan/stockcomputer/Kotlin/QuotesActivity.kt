package com.jackpan.stockcomputer.Kotlin
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import com.jackpan.stockcomputer.util.StockPriceCheck
import kotlinx.android.synthetic.main.activity_quotes.*
import org.jsoup.Jsoup
import java.io.IOException

class QuotesActivity : BaseAppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.quotes_1 -> {
                getList(rank_tse)

                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_2 -> {
                getList(rank_up)

                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_3 -> {
                getList(rank_down)

                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_4 -> {
                getList(rank_pdis)

                return@OnNavigationItemSelectedListener true
            }
            R.id.quotes_5-> {
                getList(rank_pdi)
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
    lateinit var mListView: ListView
    lateinit var mAdView:AdView
    var mArrayList :ArrayList<String> =ArrayList()
    lateinit var mMyAdapter : MyAdapter
    var mapInt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        initlayout()
    }

    override fun onResume() {
        super.onResume()
        getList(rank_tse)

    }
    fun initlayout(){
        mListView = findViewById(R.id.listview)
        mMyAdapter = MyAdapter(mArrayList)
        mListView.adapter = mMyAdapter
        mAdView = findViewById(R.id.ADView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
        qu_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun getList(s:String){
        mArrayList.clear()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("讀取中")
        progressDialog.show()
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect(s).get()
                    for (element in doc.select("table[border=0][cellspacing=1][cellpadding=3]")) {
                        for (tr in element.select("tr")) {
                            mArrayList.add(tr.text())
                            runOnUiThread {
                                mMyAdapter.notifyDataSetChanged()
                                progressDialog.dismiss()
                            }
                        }
                    }
                    //
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()

    }

    inner class MyAdapter(var mAllData: ArrayList<String>?) : BaseAdapter() {
        fun updateData(datas: ArrayList<String>) {
            mAllData = datas
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return mAllData!!.size
        }

        override fun getItem(position: Int): Any {
            return mAllData!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val data = mAllData!![position]

            if (convertView == null)
                convertView = LayoutInflater.from(this@QuotesActivity).inflate(
                        R.layout.layout_conceptdetail, null)

            var mNumberView: TextView = convertView!!.findViewById(R.id.stocknumbertext)
            mNumberView.text = data
            StockPriceCheck.check(data,mNumberView)

            return convertView
        }

    }
}
