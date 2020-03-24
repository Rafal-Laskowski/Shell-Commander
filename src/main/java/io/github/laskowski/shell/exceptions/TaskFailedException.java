package io.github.laskowski.shell.exceptions;

import io.github.laskowski.shell.tasks.Task;

public class TaskFailedException extends RuntimeException {

    public TaskFailedException(Task task) {
        super(String.format("Task: [%s] failed to execute", task.getName()));
    }

    public TaskFailedException(Task task, String cause) {
        super(String.format("Task: [%s] failed to execute. Cause: [%s]", task.getName(), cause));
    }
}
