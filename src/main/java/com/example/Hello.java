package com.example;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Hello {

    final private String name;

    @ConstructorProperties("name")
    Hello(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hello hello = (Hello) o;
        return Objects.equals(name, hello.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
