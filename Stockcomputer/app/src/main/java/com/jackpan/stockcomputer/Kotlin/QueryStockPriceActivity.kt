package com.jackpan.stockcomputer.Kotlin

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.Data.StockPriceData
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class QueryStockPriceActivity : BaseAppCompatActivity() {
    lateinit var mAdView: AdView
    lateinit var mStockSearch: EditText
    lateinit var mSearchBtn: Button
    lateinit var mListView: ListView
    val stockPriceDataList = ArrayList<StockPriceData>()
    var mAdapter: MyAdapter? = null
    lateinit var mProgressDialog :ProgressDialog
    @BindView(R.id.stocknametext)
    lateinit var mStockNameText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_stock_price)
        ButterKnife.bind(this)
        if (!checkNetWork()) {
            return
        }
        title = getString(R.string.title_activity_querystockprice)
        setAdView()
        mStockSearch = findViewById(R.id.stocknumber_edt)
        mSearchBtn = findViewById(R.id.searchbtn)
        mListView = findViewById(R.id.stocklist)
        mAdapter = MyAdapter(stockPriceDataList)
        mListView.adapter = mAdapter
        mSearchBtn.setOnClickListener(View.OnClickListener {
            var number: String = mStockSearch.text.toString().trim()
            if (!number.isEmpty()) {
                setDatePickerDialog(number)

            }

        })


    }

    fun setAdView() {

        mAdView = findViewById(R.id.adView)
        val mAdRequest = AdRequest.Builder().build()
        mAdView.loadAd(mAdRequest)
    }

    fun setDatePickerDialog(number: String) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            var YearString: String
            var MonthString: String
            var DayString: String
            YearString = year.toString()
            if ((monthOfYear + 1) <= 9) {
                MonthString = "0" + (monthOfYear + 1).toString()
            } else {
                MonthString = (monthOfYear + 1).toString()

            }
            if ((dayOfMonth) <= 9) {
                DayString = "0" + (dayOfMonth).toString()
            } else {
                DayString = (dayOfMonth).toString()
            }
            setStockData(YearString, MonthString, DayString, number)

        }, year, month, day)
        dpd.show()

    }

    private fun setStockData(year: String, month: String, day: String, number: String) {
        setLogger(year)
        setLogger(month)
        setLogger(day)
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("讀取中")
        mProgressDialog.setMessage("請稍候")
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()

        object : Thread() {
            lateinit var mStockName :String
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("http://www.tse.com.tw/exchangeReport/STOCK_DAY?response=html&date=" + year + month + day + "+&stockNo=" + number).get()
                    setLogger(doc.select("table>tbody>tr").size.toString())//
                    setLogger(doc.select("th[colspan=9]").text())//抓股票名稱

                    if(doc.select("table>tbody>tr").size==0){
                        runOnUiThread {
                            showToast("輸入錯誤！請重新查詢")
                            mProgressDialog.dismiss()

                        }
                        return

                    }
                    mStockName = doc.select("th[colspan=9]").text()



                    for (i in 0..0) {

                        //                        for (int i1 = 0; i1 < doc.select("table>tbody>tr").get(i).select("td").size(); i1++) {
                        //                            Log.d(TAG, "run: "+doc.select("table>tbody>tr").get(i).select("td").get(i));
                        //                        }
                        for (td in doc.select("table>tbody>tr")) {
                            setLogger(td.text())
                            var mStockPriceData = StockPriceData()
                            mStockPriceData.setStockPriceDataArray(td.text())
                            mStockPriceData.getSolitArrayData()
                            stockPriceDataList.add(mStockPriceData)
                            runOnUiThread({
                                mStockNameText.text = mStockName
                                mAdapter?.notifyDataSetChanged()
                                mProgressDialog.dismiss()
                            })
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    setLogger(e.message)

                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    setLogger(e.message)
                }


            }
        }.start()

    }

    inner class MyAdapter(private var mDatas: ArrayList<StockPriceData>?) : BaseAdapter() {
        fun updateData(datas: ArrayList<StockPriceData>) {
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
                convertView = LayoutInflater.from(this@QueryStockPriceActivity).inflate(
                        R.layout.stockprice_layout, null)
                viewHolder = ViewHolder(convertView)
                convertView!!.tag = viewHolder
            }
            setLogger(position.toString())
            setLogger(data.getStockPriceDataArray())
//            val adRequest = AdRequest.Builder().build()
//            viewHolder.adView.loadAd(adRequest)
            viewHolder.mDateTextView.text = data.data
            viewHolder.mDealTextView.text = data.deal
            viewHolder.mMoneyTextView.text = data.money
            viewHolder.mOpeningTextView.text = data.opening
            viewHolder.mHighTextView.text = data.high
            viewHolder.mLowTextView.text = data.low
            viewHolder.mCloseingTextView.text = data.closeing
            viewHolder.mDifferenceTextView.text = data.difference
            viewHolder.mTurnoverTextView.text = data.turnover

            return convertView
        }

    }


    internal class ViewHolder(v: View) {
        @BindView(R.id.adView)
        lateinit var adView: AdView
        @BindView(R.id.datetext)
        lateinit var mDateTextView: TextView
        @BindView(R.id.dealtext)
        lateinit var mDealTextView: TextView
        @BindView(R.id.moneytext)
        lateinit var mMoneyTextView: TextView
        @BindView(R.id.openingtext)
        lateinit var mOpeningTextView: TextView
        @BindView(R.id.hightext)
        lateinit var mHighTextView: TextView
        @BindView(R.id.lowtext)
        lateinit var mLowTextView: TextView
        @BindView(R.id.closeingtext)
        lateinit var mCloseingTextView: TextView
        @BindView(R.id.differencetext)
        lateinit var mDifferenceTextView: TextView
        @BindView(R.id.turnovertext)
        lateinit var mTurnoverTextView: TextView

        init {
            ButterKnife.bind(this, v)
        }
    }
}
