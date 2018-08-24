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
import org.jsoup.Jsoup


class ConceptActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    lateinit var mMyAdapter : MyAdapter

    var dataList :ArrayList<ConceptData> =ArrayList()
    lateinit var mListView: ListView
    val mUrlString :String = "https://tw.stock.yahoo.com/";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concept)
        mListView = findViewById(R.id.listView)
        mAdView = findViewById(R.id.adView_page)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
        mMyAdapter = MyAdapter(dataList)
        mListView.adapter = mMyAdapter
        getList()
        mListView.setOnItemClickListener { parent, view, position, id ->
            Log.d("mListView",mMyAdapter.mAllData?.get(position)?.url)

            Log.d("mListView",mUrlString+mMyAdapter.mAllData?.get(position)?.url)
            Log.d("mListView",mMyAdapter.mAllData?.get(position)?.title)

        }

    }


    fun getList(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("讀取中")
        progressDialog.show()
        Thread{

            run {

                try {

                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/h/getclass.php#table7").get()
                    for (tr in doc.select("table[border=0][cols=2][cellspacing=0][cellpadding=7]").get(3).select("tr")) {
                        Log.d("getList",tr.text())
                        var mConceptData = ConceptData()

                        for (td in tr.select("td")) {
                            Log.d("getList",td.text())
                            mConceptData.setTitle(td.text())

                            val url = td.select("a").attr("href")
                            Log.d("getList",url)

                            mConceptData.setUrl(url)

                        }
                        dataList.add(mConceptData)

                        runOnUiThread(Runnable {
                            mMyAdapter.notifyDataSetChanged()
                            progressDialog.dismiss()

                        })


                    }

                } catch (e: Exception) {
                }
            }
        }.start()
        }
    inner class MyAdapter(var mAllData: ArrayList<ConceptData>?) : BaseAdapter() {
        fun updateData(datas: ArrayList<ConceptData>) {
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
                convertView = LayoutInflater.from(this@ConceptActivity).inflate(
                        R.layout.layout_concept_item, null)
            var mTitleTextView: TextView = convertView!!.findViewById(R.id.title)
            mTitleTextView.text = data.title
            Log.d("getView",data.title)

            return convertView
        }

    }
}
