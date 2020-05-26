package io.github.laskowski.shell;

import java.io.IOException;

public interface ShellTask {

    void start() throws IOException;

    void stop();

    Process getProcess();

    ShellArguments getShellArguments();

    Script getScript();

    String getName();
}
