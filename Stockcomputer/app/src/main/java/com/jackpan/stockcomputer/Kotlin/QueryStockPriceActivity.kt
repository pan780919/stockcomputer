package com.jackpan.stockcomputer.Kotlin

import android.app.DatePickerDialog
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
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("http://www.tse.com.tw/exchangeReport/STOCK_DAY?response=html&date=" + year + month + day + "+&stockNo=" + number).get()

                    setLogger(doc.select("table>tbody>tr").size.toString())
                    for (i in 0..0) {

                        //                        for (int i1 = 0; i1 < doc.select("table>tbody>tr").get(i).select("td").size(); i1++) {
                        //                            Log.d(TAG, "run: "+doc.select("table>tbody>tr").get(i).select("td").get(i));
                        //                        }
                        for (td in doc.select("table>tbody>tr")) {
                            setLogger(td.text())
                            var mStockPriceData = StockPriceData()
                            mStockPriceData.setStockPriceDataArray(td.text())
                            stockPriceDataList.add(mStockPriceData)
                            runOnUiThread({
                                mAdapter?.notifyDataSetChanged()
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
//            viewHolder.adView!!.loadAd(adRequest)
//            viewHolder.mDateTextView!!.text = data.data
//            if (data.state == 1) {
//                viewHolder.mStateTextView!!.text = "賺"
//                viewHolder.mStateTextView!!.setTextColor(Color.RED)
//                viewHolder.mSellPayment!!.text = "賺到:" + data.price + ""
//                viewHolder.mSellPayment!!.setTextColor(Color.RED)
//            } else {
//                viewHolder.mStateTextView!!.text = "賠"
//                viewHolder.mStateTextView!!.setTextColor(Color.GREEN)
//                viewHolder.mSellPayment!!.text = "賠了:" + data.price + ""
//                viewHolder.mSellPayment!!.setTextColor(Color.GREEN)
//            }
//            viewHolder.mSellPrice!!.text = "股價：" + data.stockPrice + ""
//            viewHolder.mPriceToatal!!.text = "賣價:" + data.stockTotal + ""

            return convertView
        }

    }

    internal class ViewHolder(v: View) {
        @BindView(R.id.adView)
        var adView: AdView? = null
        @BindView(R.id.datetext)
        var mDateTextView: TextView? = null
        @BindView(R.id.dealtext)
        var mDealTextView: TextView? = null
        @BindView(R.id.moneytext)
        var mMoneyTextView: TextView? = null
        @BindView(R.id.openingtext)
        var mOpeningTextView: TextView? = null
        @BindView(R.id.hightext)
        var mHighTextView: TextView? = null
        @BindView(R.id.lowtext)
        var mLowTextView: TextView? = null
        @BindView(R.id.closeingtext)
        var mCloseingTextView: TextView? = null
        @BindView(R.id.differencetext)
        var mDifferenceTextView: TextView? = null
        @BindView(R.id.turnovertext)
        var mTurnoverTextView: TextView? = null

        init {
            ButterKnife.bind(this, v)
        }
    }
}
