package io.github.laskowski.shell.process;

public interface ProcessKiller {

    void kill(ProcessHandle pid);
}
