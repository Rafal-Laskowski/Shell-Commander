package io.github.laskowski.shell.service;

import io.github.laskowski.shell.runner.DefaultShellTaskRunner;

public class DefaultProcessRunner implements ProcessRunner {
    private static DefaultProcessRunner instance;

    public static DefaultProcessRunner getInstance() {
        if (instance == null) {
            instance = new DefaultProcessRunner();
        }

        return instance;
    }

    private DefaultProcessRunner() {}

    @Override
    public void run(Service<Process> service) {
        DefaultShellTaskRunner.getInstance()
                .run(service.getShellTask(),
                        service.getOutputListener(),
                        service.getServiceReadyStrategy());
    }
}
