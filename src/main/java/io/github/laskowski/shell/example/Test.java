package io.github.laskowski.shell.example;

import io.github.laskowski.shell.runners.DefaultTaskRunner;

public class Test {

    public static void main(String[] args) {
        DefaultTaskRunner taskRunner = DefaultTaskRunner.getInstance();
        taskRunner.run(new PythonCheckTask());
    }
}
