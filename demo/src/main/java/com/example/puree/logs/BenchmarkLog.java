package com.example.puree.logs;

import com.google.gson.annotations.SerializedName;

public class BenchmarkLog {

    @SerializedName("page")
    private String page;

    @SerializedName("label")
    private String label;

    public BenchmarkLog(String page, String label) {
        this.page = page;
        this.label = label;
    }

}
