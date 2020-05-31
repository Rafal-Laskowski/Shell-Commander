package io.github.laskowski.shell.runner;

import io.github.laskowski.shell.error.ErrorHandler;
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
        Thread outputListenerThread = new OutputListenerThread(shellTask, outputListener);
        Thread shellTaskRunner = new ShellTaskRunnerThread(shellTask);
        StringSubscriber subscriber = outputListener.getSubscriber();

        outputListenerThread.start();
        shellTaskRunner.start();

        String timeoutMessage = String.format("%s\n%s",
                serviceReadyStrategy.getTimeoutMessage(),
                serviceReadyStrategy.getReadyPredicate().toString());

        new Wait()
                .withTimeout(serviceReadyStrategy.getTimeout())
                .withMessage(timeoutMessage)
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

    private static class OutputListenerThread extends Thread implements Runnable {
        private ShellTask<Process> shellTask;
        private OutputListener<Process> outputListener;

        OutputListenerThread(ShellTask<Process> shellTask, OutputListener<Process> outputListener) {
            this.shellTask = shellTask;
            this.outputListener = outputListener;
        }

        @Override
        public void run() {
            Process process = shellTask.getProcess();
            Publisher<Process> publisher = outputListener.getPublisher();

            publisher.registerSubscriber(outputListener.getSubscriber());
            try {
                publisher.startPublishing(process);
            } catch (ErrorDetectedException e) {
                try {
                    ErrorHandler<Process> errorHandler = shellTask.getErrorHandler();

                    if (errorHandler != null) {
                        errorHandler.handle(e, shellTask, outputListener);
                    } else {
                        throw new IllegalStateException("When you implement ErrorDetectionStrategy, you also have to implement ErrorHandler");
                    }

                } catch (Exception possibleExceptionThrownByErrorHandler) {
                    throw new IllegalStateException("Error Handler cannot throw any exception. The Exception would cause to stop the publisher thread only" +
                            "\nYou should implement your own way of handling the exception");
                }
            }
        }
    }

    private static class ShellTaskRunnerThread extends Thread implements Runnable {
        private ShellTask<Process> shellTask;

        ShellTaskRunnerThread(ShellTask<Process> shellTask) {
            this.shellTask = shellTask;
        }

        @Override
        public void run() {
            shellTask.start();
        }
    }
}
