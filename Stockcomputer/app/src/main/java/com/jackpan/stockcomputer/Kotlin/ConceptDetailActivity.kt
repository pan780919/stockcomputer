package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
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
import com.jackpan.stockcomputer.util.StockPriceCheck
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
        title = mtitle
        getDetail(mUrl)

    }
    fun getDetail(url:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("讀取中")
        progressDialog.show()
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
                                progressDialog.dismiss()

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
            mNumberView.text = data
            StockPriceCheck.check(data,mNumberView)


            return convertView
        }

    }
}
