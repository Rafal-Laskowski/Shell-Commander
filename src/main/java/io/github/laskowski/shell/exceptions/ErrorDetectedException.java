package io.github.laskowski.shell.exceptions;

public class ErrorDetectedException extends Exception {

    public ErrorDetectedException(String message, Object... args) {
        super(String.format(message, args));
    }
}
