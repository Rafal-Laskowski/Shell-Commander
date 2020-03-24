package io.github.laskowski.shell.runners;

import io.github.laskowski.shell.process.DefaultProcessKiller;
import io.github.laskowski.shell.process.ProcessKiller;
import io.github.laskowski.shell.subscribers.Subscriber;
import io.github.laskowski.shell.tasks.Task;
import io.github.laskowski.shell.utils.Wait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultTaskRunner implements TaskRunner {
    private static DefaultTaskRunner instance = new DefaultTaskRunner();
    private final List<Task> tasks = new ArrayList<>();
    private final Map<Task, List<ProcessHandle>> processessToKill = new HashMap<>();

    private DefaultTaskRunner() {}

    public static DefaultTaskRunner getInstance() {
        return instance;
    }

    @Override
    public void run(Task task, Subscriber subscriber) {
        run(task, subscriber, new Object[0]);
    }

    @Override
    public void run(Task task, Subscriber subscriber, Object... arguments) {
        tasks.add(task);

        Thread taskThread = new Thread(() -> task.start(arguments));
        taskThread.setName(task.getName());

        List<ProcessHandle> beforeProcess = getRunningProcesses();
        taskThread.start();

        Thread publishingThread = new Thread(() -> task.startPublishing(subscriber));
        publishingThread.start();

        this.getProcessesToKill(task, beforeProcess).ifPresent(processes -> processessToKill.put(task, processes));

        Wait.until(task.getReadyFunctionTimeout(), task.getReadyFunction(), subscriber);
    }

    @Override
    public void stop(Task task) {
        task.stop();
        processessToKill.get(task).forEach(pid -> getProcessKiller().kill(pid));
    }

    @Override
    public void stopAll() {
        tasks.forEach(this::stop);
    }

    @Override
    public ProcessKiller getProcessKiller() {
        return new DefaultProcessKiller();
    }

    private List<ProcessHandle> getRunningProcesses() {
        return ProcessHandle.allProcesses().collect(Collectors.toList());
    }
}
