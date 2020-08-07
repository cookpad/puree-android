package com.example.puree.logs;

import com.google.gson.Gson;

import com.cookpad.puree.PureeSerializer;

public class PureeGsonSerializer implements PureeSerializer {
    private Gson gson = new Gson();

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
