package com.jackpan.stockcomputer.Kotlin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.jackpan.stockcomputer.R
import org.jsoup.Jsoup
import java.io.IOException
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso


class CreditActivity : Activity() {
    var dataList :ArrayList<String> =ArrayList()
    lateinit var mLayout: LinearLayout
    lateinit var mImgLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit)
        mLayout = findViewById(R.id.layout_1)
        mImgLayout = findViewById(R.id.layout_2)
        getList()
    }

    fun getList(){
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val doc = Jsoup.connect("https://tw.stock.yahoo.com/d/i/credit.html").get()
                    for (i in 1 until doc.select("table[border=0][cellspacing=1][cellpadding=3]").size) {

                        for (a in 1 until doc.select("table[border=0][cellspacing=1][cellpadding=3]").get(i).select("tr").size - 3) {
                            runOnUiThread {
                                addTextView(doc.select("table[border=0][cellspacing=1][cellpadding=3]").select("tr").get(a).text(),mLayout)

                            }
                        }
                    }
                    for (element in doc.select("table[border=0][cellspacing=7][cellpadding=2]")) {
                        for (tr in element.select("tr")) {
                            for (p in tr.getElementsByTag("p")) {
                                val title = p.select("[src]")

                                for (src in title) {

                                    if (src.tagName().equals("img", ignoreCase = true)) {
                                        dataList.add(src.attr("abs:src"))

                                    }

                                }
                            }
                        }
                    }
                    runOnUiThread {
                        for (i in 0..1){
                            addImg(dataList.get(i),mImgLayout)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }.start()

    }


    fun addTextView(s: String, layout: LinearLayout) {
        var view: View = LayoutInflater.from(this@CreditActivity).inflate(R.layout.layout_conceptdetail, layout, false)
        val mTitleTextView: TextView = view.findViewById(R.id.stocknumbertext)
        mTitleTextView.text =s
        layout.addView(view)
    }

    fun addImg(s: String, layout: LinearLayout) {
        var view: View = LayoutInflater.from(this@CreditActivity).inflate(R.layout.layout_img, layout, false)
        val mImageView: ImageView = view.findViewById(R.id.img)
        Picasso.get().load(s).into(mImageView);

        layout.addView(view)
    }
}
