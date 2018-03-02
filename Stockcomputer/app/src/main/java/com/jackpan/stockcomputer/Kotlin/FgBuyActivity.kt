package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_fg_buy.*

class FgBuyActivity : BaseAppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fgbuy_tse -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgbuy_tse_w -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.fgsell_tse_w ->{

                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }
    @BindView(R.id.adbertADView)
    lateinit var mAdbertView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fg_buy)
        ButterKnife.bind(this)
        setAdmob()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun setAdmob(){
        val mAdRequest =AdRequest.Builder().build()
        mAdbertView.loadAd(mAdRequest)


    }
    fun getFgBuyTse(){

        Thread(){
            run {

                runOnUiThread {

                }
            }

        }.start()
    }
}
