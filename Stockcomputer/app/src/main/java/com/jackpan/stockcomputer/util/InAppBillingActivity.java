package com.jackpan.stockcomputer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jackpan.stockcomputer.MySharedPrefernces;
import com.jackpan.stockcomputer.R;

import java.util.ArrayList;
import java.util.List;

public class InAppBillingActivity extends Activity {
    static final String ITEM_MY_FREE = "my_free";
    static final String ITEM_SPONSOR_MONth = "sponsor_month";
    static final String ITEM_SPONSOR_YEARS = "sponsor_years";
    static final  String ITEM_SPOMSOR_OTHER ="sponsor_other";
    static final String ITEM_MY_VIP = "my_vip";
    static final String ITEM_1000 = "1000";
    static final String ITEM_100 = "100";
    private Button mUpLoad;
    private ListView mListView;
    IabHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);
        checkbuy();




    }

    //
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.buyButton:
//                mHelper.launchPurchaseFlow(this,ITEM_SKU,10001,mPurchaseFinishedListener, "mypurchasetoken");
//
//
//                break;
//            case R.id.clickButton:
//
//                List additionalSkuList = new ArrayList();
//                additionalSkuList.add("sponsor_years");
//                additionalSkuList.add("sponsor_month");
//                mHelper.queryInventoryAsync(true, additionalSkuList,
//                        mReceivedInventoryListener);
//
//                break;
//
//
//        }
//
//    }

    //給用戶付費內容的訪問和更新UI
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_MY_FREE)) {
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
            }else if (purchase.getSku().equals(ITEM_SPONSOR_MONth)){
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
            }else if (purchase.getSku().equals(ITEM_SPONSOR_YEARS)) {
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);

            }else if (purchase.getSku().equals(ITEM_MY_VIP)) {
                showDilog();
                consumeItem();

            }else if (purchase.getSku().equals(ITEM_1000)){
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
            }else if (purchase.getSku().equals(ITEM_100)){
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
            }
            else if (purchase.getSku().equals(ITEM_SPOMSOR_OTHER)){
                showDilog();
                MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
            }
            else {

                          mHelper.consumeAsync(inventory.getPurchase(ITEM_MY_VIP),
                          mConsumeFinishedListener);

            }
        }
    };


    // 詢問 可以購買名單

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             final Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
                return;
            } else {
//
//
                mListView = (ListView) findViewById(R.id.buyList);



                String myfree =
                        inventory.getSkuDetails(ITEM_MY_FREE).getTitle();
                String month =
                        inventory.getSkuDetails(ITEM_SPONSOR_MONth).getTitle();
                String other =inventory.getSkuDetails(ITEM_SPOMSOR_OTHER).getTitle();
                String years = inventory.getSkuDetails(ITEM_SPONSOR_YEARS).getTitle();
                String vip = inventory.getSkuDetails(ITEM_MY_VIP).getTitle();
                String vip_1000 = inventory.getSkuDetails(ITEM_1000).getTitle();
                String vip_100 = inventory.getSkuDetails(ITEM_100).getTitle();
                ArrayList<String> mylist = new ArrayList<>();
                mylist.add(myfree);
                mylist.add(month);
                mylist.add(years);
                mylist.add(vip);
                mylist.add(vip_1000);
                mylist.add(vip_100);
                mylist.add(other);

                ArrayAdapter<String>myAdapter = new ArrayAdapter<String>(InAppBillingActivity.this,android.R.layout.simple_list_item_1,mylist);
                mListView.setAdapter(myAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position)
                        {
                            case 0:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_MY_FREE).getTitle(), inventory.getSkuDetails(ITEM_MY_FREE).getDescription(), inventory.getSkuDetails(ITEM_MY_FREE).getPrice(),ITEM_MY_FREE);

                                break;
                            case 1:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_SPONSOR_MONth).getTitle(), inventory.getSkuDetails(ITEM_SPONSOR_MONth).getDescription(),inventory.getSkuDetails(ITEM_SPONSOR_MONth).getPrice(),ITEM_SPONSOR_MONth);

                                break;
                            case 2:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_SPONSOR_YEARS).getTitle(), inventory.getSkuDetails(ITEM_SPONSOR_YEARS).getDescription(),inventory.getSkuDetails(ITEM_SPONSOR_YEARS).getPrice(),ITEM_SPONSOR_YEARS);
                                break;
                            case 3:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_MY_VIP).getTitle(), inventory.getSkuDetails(ITEM_MY_VIP).getDescription(),inventory.getSkuDetails(ITEM_MY_VIP).getPrice(),ITEM_MY_VIP);
                                break;
                            case 4:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_1000).getTitle(), inventory.getSkuDetails(ITEM_1000).getDescription(),inventory.getSkuDetails(ITEM_1000).getPrice(),ITEM_1000);
                                break;
                            case 5:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_100).getTitle(), inventory.getSkuDetails(ITEM_100).getDescription(),inventory.getSkuDetails(ITEM_100).getPrice(),ITEM_100);
                                break;
                            case 6:
                                ShowBuyDilog(inventory.getSkuDetails(ITEM_SPOMSOR_OTHER).getTitle(), inventory.getSkuDetails(ITEM_SPOMSOR_OTHER).getDescription(),inventory.getSkuDetails(ITEM_SPOMSOR_OTHER).getPrice(),ITEM_SPOMSOR_OTHER);
                                break;
                        }

                    }
                });


            }

        }
    };

    //  永久商品以消費完畢
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        showDilog();
//                        int index =0;
//                        index++;
                        MySharedPrefernces.saveIsBuyed(InAppBillingActivity.this,true);
//                        Toast.makeText(getApplicationContext(),"已購買:"+index+"次",Toast.LENGTH_SHORT).show();
                    } else {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            switch (requestCode){
                case 0:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_SHORT).show();
                    break;

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkbuy() {

        mHelper = new IabHelper(this, getString(R.string.my_license_key));
        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {


                                           } else {


                                               List additionalSkuList = new ArrayList();
                                               additionalSkuList.add(ITEM_MY_FREE);
                                               additionalSkuList.add(ITEM_SPOMSOR_OTHER);
                                               additionalSkuList.add(ITEM_SPONSOR_MONth);
                                               additionalSkuList.add(ITEM_SPONSOR_YEARS);
                                               additionalSkuList.add(ITEM_MY_VIP);
                                               additionalSkuList.add(ITEM_1000);
                                               additionalSkuList.add(ITEM_100);
                                               mHelper.queryInventoryAsync(true, additionalSkuList,
                                                       mReceivedInventoryListener);
                                           }
                                       }
                                   });
    }
    private  void showDilog(){

        new AlertDialog.Builder(this)
                .setTitle("訊息")
                .setMessage("感謝您大方贊助")
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();

    }
    private  void ShowBuyDilog(String tittle,String message,String price,final String sku){

        new AlertDialog.Builder(this)
                .setTitle(tittle)
                .setMessage("內容:"+message+"\n\n"+"價錢:"+price)
                .setNegativeButton("確定購買", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHelper.launchPurchaseFlow(InAppBillingActivity.this,sku,10001,mPurchaseFinishedListener, "mypurchasetoken");
                        dialog.dismiss();

                    }
                })
                .setPositiveButton("不了,謝謝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

}
