package com.example.loghouse.logs;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;

public class ClickLog extends Log {
    private String page;
    private String label;
    private String type = OutBufferedLogcat.TYPE;

    public String type() {
        return type;
    }

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }

    public ClickLog(String page, String label, String type) {
        this.page = page;
        this.label = label;
        this.type = type;
    }
}
