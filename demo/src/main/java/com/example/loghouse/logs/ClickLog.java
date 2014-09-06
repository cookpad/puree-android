package com.example.loghouse.logs;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.cookpad.android.loghouse.plugins.OutLogcat;

public class ClickLog extends Log {
    private String page;
    private String label;

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }

    public String type() {
        return OutLogcat.TYPE;
    }
}
