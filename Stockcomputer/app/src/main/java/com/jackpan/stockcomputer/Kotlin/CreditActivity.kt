package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import com.vpadn.ads.VpadnInReadAd

class CreditActivity : BaseAppCompatActivity() {
    // 宣告 VpadnInReadAd
    private val inReadAd: VpadnInReadAd? = null

    // 請將 License Key 換成 Vpon BD 提供您的 License Key
    private val licenseKey = "License Key"

    // 請替換成欲顯示廣告的位置
    val AD_POSITION = 15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit)
    }
}
