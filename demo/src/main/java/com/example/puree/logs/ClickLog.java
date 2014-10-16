package com.example.puree.logs;

import com.cookpad.android.puree.JsonConvertible;
import com.example.puree.logs.plugins.OutDisplay;
import com.google.gson.annotations.SerializedName;

public class ClickLog extends JsonConvertible {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    private String type = OutDisplay.TYPE;

    public String sendTo() {
        return type;
    }

    public ClickLog(String page, String label, String type) {
        this.page = page;
        this.label = label;
        this.type = type;
    }
}
