package com.cookpad.puree;

import com.cookpad.puree.outputs.PureeOutput;

public class Source {
    private PureeConfiguration.Builder builder;
    private Key key;
    private PureeFilter filter;

    public Source(PureeConfiguration.Builder builder, Key key) {
        this.builder = builder;
        this.key = key;
    }

    public Source filter(PureeFilter filter) {
        this.filter = filter;
        return this;
    }

    public PureeConfiguration.Builder to(PureeOutput output) {
        builder.registerOutput(key, output, filter);
        return builder;
    }
}
