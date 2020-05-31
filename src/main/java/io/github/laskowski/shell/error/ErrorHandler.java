package io.github.laskowski.shell.error;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.OutputListener;
import io.github.laskowski.shell.task.ShellTask;

public interface ErrorHandler<T> {
    void handle(ErrorDetectedException errorDetectionException, ShellTask<T> shellTask, OutputListener<T> outputListener);
}
