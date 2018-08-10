package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.login.LoginManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.Data.MemberData
import com.jackpan.stockcomputer.Data.MyApi
import com.jackpan.stockcomputer.MySharedPrefernces
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_member_center.*

class MemberCenterActivity : BaseAppCompatActivity() {
    @BindView(R.id.userimg)
    lateinit var mUserImg: ImageView
    @BindView(R.id.userid)
    lateinit var mUserIdTextView: TextView
    @BindView(R.id.username)
    lateinit var mUserNameTextView: TextView
    @BindView(R.id.userpoint)
    lateinit var mUserMailTextView: TextView
    @BindView(R.id.userlv)
    lateinit var mUserLVTextView: TextView
    @BindView(R.id.pointbutton)
    lateinit var mPointButton: Button
    lateinit var mRewardedVideoAd: RewardedVideoAd
    var TAG: String = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_center)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)
        setTitle("會員中心")
        getUserState()
        checkUserLv()
        setRewardedVideoAd()


    }

    @OnClick(R.id.pointbutton)
    fun getPoint() {
        if (mRewardedVideoAd.isLoaded) {
            mRewardedVideoAd.show()
        }

    }

    public override fun onPause() {
        mRewardedVideoAd.pause(this)
        super.onPause()
    }

    public override fun onDestroy() {
        mRewardedVideoAd.destroy(this)
        super.onDestroy()
    }

    fun getUserState() {
        var userId: String = MySharedPrefernces.getUserId(this)
        var userName: String = MySharedPrefernces.getUserName(this)
        var userPoint: String = MySharedPrefernces.getUserPoint(this)
        var userPhoto: String = MySharedPrefernces.getUserPhoto(this)
        var userLv: String = MySharedPrefernces.getUserlv(this)

        if (!userId.equals("")) {
            mUserIdTextView.text = userId
        }

        if (!userName.equals("")) {
            mUserNameTextView.text = userName
        }

        if (!userPoint.equals("")) {
            mUserMailTextView.text = userPoint.toString()
        }
        if (!userPhoto.equals("")) {
            MyApi.loadImage(userPhoto, mUserImg, this)

        }
    }

    fun checkUserLv() {
        var pointString: String = MySharedPrefernces.getUserPoint(this)
        var point: Int = pointString.replace("\"", "").toInt()
        if (point <= 999) {
            mUserLVTextView.text = MemberData.MEMBER_LV_1

        } else if (point <= 1999 && point >= 1000) {

            mUserLVTextView.text = MemberData.MEMBER_LV_2

        } else if (point <= 2999 && point >= 2000) {
            mUserLVTextView.text = MemberData.MEMBER_LV_3

        } else if (point <= 3999 && point >= 3000) {
            mUserLVTextView.text = MemberData.MEMBER_LV_4

        } else if (point <= 4999 && point >= 4000) {
            mUserLVTextView.text = MemberData.MEMBER_LV_5

        }


    }

    @OnClick(R.id.userlogoutbtn)
    fun setUserLoginButton() {
        MySharedPrefernces.saveUserId(this, "")
        MySharedPrefernces.saveUserName(this, "")
        MySharedPrefernces.saveUserMail(this, "")
        MySharedPrefernces.saveUserPhoto(this, "")
        LoginManager.getInstance().logOut()
        finish()


    }

    private fun setRewardedVideoAd() {
        MobileAds.initialize(this,
                "ca-app-pub-7019441527375550~1705354228")
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.loadAd("ca-app-pub-7019441527375550/1968113408",
                AdRequest.Builder().build())

        mRewardedVideoAd.setRewardedVideoAdListener(object : RewardedVideoAdListener {
            override fun onRewardedVideoCompleted() {
            }

            override fun onRewarded(reward: RewardItem) {
                Log.d(TAG, reward.amount.toString())
                Log.d(TAG, reward.type)

                // Reward the user.

            }

            override fun onRewardedVideoAdLeftApplication() {

            }

            override fun onRewardedVideoAdClosed() {}

            override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
            }

            override fun onRewardedVideoAdLoaded() {}

            override fun onRewardedVideoAdOpened() {}

            override fun onRewardedVideoStarted() {}
        })

    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(this)

    }
}
