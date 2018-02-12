package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import butterknife.ButterKnife
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_member_center.*

class MemberCenterActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_center)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)
        setTitle("會員中心")


    }

}
