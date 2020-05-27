package io.github.laskowski.shell.task;

import io.github.laskowski.shell.script.Script;

public interface ShellTask<T> extends Task {

    T getProcess();

    Script getScript();
}
