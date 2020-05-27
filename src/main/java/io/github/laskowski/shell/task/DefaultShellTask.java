package io.github.laskowski.shell.task;

import io.github.laskowski.shell.script.DefaultScriptInfoProvider;
import io.github.laskowski.shell.script.Script;
import io.github.laskowski.shell.ShellArguments;
import io.github.rafal.laskowski.wait.Wait;

import java.io.IOException;

public abstract class DefaultShellTask implements ShellTask<Process> {
    private static final ShellArguments SHELL_ARGUMENTS = DefaultScriptInfoProvider.getInstance().getShellArguments();
    private volatile Process process;

    @Override
    public void start() {
        Script script = getScript();

        ProcessBuilder processBuilder = new ProcessBuilder(SHELL_ARGUMENTS.getExecutor(), SHELL_ARGUMENTS.getExecutionArguments(), script.getFile().getAbsolutePath());

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public Process getProcess() {
        new Wait().withMessage("Failed to wait until Process is not null").until(() -> process != null);

        return process;
    }
}
