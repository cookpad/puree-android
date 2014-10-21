package com.cookpad.android.puree.outputs;

import android.test.AndroidTestCase;

import com.cookpad.android.puree.OutputConfiguration;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputMatcherTest extends AndroidTestCase {
    private PureeOutput output1 = new PureeOutput() {
        @Override
        public String type() {
            return "output1";
        }

        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return null;
        }

        @Override
        public void emit(JSONObject serializedLog) {

        }
    };

    private PureeOutput output2 = new PureeOutput() {
        @Override
        public String type() {
            return "output2";
        }

        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return null;
        }

        @Override
        public void emit(JSONObject serializedLog) {

        }
    };

    public void testMatchWith() {
        Map<String, PureeOutput> outputMap = new HashMap<>();
        outputMap.put(output1.type(), output1);
        outputMap.put(output2.type(), output2);

        {
            List<PureeOutput> outputs = OutputMatcher.matchWith(outputMap, new String[]{"JAVA"});
            assertEquals(0, outputs.size());
        }
        {
            List<PureeOutput> outputs = OutputMatcher.matchWith(outputMap, new String[]{"output1"});
            assertEquals(1, outputs.size());
            assertEquals("output1", outputs.get(0).type());
        }
        {
            List<PureeOutput> outputs = OutputMatcher.matchWith(outputMap, new String[]{"output2"});
            assertEquals(1, outputs.size());
            assertEquals("output2", outputs.get(0).type());
        }
    }
}
