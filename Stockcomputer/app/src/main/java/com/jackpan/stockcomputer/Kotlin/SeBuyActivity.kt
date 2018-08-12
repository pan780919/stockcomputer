package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
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
import com.jackpan.stockcomputer.Data.SeBuyData
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_fg_buy.*

import kotlinx.android.synthetic.main.activity_se_buy.*
import org.jsoup.Jsoup

class SeBuyActivity : BaseAppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fgbuy_tse -> {

                getSeBuy(URL_SEBUY_TSE)
                title = mSeBuyTitleList[0].toString()


                return@OnNavigationItemSelectedListener true
            }
            R.id.fgbuy_tse_w -> {
                getSeBuy(URL_SEBUY_TSE_W)
                title = mSeBuyTitleList[1].toString()

                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse -> {
                getSeBuy(URL_SESELL_TSE)
                title = mSeBuyTitleList[2].toString()

                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse_w -> {
                getSeBuy(URL_SESELL_TEST_W)
                title = mSeBuyTitleList[3].toString()

                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }
    var URL_SEBUY_TSE: String = "https://tw.stock.yahoo.com/d/i/sebuy_tse.html"
    var URL_SEBUY_TSE_W: String = "https://tw.stock.yahoo.com/d/i/sebuy_tse_w.html"
    var URL_SESELL_TSE: String = "https://tw.stock.yahoo.com/d/i/sesell_tse.html"
    var URL_SESELL_TEST_W: String = "https://tw.stock.yahoo.com/d/i/sesell_tse_w.html"

    lateinit var mAdbertView: AdView
    lateinit var mFgBuyListView: ListView
    var mSeBuyArrayList =ArrayList<SeBuyData>()
    var mAdapter: MyAdapter? = null
    lateinit var mSeBuyTitleList : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_se_buy)
        mSeBuyTitleList  = (resources.getStringArray(R.array.fgtitlelist))
        title = mSeBuyTitleList[0]
        mFgBuyListView = findViewById(R.id.fgbuylistview)
        setAdmob()
        getSeBuy(URL_SEBUY_TSE)
        mAdapter = MyAdapter(mSeBuyArrayList)
        mFgBuyListView.adapter = mAdapter
        sebuy_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }
    fun setAdmob() {
        mAdbertView = findViewById(R.id.adbertADView)
        val mAdRequest = AdRequest.Builder().build()
        mAdbertView.loadAd(mAdRequest)


    }
    fun getSeBuy(url: String) {
        mSeBuyArrayList.clear()
        var mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("讀取中")
        mProgressDialog.setMessage("請稍候")
        mProgressDialog.setCancelable(true)
        mProgressDialog.show()

        Thread() {
            run {
                try {
                    val doc = Jsoup.connect(url).get()
                    for (element in doc.select("table[border=0][cellpadding=3][cellspacing=1]")) {


                        for (tr in element.select("tr")) {
                            var mSeBuyBuyData = SeBuyData()
                            mSeBuyBuyData.setSeBuymessage(tr.text().get(0).toString())

                            mSeBuyBuyData.setSeBuymessage(tr.text())
                            mSeBuyArrayList.add(mSeBuyBuyData)

                            runOnUiThread {

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
    inner class MyAdapter(private var mDatas:ArrayList<SeBuyData>?) : BaseAdapter() {
        fun updateData(datas: ArrayList<SeBuyData>) {
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
                convertView = LayoutInflater.from(this@SeBuyActivity).inflate(
                        R.layout.fgbuy_layout, null)
                viewHolder = ViewHolder(convertView)
                convertView!!.tag = viewHolder
            }

            viewHolder.mFgTitle.text = data.SeBuytitle
            viewHolder.mFgMessage.text = data.SeBuymessage


            return convertView
        }

    }


    internal class ViewHolder(v: View) {
        val mFgTitle: TextView = v.findViewById(R.id.fugtitletext)
        val mFgMessage: TextView = v.findViewById(R.id.fugmessagetext)

    }
}
