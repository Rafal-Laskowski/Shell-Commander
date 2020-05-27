package io.github.laskowski.shell.runner;

import io.github.laskowski.shell.task.Task;

import java.util.ArrayList;
import java.util.List;

public class DefaultTaskRunner implements TaskRunner {
    private static DefaultTaskRunner taskRunner;
    private final List<Task> tasks = new ArrayList<>();

    private DefaultTaskRunner() {}

    public synchronized static DefaultTaskRunner getInstance() {
        if (taskRunner == null) {
            taskRunner = new DefaultTaskRunner();
        }
        return taskRunner;
    }

    @Override
    public void run(Task task) {
        Thread taskThread = new Thread(task::start);
        taskThread.start();

        tasks.add(task);
    }

    @Override
    public void stop(Task task) {
        tasks.remove(task);
        task.stop();
    }

    @Override
    public void stopAll() {
        tasks.forEach(Task::stop);
    }
}
