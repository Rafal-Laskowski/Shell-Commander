package io.github.laskowski.shell.error;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;

public interface ErrorHandler {
    boolean handle(ErrorDetectedException errorDetectionException);
}
