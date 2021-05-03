package com.daniilvdovin.tableapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Table{
    public List<Row> products;

    public Table() {
        this.products = new ArrayList<>();
    }

    public void Print(){
        String temp = "";
        for (Row r:products) {
            temp += String.format("%s|%s|%s|%s|%s\n",
                    r.article,
                    r.product,
                    "p:"+r.price,
                    "s:"+r.in_stock,
                    r.description);
        }
        Log.e(DBH.LOG_TAG,temp);
    }
}
