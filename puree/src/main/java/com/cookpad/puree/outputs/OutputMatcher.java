package com.cookpad.puree.outputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputMatcher {
    public static List<PureeOutput> matchWith(Map<String, PureeOutput> outputMap, List<String> types) {
        List<PureeOutput> foundOutputs = new ArrayList<>();
        for (String type : types) {
            PureeOutput output = outputMap.get(type);
            if (output != null) {
                foundOutputs.add(output);
            }
        }
        return foundOutputs;
    }
}
