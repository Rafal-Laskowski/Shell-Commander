package io.github.laskowski.shell.exceptions;

import io.github.laskowski.shell.task.ShellTask;

public class ShellTaskFailedException extends RuntimeException {

    public ShellTaskFailedException(ShellTask<?> shellTask, ErrorDetectedException error) {
        super(String.format("Shell Task: [%s] failed!\nCause: %s", shellTask.getScript().getFile().getName(), error.getMessage()));
    }
}
