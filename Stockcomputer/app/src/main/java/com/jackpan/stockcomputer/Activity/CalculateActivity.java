package com.jackpan.stockcomputer.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jackpan.stockcomputer.Data.CalculateData;
import com.jackpan.stockcomputer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculateActivity extends BaseAppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.cal_edt_input_buy_amount)
    EditText mBuyPrice;
    @BindView(R.id.cal_edt_input_buy_amount_all)
    EditText mBuyTotal;
    @BindView(R.id.cal_textView_input_buy_payment)
    TextView mPrice;
    @BindView(R.id.listView_cal)
    ListView mListView;
    private static final String TAG = "CalculateActivity";
    CalculateData cal = new CalculateData();
    ArrayList<CalculateData>calList = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mAdapter = new MyAdapter(calList);

        mListView.setAdapter(mAdapter);


    }
    @OnClick(R.id.cal_button_calculate)
    public void calculate(){
        if(mBuyPrice.getText().toString().trim().isEmpty()||
                mBuyTotal.getText().toString().trim().isEmpty()){
            showToast("請檢查是否尚未輸入數值！！");
             return;
        }
        Double BuyPrice = Double.parseDouble(mBuyPrice.getText().toString().trim());
        long BuyAmount = Integer.parseInt(mBuyTotal.getText().toString().trim());
        long buyProcedures = Math.round((BuyPrice * BuyAmount) * 0.001425);
        long sellProcedures = Math.round((BuyPrice * BuyAmount) * 0.001425);
        long taxPayment = Math.round((BuyAmount * BuyAmount) * 0.003);

        if (buyProcedures < 20) {
            buyProcedures = 20;
        }
        if (sellProcedures < 20) {
            sellProcedures = 20;
        }
        long buyPrice = Math.round((BuyPrice * BuyAmount) + buyProcedures);

        for(double i = 0; i<5;i=i+0.1){
            Log.d(TAG, "buyPrice: "+(BuyPrice));

            Log.d(TAG, "i: "+(i));
            Log.d(TAG, "buyPrice+i: "+(BuyPrice+i));
            Log.d(TAG, "mBuyAmount: "+BuyAmount);
            Log.d(TAG, "(buyPrice+i) * mBuyAmount: "+(BuyPrice+i) * BuyAmount);
            long n =  Math.round(((BuyPrice+i) * BuyAmount) - sellProcedures - taxPayment);
            cal.setStockPrice(n);
            Log.d(TAG, "n: "+n);
            long n1 =n-buyPrice;
            cal.setPrice(n1);

            Log.d(TAG, "n1: "+n1);
            Log.d(TAG, "calculate: "+"===========");
            if(n1>0){
                cal.setState(1);
                Log.d(TAG, "calculate: "+"開始賺錢"+i);
                Log.d(TAG, "calculate: "+(buyPrice+i));
                Log.d(TAG, "calculate: "+"===========");
            }else {
                cal.setState(0);
                Log.d(TAG, "calculate: "+"會賠錢"+i);
                Log.d(TAG, "calculate: "+(buyPrice+i));
                Log.d(TAG, "calculate: "+"===========");

            }
            calList.add(cal);
        }
        mAdapter.notifyDataSetChanged();

    }
    public class MyAdapter extends BaseAdapter {
        private ArrayList<CalculateData> mDatas;

        public MyAdapter(ArrayList<CalculateData> datas) {
            mDatas = datas;
        }
        public void updateData(ArrayList<CalculateData> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(CalculateActivity.this).inflate(
                        R.layout.layout_calculate, null);
            AdView adView = (AdView) convertView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            return convertView;
        }

    }
}