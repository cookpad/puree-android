package com.cookpad.puree;

/**
 * Provide a serializer mechanism for logs
 */
public interface PureeSerializer {

    /**
     * Serialize a {@link Object} into a json string representation.
     *
     * @param log {@link Object}.
     * @return serialized json object.
     */
    String serialize(Object log);
}
