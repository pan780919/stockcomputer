package com.jackpan.stockcomputer.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        long taxPayment = Math.round((BuyPrice * BuyAmount) * 0.003);

        if (buyProcedures < 20) {
            buyProcedures = 20;
        }
        if (sellProcedures < 20) {
            sellProcedures = 20;
        }
        long buyPrice = Math.round((BuyPrice * BuyAmount) + buyProcedures);
        mPrice.setText(buyPrice+"");
        for(double i = 0; i<5;i=i+0.1){
            CalculateData cal = new CalculateData();
            long n =  Math.round(((BuyPrice+i) * BuyAmount) - sellProcedures - taxPayment);
            cal.setStockPrice((BuyPrice+i));
            cal.setStockTotal(n);
            long n1 =n-buyPrice;
            cal.setPrice(n1);
            if(n1>0){
                cal.setState(1);
            }else {
                cal.setState(0);

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
            ViewHolder viewHolder;
            CalculateData data = mDatas.get(position);
            if(convertView!=null){
                viewHolder = (ViewHolder)convertView.getTag();
            }else {
                convertView = LayoutInflater.from(CalculateActivity.this).inflate(
                        R.layout.layout_calculate, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            AdRequest adRequest = new AdRequest.Builder().build();
            viewHolder.adView.loadAd(adRequest);
            if(data.state==1){
               viewHolder.mStateTextView.setText("賺");
                viewHolder.mStateTextView.setTextColor(Color.RED);
                viewHolder.mSellPayment.setText("賺到:"+data.price+"");
                viewHolder.mSellPayment.setTextColor(Color.RED);
            }else {
                viewHolder.mStateTextView.setText("賠");
                viewHolder.mStateTextView.setTextColor(Color.GREEN);
                viewHolder.mSellPayment.setText("賠了:"+data.price+"");
                viewHolder.mSellPayment.setTextColor(Color.GREEN);
            }
            viewHolder.mSellPrice.setText("股價："+data.stockPrice+"");
            viewHolder.mPriceToatal.setText("賣價:"+data.stockTotal+"");

            return convertView;
        }

    }
    static class  ViewHolder{
        @BindView(R.id.adView) AdView adView;
        @BindView(R.id.text_state)
        TextView mStateTextView;
        @BindView(R.id.textView_sell_price)
        TextView mSellPrice;
        @BindView(R.id.textView_sell_payment)
        TextView mSellPayment;
        @BindView(R.id.textView_sell_pricetotal)
        TextView mPriceToatal;

        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}