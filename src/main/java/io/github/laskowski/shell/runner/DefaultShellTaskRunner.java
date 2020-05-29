package io.github.laskowski.shell.runner;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.OutputListener;
import io.github.laskowski.shell.output.Publisher;
import io.github.laskowski.shell.output.StringSubscriber;
import io.github.laskowski.shell.output.service.ServiceReadyStrategy;
import io.github.laskowski.shell.task.ShellTask;
import io.github.rafal.laskowski.wait.Wait;

import java.util.ArrayList;
import java.util.List;

public class DefaultShellTaskRunner implements ShellTaskRunner<Process> {
    private static DefaultShellTaskRunner taskRunner;
    private final List<ShellTask<Process>> tasks = new ArrayList<>();

    private DefaultShellTaskRunner() {}

    public synchronized static DefaultShellTaskRunner getInstance() {
        if (taskRunner == null) {
            taskRunner = new DefaultShellTaskRunner();
        }
        return taskRunner;
    }

    @Override
    public void run(ShellTask<Process> shellTask, OutputListener<Process> outputListener, ServiceReadyStrategy serviceReadyStrategy) {
        Publisher<Process> publisher = outputListener.getPublisher();
        StringSubscriber subscriber = outputListener.getSubscriber();
        Thread outputListenerThread = new Thread(() -> {
            Process process = shellTask.getProcess();

            publisher.registerSubscriber(subscriber);
            try {
                publisher.startPublishing(process);
            } catch (ErrorDetectedException e) {
                shellTask.getErrorHandler().handle(e);
            }
        });

        outputListenerThread.start();

        Thread processThread = new Thread(shellTask::start);
        processThread.start();

        new Wait()
                .withTimeout(serviceReadyStrategy.getTimeout())
                .withMessage(serviceReadyStrategy.getTimeoutMessage())
                .until(() -> subscriber.getLines().stream().anyMatch(serviceReadyStrategy.getReadyPredicate()));

        tasks.add(shellTask);
    }

    @Override
    public void stop(ShellTask<Process> shellTask) {
        tasks.remove(shellTask);
        shellTask.stop();
    }

    @Override
    public void stopAll() {
        tasks.forEach(ShellTask::stop);
    }
}
