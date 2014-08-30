package com.example.loghouse.logs;

import com.cookpad.android.loghouse.Log;

public class ClickLog implements Log {
    private String page;
    private String label;

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
