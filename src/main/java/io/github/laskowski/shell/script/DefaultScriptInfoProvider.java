package io.github.laskowski.shell.script;

import io.github.laskowski.os.DefaultOperatingSystemDiscoveryStrategy;
import io.github.laskowski.os.OperatingSystem;
import io.github.laskowski.os.OperatingSystemDiscoveryStrategy;
import io.github.laskowski.shell.DefaultLinuxShellArguments;
import io.github.laskowski.shell.DefaultWindowsShellArguments;
import io.github.laskowski.shell.ShellArguments;

import java.io.File;

public class DefaultScriptInfoProvider implements ScriptInfoProvider {
    private static DefaultScriptInfoProvider instance;

    private DefaultScriptInfoProvider() {}

    public static DefaultScriptInfoProvider getInstance() {
        if (instance == null) {
            instance = new DefaultScriptInfoProvider();
        }

        return instance;
    }

    @Override
    public OperatingSystemDiscoveryStrategy getOperatingSystemDiscoveryStrategy() {
        return DefaultOperatingSystemDiscoveryStrategy.getInstance();
    }

    public ShellArguments getShellArguments() {
        OperatingSystemDiscoveryStrategy operatingSystemDiscoveryStrategy = getOperatingSystemDiscoveryStrategy();
        OperatingSystem operatingSystem = operatingSystemDiscoveryStrategy.getOS();
        switch (operatingSystem) {
            case WINDOWS: return new DefaultWindowsShellArguments();
            case MAC:
            case LINUX: return new DefaultLinuxShellArguments();
            default: throw new IllegalArgumentException("Cannot discover shell arguments for OS: " + operatingSystem);
        }
    }

    public File getScriptDirectory() {
        return new File("shell-scripts");
    }
}
