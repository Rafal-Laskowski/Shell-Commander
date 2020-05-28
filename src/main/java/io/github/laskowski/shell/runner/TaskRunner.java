package io.github.laskowski.shell.runner;

import io.github.laskowski.shell.task.Task;

public interface TaskRunner {
    void run(Task task);

    void stop(Task task);

    void stopAll();
}
