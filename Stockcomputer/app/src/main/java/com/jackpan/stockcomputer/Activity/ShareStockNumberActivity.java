package com.jackpan.stockcomputer.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jackpan.stockcomputer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareStockNumberActivity extends BaseAppCompatActivity {
    private MyAdapter mAdapter;
    private static final String TAG = "ShareStockNumberActivit";
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.listView1) ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_stock_number);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ArrayList<String> mAllData = new ArrayList<>();
        mAllData.add("1");
        mAllData.add("2");
        mAllData.add("3");
        mAllData.add("4");
        mAllData.add("5");
        mAllData.add("6");
        mAdapter = new MyAdapter(mAllData);

        mListView.setAdapter(mAdapter);

    }
    public class MyAdapter extends BaseAdapter {
        private ArrayList<String> mDatas;

        public MyAdapter(ArrayList<String> datas) {
            mDatas = datas;
        }
        public void updateData(ArrayList<String> datas) {
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
                convertView = LayoutInflater.from(ShareStockNumberActivity.this).inflate(
                        R.layout.layout_report, null);
            AdView adView = (AdView) convertView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setVisibility(View.INVISIBLE);
            if(position>=2&&position%2==0){
                adView.setVisibility(View.VISIBLE);

            }
//            TextView textname = (TextView) convertView.findViewById(R.id.name);
//            TextView list = (TextView) convertView.findViewById(R.id.txtengname);
//            TextView bigtext= (TextView) convertView.findViewById(R.id.bigtext);
//            TextView place= (TextView) convertView.findViewById(R.id.palace);
//            TextView time= (TextView) convertView.findViewById(R.id.time);
//            textname.setText("體型:"+data.animal_bodytype);
//            list.setText("年紀:"+data.animal_age);
//            bigtext.setText(data.animal_kind);
//            place.setText("實際所在地:"+data.animal_place);
//            time.setText("開放認養時間:"+data.animal_opendate);
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.photoimg);
//            //			loadImage(data.album_file, img);
//            //			Glide.with(MainActivity.this).load(data.album_file).into(imageView);
//
//            Glide.with(MainActivity.this)
//                    .load(data.album_file)
//                    .centerCrop()
//                    .placeholder(R.drawable.nophoto)
//                    .crossFade()
//                    .into(imageView);
            return convertView;
        }

    }
}
