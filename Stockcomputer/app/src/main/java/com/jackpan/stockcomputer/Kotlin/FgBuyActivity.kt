package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.Data.FgBuyData
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_fg_buy.*
import org.jsoup.Jsoup

class FgBuyActivity : BaseAppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fgbuy_tse -> {

                getFg(URL_FGBUY_TSE)
                title = mFgTitleList[0].toString()


                return@OnNavigationItemSelectedListener true
            }
            R.id.fgbuy_tse_w -> {
                getFg(URL_FGBUY_TSE_W)
                title = mFgTitleList[1].toString()

                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse -> {
                getFg(URL_FGSELL_TSE)
                title = mFgTitleList[2].toString()

                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse_w -> {
                getFg(URL_FGSELL_TEST_W)
                title = mFgTitleList[3].toString()

                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }
    var URL_FGBUY_TSE: String = "https://tw.stock.yahoo.com/d/i/fgbuy_tse.html"
    var URL_FGBUY_TSE_W: String = "https://tw.stock.yahoo.com/d/i/fgbuy_tse_w.html"
    var URL_FGSELL_TSE: String = "https://tw.stock.yahoo.com/d/i/fgsell_tse.html"
    var URL_FGSELL_TEST_W: String = "https://tw.stock.yahoo.com/d/i/fgsell_tse_w.html"

    lateinit var mAdbertView: AdView
    lateinit var mFgBuyListView: ListView
    var mFgBuyArrayList =ArrayList<FgBuyData>()
    var mAdapter: MyAdapter? = null
    lateinit var mFgTitleList : Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fg_buy)
        mFgTitleList  = (resources.getStringArray(R.array.fgtitlelist))
        title = mFgTitleList[0]
        mFgBuyListView = findViewById(R.id.fgbuylistview)
        setAdmob()
        getFg(URL_FGBUY_TSE)
        mAdapter = MyAdapter(mFgBuyArrayList)
        mFgBuyListView.adapter = mAdapter
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun setAdmob() {
        mAdbertView = findViewById(R.id.adbertADView)
        val mAdRequest = AdRequest.Builder().build()
        mAdbertView.loadAd(mAdRequest)


    }

    fun getFg(url: String) {
        mFgBuyArrayList.clear()
        var mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("讀取中")
        mProgressDialog.setMessage("請稍候")
        mProgressDialog.setCancelable(true)
        mProgressDialog.show()

        Thread() {
            run {
                try {
                    val doc = Jsoup.connect(url).get()
//                    Log.d("TAG", doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]").select("tr").get(0).text())
//                    Log.d("TAG",doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]").select("tr").get(0).toString())
                    for (element in doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]")) {


                        for (tr in element.select("tr")) {
//                            Log.d(javaClass.simpleName, "run: " + tr.text())
//                            mFgBuyArrayList.add(tr.text())
                            var mFgBuyData = FgBuyData()
//                            mFgBuyData.setFutitle(doc.select("table[border=0][width=600][cellpadding=3][cellspacing=1]").select("tr").get(0).text())
                            mFgBuyData.setFgmessage(tr.text().get(0).toString())

                            mFgBuyData.setFgmessage(tr.text())
                            mFgBuyArrayList.add(mFgBuyData)
                            runOnUiThread {
//                                Log.d("TAG", mFgBuyData.futitle.get(0).toString())

//                                Log.d("TAG", mFgBuyData.futitle)
                                Log.d("TAG", mFgBuyData.fgmessage)
                                mAdapter?.notifyDataSetChanged()

                                mProgressDialog.dismiss()


                            }
                        }
                    }

                } catch (e: Exception) {
                }


            }

        }.start()
    }

    inner class MyAdapter(private var mDatas: java.util.ArrayList<FgBuyData>?) : BaseAdapter() {
        fun updateData(datas: java.util.ArrayList<FgBuyData>) {
            mDatas = datas
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return mDatas!!.size
        }

        override fun getItem(position: Int): Any {
            return mDatas!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            val data = mDatas!![position]
            if (convertView != null) {
                viewHolder = convertView.tag as ViewHolder
            } else {
                convertView = LayoutInflater.from(this@FgBuyActivity).inflate(
                        R.layout.fgbuy_layout, null)
                viewHolder = ViewHolder(convertView)
                convertView!!.tag = viewHolder
            }

            viewHolder.mFgTitle.text = data.futitle
            viewHolder.mFgMessage.text = data.fgmessage


            return convertView
        }

    }


    internal class ViewHolder(v: View) {
        val mFgTitle: TextView = v.findViewById(R.id.fugtitletext)
        val mFgMessage: TextView = v.findViewById(R.id.fugmessagetext)

    }
}