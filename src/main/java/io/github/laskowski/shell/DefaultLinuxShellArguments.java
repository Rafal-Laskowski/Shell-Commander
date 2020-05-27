package io.github.laskowski.shell;

public class DefaultLinuxShellArguments implements ShellArguments {

    @Override
    public String getExecutor() {
        return "bash";
    }

    @Override
    public String getExtension() {
        return ".sh";
    }

    @Override
    public String getExecutionArguments() {
        return "-c";
    }

    @Override
    public String getVariableCommand() {
        return "export";
    }
}
