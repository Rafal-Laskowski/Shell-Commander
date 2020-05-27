package io.github.laskowski.shell.service;

import io.github.laskowski.shell.output.OutputListener;
import io.github.laskowski.shell.output.service.ServiceReadyStrategy;
import io.github.laskowski.shell.task.ShellTask;

public interface Service<T> {

    ShellTask<T> getShellTask();

    OutputListener<T> getOutputListener();

    ServiceReadyStrategy getServiceReadyStrategy();
}
