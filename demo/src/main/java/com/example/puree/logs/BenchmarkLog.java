package com.example.puree.logs;

import com.google.gson.annotations.SerializedName;

import com.cookpad.puree.JsonConvertible;

public class BenchmarkLog extends JsonConvertible {

    @SerializedName("page")
    private String page;

    @SerializedName("label")
    private String label;

    public BenchmarkLog(String page, String label) {
        this.page = page;
        this.label = label;
    }

}
