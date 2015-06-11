package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * This class is obsolete. Use {@link PureeLog} instead.
 */
@Deprecated
public abstract class JsonConvertible implements PureeLog {

    public JsonObject toJson(Gson gson) {
        throw new UnsupportedOperationException("Puree's JsonConvertible is obsolete. Use PureeLog instead.");
    }
}
