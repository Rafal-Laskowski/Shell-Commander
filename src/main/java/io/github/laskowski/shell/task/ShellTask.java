package io.github.laskowski.shell.task;

import io.github.laskowski.shell.script.Script;

public interface ShellTask<T> {

    void start();

    void stop();

    T getProcess();

    Script getScript();

    String getName();
}
