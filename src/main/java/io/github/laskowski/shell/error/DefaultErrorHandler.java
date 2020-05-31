package io.github.laskowski.shell.error;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.OutputListener;
import io.github.laskowski.shell.task.ShellTask;

public class DefaultErrorHandler implements ErrorHandler<Process> {

    @Override
    public void handle(ErrorDetectedException errorDetectionException, ShellTask<Process> shellTask, OutputListener<Process> outputListener) {
        outputListener.getPublisher().stop();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
            //let output listener to read last lines
        }

        shellTask.stop();
    }
}
