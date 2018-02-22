package com.jackpan.stockcomputer.Kotlin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.login.LoginManager
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity
import com.jackpan.stockcomputer.Data.MyApi
import com.jackpan.stockcomputer.MySharedPrefernces
import com.jackpan.stockcomputer.R
import kotlinx.android.synthetic.main.activity_member_center.*

class MemberCenterActivity : BaseAppCompatActivity() {
    @BindView(R.id.userimg)
    lateinit var mUserImg :ImageView
    @BindView(R.id.userid)
    lateinit var mUserIdTextView: TextView
    @BindView(R.id.username)
    lateinit var mUserNameTextView: TextView
    @BindView(R.id.usermail)
    lateinit var mUserMailTextView: TextView
    @BindView(R.id.userlv)
    lateinit var mUserLVTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_center)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)
        setTitle("會員中心")
        getUserState()




    }
    fun getUserState() {
        var userId: String = MySharedPrefernces.getUserId(this)
        var userName: String = MySharedPrefernces.getUserName(this)
        var userEmail: String = MySharedPrefernces.getUserMail(this)
        var userPhoto: String = MySharedPrefernces.getUserPhoto(this)

        if (!userId.equals("")){
            mUserIdTextView.text = userId
        }

        if (!userName.equals("")){
            mUserNameTextView.text = userName
        }

        if(!userEmail.equals("")){
            mUserMailTextView.text = userEmail
        }
        if(!userPhoto.equals("")){
            MyApi.loadImage(userPhoto,mUserImg,this)

        }
    }

    @OnClick(R.id.userlogoutbtn)
    fun setUserLoginButton(){
        MySharedPrefernces.saveUserId(this,"")
        MySharedPrefernces.saveUserName(this,"")
        MySharedPrefernces.saveUserMail(this,"")
        MySharedPrefernces.saveUserPhoto(this,"")
        LoginManager.getInstance().logOut()
        finish()


    }

}
