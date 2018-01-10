package com.jackpan.stockcomputer;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jackpan.stockcomputer.Activity.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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

}
