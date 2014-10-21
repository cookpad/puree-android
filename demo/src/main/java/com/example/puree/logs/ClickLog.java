package com.example.puree.logs;

import com.cookpad.puree.JsonConvertible;
import com.google.gson.annotations.SerializedName;

public class ClickLog extends JsonConvertible {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
