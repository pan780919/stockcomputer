package com.jackpan.stockcomputer.util;

import android.graphics.Color;
import android.widget.TextView;

public class StockPriceCheck {

    public static void  check(String s , TextView view){
        String[] arrray = s.split("");
        for (String s1 : arrray) {
            if (s.contains("%")){
                if (s.contains("-")){
                    view.setTextColor(Color.GREEN);
                }else if(s.contains("+")){
                    view.setTextColor(Color.RED);
                }
            }
        }
    }
}
