package io.github.laskowski.shell.runner;

import io.github.laskowski.shell.output.OutputListener;
import io.github.laskowski.shell.output.service.ServiceReadyStrategy;
import io.github.laskowski.shell.task.ShellTask;

public interface ShellTaskRunner<T> {
    void run(ShellTask<T> shellTask, OutputListener<T> outputListener, ServiceReadyStrategy serviceReadyStrategy);

    void stop(ShellTask<T> shellTask);

    void stopAll();
}
