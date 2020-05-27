package io.github.laskowski.shell;

public class DefaultWindowsShellArguments implements ShellArguments {

    @Override
    public String getExecutor() {
        return "cmd.exe";
    }

    @Override
    public String getExtension() {
        return ".bat";
    }

    @Override
    public String getExecutionArguments() {
        return "/C";
    }

    @Override
    public String getVariableCommand() {
        return "set";
    }
}
