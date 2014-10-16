package com.example.puree.logs;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.cookpad.android.puree.JsonConvertible;
import com.cookpad.android.puree.plugins.OutLogcat;
import com.google.gson.annotations.SerializedName;

public class PvLog extends JsonConvertible {
    @SerializedName("screen_name")
    private String sceenName;

    public PvLog(Activity activity) {
        this(activity.getClass().getSimpleName());
    }

    public PvLog(Fragment fragment) {
        this(fragment.getClass().getSimpleName());
    }

    public PvLog(String sceenName) {
        this.sceenName = sceenName;
    }
}
