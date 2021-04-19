package com.example.puree.logs.plugins;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.plugins.OutBufferedLogcat;

import javax.annotation.Nonnull;

public class OutBufferedLogcatPurge extends OutBufferedLogcat {

    @Nonnull
    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf = super.configure(conf);
        conf.setFlushIntervalMillis(1000);
        conf.setPurgeAgeMillis(5000);
        return conf;
    }
}
