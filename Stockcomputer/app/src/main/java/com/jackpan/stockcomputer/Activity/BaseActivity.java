package com.jackpan.stockcomputer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.jackpan.stockcomputer.R;

public class BaseActivity  extends Activity{
    private static Toast toast = null;


    public void startActivity(Class<?> clas){

        startActivity(new Intent(this,clas));

    }
    public void startActivity(Class<?> clas ,Bundle bundle){

        Intent intent = new Intent();
        intent.setClass(this, clas);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     *  基本AlertDilog
     */
    public  void showBaseAlertDilog(String title,String message){
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(getString(R.string.alertbutton_ok),null)
            .show();
    }

    /**
     * 不重複的toast
     */
    public void showToast(String msg){
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    }


