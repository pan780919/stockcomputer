package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

import kotlinx.android.synthetic.main.activity_world_idx.*
import org.jsoup.Jsoup
import java.io.IOException

class WorldIdxActivity : BaseAppCompatActivity() {
    lateinit var mMyAdapter : MyAdapter
    var dataList :ArrayList<String> =ArrayList()
    lateinit var mListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_idx)
        initLayout()
    }
    fun initLayout(){
        mListView = findViewById(R.id.fgbuylistview)
        mMyAdapter = MyAdapter(dataList)
        mListView.adapter = mMyAdapter
    }

    override fun onResume() {
        super.onResume()
        getList()
    }
    fun getList() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("讀取中")
        progressDialog.show()
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/us/worldidx.php").get()
                    for (element in doc.select("table[border=0][cellpadding=4][cellspacing=1][width=100%]")) {
                        for (i in 3..14) {
                            dataList.add(element.select("tr").get(i).text())
                            runOnUiThread(Runnable {
                                mMyAdapter.notifyDataSetChanged()
                                progressDialog.dismiss()

                            })
                        }

                        for (i in 16..23) {
                            //                            Log.d(TAG, "worldidx: " + element.select("tr").get(i).text());
                        }
                        //                            for (Element tr : element.select("tr")) {
                        //                                Log.d(TAG, "run: "+tr.text());
                        //                            }
                    }
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
            Log.d(javaClass.simpleName,mAllData!![0])
            val data = mAllData!![position]
            if (convertView == null)
                convertView = LayoutInflater.from(this@WorldIdxActivity).inflate(
                        R.layout.layout_conceptdetail, null)

            var mNumberView: TextView = convertView!!.findViewById(R.id.stocknumbertext)
            mNumberView.text = data


            return convertView
        }

    }
}
