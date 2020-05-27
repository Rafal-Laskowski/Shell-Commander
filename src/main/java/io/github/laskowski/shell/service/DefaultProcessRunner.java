package io.github.laskowski.shell.service;

import io.github.laskowski.shell.runner.DefaultShellTaskRunner;

public class DefaultProcessRunner implements ProcessRunner {

    @Override
    public void run(Service<Process> service) {
        DefaultShellTaskRunner.getInstance()
                .run(service.getShellTask(),
                        service.getOutputListener(),
                        service.getServiceReadyStrategy());
    }
}
