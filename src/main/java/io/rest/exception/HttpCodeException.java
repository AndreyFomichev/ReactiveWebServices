package io.rest.exception;

public class HttpCodeException extends RuntimeException {
    public HttpCodeException(String message) {
        super(message);
    }
}
