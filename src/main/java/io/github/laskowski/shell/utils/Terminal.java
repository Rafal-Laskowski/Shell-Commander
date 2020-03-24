package io.github.laskowski.shell.utils;

import static io.github.laskowski.shell.utils.OperatingSystem.getStringBasedOnOS;

public class Terminal {

    public static String getCommandExecutor() {
        return getStringBasedOnOS("cmd.exe", "bash");
    }

    public static String getScriptFileExtension() {
        return getStringBasedOnOS(".bat", ".sh");
    }

    public static String getExecutorArgument() {
        return getStringBasedOnOS("/c", "-c");
    }

    public static String getVariableCommand() {
        return getStringBasedOnOS("set", "export");
    }
}
