package com.jackpan.stockcomputer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfitAndLossActvity extends BaseAppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.edt_input_buy_amount)
    EditText mBuyAmount;
    @BindView(R.id.edt_input_buy_amount_all)
    EditText mBuyAllAmount;
    @BindView(R.id.textView_input_buy_payment)
    TextView mBuyPayment;
    @BindView(R.id.textView_input_sell_amount)
    EditText mSellAmount;
    @BindView(R.id.edt_sell_amount_all)
    EditText mSellAllAmount;
    @BindView(R.id.textView_sell_payment)
    TextView mSellPayment;
    @BindView(R.id.button_calculate)
    Button mCalculateBtn;
    @BindView(R.id.textView_profit)
    TextView mProfitText;
    @BindView(R.id.textView_profitability)
    TextView mProfitabilityText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss_actvity);
        ButterKnife.bind(this);
        checkNetWork();
        setSupportActionBar(toolbar);
        setAdView();

    }


    private void setAdView() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private static final String TAG = "ProfitAndLossActvity";
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (mBuyAmount.getText().toString().trim().isEmpty() || mBuyAllAmount.getText().toString().trim().isEmpty() ||
                mSellAmount.getText().toString().trim().isEmpty() || mSellAllAmount.getText().toString().trim().isEmpty()) {
            showToast("請檢查是否有地方尚未填寫！");
            return;
        }
        Double mBuyPrice = Double.parseDouble(mBuyAmount.getText().toString().trim());
        long mBuyAmount = Integer.parseInt(mBuyAllAmount.getText().toString().trim());
        Double mSellPrice = Double.parseDouble(mSellAmount.getText().toString().trim());
        long mSellAmount = Integer.parseInt(mSellAllAmount.getText().toString().trim());
        long buyProcedures = Math.round((mBuyPrice * mBuyAmount) * 0.001425);
        long sellProcedures = Math.round((mSellPrice * mSellAmount) * 0.001425);
        long taxPayment = Math.round((mSellPrice * mSellAmount) * 0.003);

        if (buyProcedures < 20) {
            buyProcedures = 20;
        }
        if (sellProcedures < 20) {
            sellProcedures = 20;
        }
        long buyPrice = Math.round((mBuyPrice * mBuyAmount) + buyProcedures);
        long sellPrice = Math.round((mSellPrice * mSellAmount) - sellProcedures - taxPayment);
        long price = sellPrice - buyPrice;

        for(double i = 0; i<5;i=i+0.1){
            Log.d(TAG, "buyPrice: "+(mBuyPrice));
            Log.d(TAG, "i: "+(i));
            Log.d(TAG, "buyPrice+i: "+(mBuyPrice+i));
            Log.d(TAG, "mBuyAmount: "+mBuyAmount);
            Log.d(TAG, "(buyPrice+i) * mBuyAmount: "+(mBuyPrice+i) * mBuyAmount);
           long n =  Math.round(((mBuyPrice+i) * mBuyAmount) - sellProcedures - taxPayment);
            Log.d(TAG, "n: "+n);
           long n1 =n-buyPrice;
            Log.d(TAG, "n1: "+n1);
            Log.d(TAG, "calculate: "+"===========");
            if(n1>0){
                Log.d(TAG, "calculate: "+"開始賺錢"+i);
                Log.d(TAG, "calculate: "+(buyPrice+i));
                Log.d(TAG, "calculate: "+"===========");
            }else {
                Log.d(TAG, "calculate: "+"會賠錢"+i);
                Log.d(TAG, "calculate: "+(buyPrice+i));
                Log.d(TAG, "calculate: "+"===========");

            }

        }
        if (price < 0) {
            mProfitText.setTextColor(Color.GREEN);
        } else {
            mProfitText.setTextColor(Color.RED);
        }

        Double profitability = (price / mBuyPrice) * 0.01;

        if (profitability < 0) {
            mProfitabilityText.setTextColor(Color.GREEN);
        } else {
            mProfitabilityText.setTextColor(Color.RED);
        }
        mBuyPayment.setText(String.valueOf(buyPrice));
        mSellPayment.setText(String.valueOf(sellPrice));
        mProfitText.setText(String.valueOf(price));
        mProfitabilityText.setText(profitability + "%");


    }

}
