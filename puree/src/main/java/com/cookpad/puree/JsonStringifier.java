package com.cookpad.puree;

public interface JsonStringifier {
    /**
     * Convert an instance to JSON formatted string.
     *
     * @param jsonConvertible to be converted to JSON formatted string
     * @return JSON formatted string as an object like '{"key": "type"}'
     */
    String toJson(JsonConvertible jsonConvertible);
}
