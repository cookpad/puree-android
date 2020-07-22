package com.example.puree.logs;

import com.google.gson.annotations.SerializedName;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public class PvLog {
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
