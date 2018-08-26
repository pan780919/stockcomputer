package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
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
import com.jackpan.stockcomputer.Data.ConceptData
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup

class ConceptDetailActivity : AppCompatActivity() {
    lateinit var mListView : ListView
    lateinit var mMyAdapter : MyAdapter
    lateinit var mAdView : AdView

    var dataList :ArrayList<String> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concept_detail)
        initLayout()
        getData()

        mAdView = findViewById(R.id.adView_page)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)


    }
    fun getData(){
       val mtitle :String =  intent.getStringExtra("title")
       val mUrl :String  =  intent.getStringExtra("url")
        getDetail(mUrl)

    }
    fun getDetail(url:String){
        Thread(){
            run {
                try {
                    val doc = Jsoup.connect(url).get()
                    for (element in doc.select("table[border=1][cellspacing=0][cellpadding=2]")) {
                        for (tr in element.select("tr")) {
                            Log.d(javaClass.simpleName,tr.text())
                            dataList.add(tr.text())
                            runOnUiThread(Runnable {
                                mMyAdapter.notifyDataSetChanged()

                            })
                        }



                    }


                } catch (e: Exception) {
                    Log.d(javaClass.simpleName, Log.getStackTraceString(e))
                }
            }
        }.start()
    }
    fun initLayout(){
        mListView = findViewById(R.id.listView)
        mMyAdapter = MyAdapter(dataList)
        mListView.adapter = mMyAdapter
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
            Log.d(javaClass.simpleName,mAllData!![0])
            val data = mAllData!![position]
            if (convertView == null)
                convertView = LayoutInflater.from(this@ConceptDetailActivity).inflate(
                        R.layout.layout_conceptdetail, null)


            var mNumberView: TextView = convertView!!.findViewById(R.id.stocknumbertext)
//            var mTimeView: TextView = convertView!!.findViewById(R.id.timetext)
//
//            var mMoneyView: TextView = convertView!!.findViewById(R.id.moneytext)
//            var mBuyView: TextView = convertView!!.findViewById(R.id.buytext)
//
//            var mSellView: TextView = convertView!!.findViewById(R.id.selltext)
//
//            var mHigtlowView: TextView = convertView!!.findViewById(R.id.higtlowtext)
//
//            var mAmounttextView: TextView = convertView!!.findViewById(R.id.amounttext)
//
//            var mYestdaytextView: TextView = convertView!!.findViewById(R.id.yestdaytext)
//
//            var mOpenView: TextView = convertView!!.findViewById(R.id.opentext)
//
//            var mHighttextView: TextView = convertView!!.findViewById(R.id.highttext)
//
//            var mLowtextView: TextView = convertView!!.findViewById(R.id.lowtext)
            mNumberView.text = data


            return convertView
        }

    }
}
