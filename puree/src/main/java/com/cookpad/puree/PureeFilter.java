package com.cookpad.puree;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PureeFilter {

    @Nullable
    JsonObject apply(JsonObject jsonLog);
}
