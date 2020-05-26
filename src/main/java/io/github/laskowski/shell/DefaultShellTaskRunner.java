package io.github.laskowski.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultShellTaskRunner implements ShellTaskRunner {
    private static DefaultShellTaskRunner taskRunner;
    private final List<ShellTask> tasks = new ArrayList<>();

    private DefaultShellTaskRunner() {}

    public synchronized static DefaultShellTaskRunner getInstance() {
        if (taskRunner == null) {
            taskRunner = new DefaultShellTaskRunner();
        }
        return taskRunner;
    }

    @Override
    public void run(ShellTask shellTask, OutputListener outputListener) {
        Thread processThread = new Thread(() -> {
            try {
                shellTask.start();
            } catch (IOException e) {
                //TO-DO
            }
        });
        processThread.setName(String.format("%s Shell Task Thread", shellTask.getName()));
        processThread.start();

        Thread outputListenerThread = new Thread(() -> {
            Publisher publisher = outputListener.getPublisher();
            StringSubscriber subscriber = outputListener.getSubscriber();
            Process process = shellTask.getProcess();

            publisher.registerSubscriber(subscriber);
            publisher.startPublishing(process);
        });

        outputListenerThread.setName(String.format("%s Output Listener Thread", shellTask.getName()));
        outputListenerThread.start();

        tasks.add(shellTask);
    }

    @Override
    public void stop(ShellTask shellTask) {
        tasks.remove(shellTask);
        shellTask.stop();
    }

    @Override
    public void stopAll() {
        tasks.forEach(ShellTask::stop);
    }
}
