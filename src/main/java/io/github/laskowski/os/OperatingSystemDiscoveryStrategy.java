package io.github.laskowski.os;

import io.github.laskowski.shell.ShellArguments;

public interface OperatingSystemDiscoveryStrategy {
    OperatingSystem getOS();
}
