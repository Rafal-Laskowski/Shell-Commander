package io.github.laskowski.shell.error;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;

public interface ErrorHandler {
    void handle(ErrorDetectedException errorDetectionException);
}
