package com.example.puree.logs;

import com.google.gson.annotations.SerializedName;

import com.cookpad.puree.PureeLog;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class PvLog implements PureeLog {
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
