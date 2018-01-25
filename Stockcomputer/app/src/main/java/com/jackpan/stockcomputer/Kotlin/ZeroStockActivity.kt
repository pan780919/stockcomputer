package com.jackpan.stockcomputer.Kotlin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R

class ZeroStockActivity : BaseAppCompatActivity() {
    lateinit var mPrcieEditText :EditText
    lateinit var mTotalEditText :EditText
    lateinit var mStockTotalText :TextView
    lateinit var mStockPriceText :TextView
    lateinit var mStockTotal :TextView
    lateinit var mStockIdeaText :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zero_stock)
        var mTitleTextView :TextView = findViewById(R.id.titletextview)
        mPrcieEditText = findViewById(R.id.price_edittext)
        mTotalEditText = findViewById(R.id.total_edittext)
        mStockTotalText = findViewById(R.id.stocktotal_text)
        mStockPriceText = findViewById(R.id.stockpricet_text)
        mStockTotal = findViewById(R.id.stocktotal)
        mStockIdeaText = findViewById(R.id.stockidea_text)
        var mCalculate :Button = findViewById(R.id.button_calculate)
        mTitleTextView.text = getString(R.string.activity_zerostock_title)

        mCalculate.setOnClickListener(View.OnClickListener { calculatePrcie() })

    }
    fun calculatePrcie(){
        var price :String
        var total :String
        price = mPrcieEditText.text.toString().trim()
        total = mTotalEditText.text.toString().trim()
        if (!price.isEmpty()||!total.isEmpty()){
            if(!price.isEmpty()){

                var priceDouble :Double  = Math.round(14035/price.toDouble()).toDouble()
                mStockTotalText.text =priceDouble.toString()
                var sellPrice :Double =(priceDouble * 0.001425 * price.toDouble())
                if(sellPrice<20){
                    sellPrice = 20.0
                }
                var priceString :String = Math.round(((priceDouble * price.toDouble())+sellPrice)).toString()
                mStockPriceText.text = priceString+"(股價:"+Math.round(((priceDouble * price.toDouble()))).toString()+"手續費："+sellPrice+")"

            }
            if(!price.isEmpty()&&!total.isEmpty()){


                var priceDouble :Double  = Math.round(14035/price.toDouble()).toDouble()
                mStockTotalText.text =priceDouble.toString()
                var sellPrice :Double = (total.toDouble() * 0.001425 * price.toDouble())
                if(sellPrice<=20){
                    sellPrice = 20.0
                }
                var priceString :String = Math.round(((total.toDouble() * price.toDouble())+ sellPrice)).toString()
                mStockPriceText.text = priceString+"(股價:"+Math.round(((total.toDouble() * price.toDouble()))).toString()+"手續費："+sellPrice+")"
                mStockTotal.text = total
                if(total>=priceDouble.toString()){
                    mStockIdeaText.text = "合理股數"

                }else{
                    mStockIdeaText.text = "不划算股數"

                }

            }




        }else{
            setAlertDilog("提示","至少輸入一項才能試算唷！！")

            return
        }

    }
    fun setAlertDilog(title:String,message:String){
        val mAlertDilog = AlertDialog.Builder(this).create()
        mAlertDilog.setTitle(title)
        mAlertDilog.setMessage(message)
        mAlertDilog.setButton(AlertDialog.BUTTON_POSITIVE,"確定", DialogInterface.OnClickListener { dialogInterface, i ->
            mAlertDilog.dismiss()
        })
        mAlertDilog.show()


    }
}
