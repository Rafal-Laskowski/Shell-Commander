package io.github.laskowski.shell;

import io.github.rafal.laskowski.wait.Wait;

import java.io.IOException;

public abstract class DefaultShellTask implements ShellTask {
    private Process process;

    @Override
    public void start() throws IOException {
        ShellArguments shellArguments = getShellArguments();
        Script script = getScript();

        ProcessBuilder processBuilder = new ProcessBuilder(shellArguments.getExecutor(), shellArguments.getExecutionArguments(), script.getFile().getAbsolutePath());

        process = processBuilder.start();
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public Process getProcess() {
        new Wait().until(() -> process != null);

        return process;
    }
}
