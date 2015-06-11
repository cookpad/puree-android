package com.cookpad.puree;

public class Key {

    private final String id;

    public String getId() {
        return id;
    }

    private Key(Class<? extends PureeLog> clazz) {
        this.id = clazz.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Key) {
            Key other = (Key) o;
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static Key from(Class<? extends PureeLog> clazz) {
        return new Key(clazz);
    }

    @Override
    public String toString() {
        return "Key{" + id + "}";
    }
}
