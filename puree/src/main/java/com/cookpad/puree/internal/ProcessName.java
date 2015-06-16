package com.cookpad.puree.internal;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

// http://stackoverflow.com/questions/19631894/is-there-a-way-to-get-current-process-name-in-android
@ParametersAreNonnullByDefault
public class ProcessName {

    @Nonnull
    public static String getAndroidProcessName(Context context) {
        String name = findProcessNameInLinuxWay();
        if (name == null) {
            name = findProcessNameInAndroidWay(context);
        }
        if (name != null) {
            return extractAndroidProcessName(name);
        } else {
            return "";
        }
    }

    static String extractAndroidProcessName(String fullProcessName) {
        int pos = fullProcessName.lastIndexOf(':');
        if (pos != -1) {
            return fullProcessName.substring(pos + 1);
        }
        return "";
    }

    static String findProcessNameInLinuxWay() {
        BufferedReader cmdlineReader = null;
        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(
                            "/proc/" + android.os.Process.myPid() + "/cmdline"),
                    "UTF-8"));
            int c;
            StringBuilder processName = new StringBuilder();
            while ((c = cmdlineReader.read()) > 0) {
                processName.append((char) c);
            }
            return processName.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close();
                } catch (IOException e) {
                    // noop
                }
            }
        }
    }

    static String findProcessNameInAndroidWay(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
