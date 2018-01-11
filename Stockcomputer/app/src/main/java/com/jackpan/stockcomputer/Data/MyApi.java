package com.jackpan.stockcomputer.Data;

import java.util.regex.Pattern;

public class MyApi {
    public static final String AdLocusKey = "ae0e79da3bc5d0c3ede9c907214713a9d1aa86dd";


    public static boolean isInteger(String value) {//正規表達式
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(value).matches();
    }}
