package com.cookpad.puree.plugins;

import android.util.Log;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeOutput;

import org.json.JSONObject;

public class OutLogcat extends PureeOutput {
    @Override
    public String type() {
        return "out_logcat";
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        return conf;
    }

    @Override
    public void emit(JSONObject jsonLog) {
        Log.d("out_logcat", jsonLog.toString());
    }
}
