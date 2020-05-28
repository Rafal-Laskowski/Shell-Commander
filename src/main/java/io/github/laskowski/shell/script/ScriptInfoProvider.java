package io.github.laskowski.shell.script;

import io.github.laskowski.os.OperatingSystemDiscoveryStrategy;
import io.github.laskowski.shell.ShellArguments;

import java.io.File;

public interface ScriptInfoProvider {
    OperatingSystemDiscoveryStrategy getOperatingSystemDiscoveryStrategy();

    ShellArguments getShellArguments();

    File getScriptDirectory();
}
