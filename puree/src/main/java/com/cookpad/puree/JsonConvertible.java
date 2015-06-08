package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class JsonConvertible {

    public JsonObject toJson(Gson gson) {
        return (JsonObject) gson.toJsonTree(this);
    }
}
