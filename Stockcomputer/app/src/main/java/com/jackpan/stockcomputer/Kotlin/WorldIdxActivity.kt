package com.jackpan.stockcomputer.Kotlin

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_world_idx.*
import org.jsoup.Jsoup
import java.io.IOException

class WorldIdxActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.world_1 -> {
                state=0
                getList(Asian_int)

                return@OnNavigationItemSelectedListener true
            }
            R.id.world_2 -> {
                state = 1
                getList(US_int)

                return@OnNavigationItemSelectedListener true
            }
            R.id.world_3 -> {
                state = 2
                getList(Europe_int)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.updatabtn ->{
                when(state){
                    0 ->{
                        getList(Asian_int)
                    }
                    1 ->{
                        getList(US_int)

                    }
                  2 ->{
                      getList(Europe_int)

                  }
                }

            }
        }
    }
    val Asian_int = 0
    val US_int = 1
    val Europe_int = 2
    var state =0
    lateinit var mMyAdapter : MyAdapter
    var dataList :ArrayList<String> =ArrayList()
    lateinit var mListView: ListView
    lateinit var mTitleTextView: TextView
    lateinit var mUpdateButton: ImageView
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_idx)
        initLayout()
    }
    fun initLayout(){
        mAdView = findViewById(R.id.adbertADView)
        var adRequset = AdRequest.Builder().build()
        mAdView.loadAd(adRequset)
        mUpdateButton = findViewById(R.id.updatabtn)
        mTitleTextView = findViewById(R.id.title)
        mListView = findViewById(R.id.fgbuylistview)
        mMyAdapter = MyAdapter(dataList)
        mListView.adapter = mMyAdapter
        mUpdateButton.setOnClickListener(this)
        worldIdx_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()
        when(state){
            0 ->{
                getList(Asian_int)
            }
            1 ->{
                getList(US_int)

            }
            2 ->{
                getList(Europe_int)

            }
        }    }

    fun getList(int: Int) {
        dataList.clear()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("讀取中")
        progressDialog.show()
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/us/worldidx.php").get()
                    for (element in doc.select("table[border=0][cellpadding=4][cellspacing=1][width=100%]")) {
                        when(int){
                           0 ->{
                                for (i in 3..14) {
                                    dataList.add(element.select("tr").get(i).text())
                                    runOnUiThread(Runnable {
                                        mTitleTextView.text = element.select("tr").get(1).text()
                                        mMyAdapter.notifyDataSetChanged()
                                        progressDialog.dismiss()

                                    })
                                }
                            }
                           1 ->{
                                for (i in 18..23) {
                                    dataList.add(element.select("tr").get(i).text())
                                    runOnUiThread(Runnable {
                                        mTitleTextView.text = element.select("tr").get(1).text()
                                        mMyAdapter.notifyDataSetChanged()
                                        progressDialog.dismiss()

                                    })                                }
                            }
                           2 ->{
                                for (i in 27..29) {
                                    dataList.add(element.select("tr").get(i).text())
                                    runOnUiThread(Runnable {
                                        mTitleTextView.text = element.select("tr").get(1).text()
                                        mMyAdapter.notifyDataSetChanged()
                                        progressDialog.dismiss()

                                    })                                }
                            }
                        }
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
            val data = mAllData!![position]

            var mString = data.split(" ")
            if (convertView == null)
                convertView = LayoutInflater.from(this@WorldIdxActivity).inflate(
                        R.layout.layout_conceptdetail, null)

            var mNumberView: TextView = convertView!!.findViewById(R.id.stocknumbertext)
            mNumberView.text = data
            for (s in mString) {
                if (s.contains("%")){
                    if (s.contains("-")){
                        mNumberView.setTextColor(Color.GREEN)
                    }else if(s.contains("+")){
                        mNumberView.setTextColor(Color.RED)

                    }
                }
            }

            return convertView
        }

    }
}
