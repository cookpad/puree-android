package com.cookpad.puree;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PureeFilter {

    @Nullable
    String apply(String jsonLog);
}
