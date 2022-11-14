package com.example.projeto.exceptions;

import java.net.MalformedURLException;

public class MyResourceNotFoundException extends RuntimeException {

    /**
     *
     */

    public MyResourceNotFoundException(final String string) {
        super(string);
    }

    public MyResourceNotFoundException(final String string, final MalformedURLException ex) {
        super(string, ex);
    }

    public MyResourceNotFoundException(final Class<?> clazz, final long id) {
        super(String.format("Entity %s with id %d not found", clazz.getSimpleName(), id));
    }

    public MyResourceNotFoundException(final Class<?> clazz, final String id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
    }
}
