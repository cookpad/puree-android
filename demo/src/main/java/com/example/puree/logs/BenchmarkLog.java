package com.example.puree.logs;

import com.google.gson.annotations.SerializedName;

import com.cookpad.puree.PureeLog;

public class BenchmarkLog implements PureeLog {

    @SerializedName("page")
    private String page;

    @SerializedName("label")
    private String label;

    public BenchmarkLog(String page, String label) {
        this.page = page;
        this.label = label;
    }

}
