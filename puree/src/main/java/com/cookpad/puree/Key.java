package com.cookpad.puree;

public class Key {
    private String id;

    public String getId() {
        return id;
    }

    public Key(Class<?> clazz) {
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

    public static Key from(Class<?> clazz) {
        return new Key(clazz);
    }
}
