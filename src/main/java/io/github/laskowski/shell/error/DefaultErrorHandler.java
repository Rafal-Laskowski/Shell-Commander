package io.github.laskowski.shell.error;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.exceptions.ShellTaskFailedException;
import io.github.laskowski.shell.task.ShellTask;

public class DefaultErrorHandler implements ErrorHandler {
    private ShellTask<Process> shellTask;

    public DefaultErrorHandler(ShellTask<Process> shellTask) {
        this.shellTask = shellTask;
    }

    @Override
    public boolean handle(ErrorDetectedException errorDetectionException) {
        throw new ShellTaskFailedException(shellTask, errorDetectionException);
    }
}
