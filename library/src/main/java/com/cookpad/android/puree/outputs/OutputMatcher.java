package com.cookpad.android.puree.outputs;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputMatcher {
    public static List<PureeOutput> matchWith(Map<String, PureeOutput> outputMap, String spaceSeparatedString) {
        List<PureeOutput> foundOutputs = new ArrayList<>();
        for (String type : getTypes(spaceSeparatedString)) {
            PureeOutput output = outputMap.get(type);
            if (output != null) {
                foundOutputs.add(output);
            }
        }
        return foundOutputs;
    }

    public static String[] getTypes(String spaceSeparatedString) {
        if (TextUtils.isEmpty(spaceSeparatedString)) {
            return new String[]{};
        }
        return spaceSeparatedString.split(" ");
    }
}
