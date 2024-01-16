package com.pixelflicks.admin.catalogo.domain.exceptions;

public class NoStackTracingException extends RuntimeException{
    public NoStackTracingException(final String message) {
        this(message,null);
    }

    public NoStackTracingException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
