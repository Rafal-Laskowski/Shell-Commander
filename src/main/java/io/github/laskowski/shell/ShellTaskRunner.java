package io.github.laskowski.shell;

public interface ShellTaskRunner {
    void run(ShellTask shellTask, OutputListener outputListener);

    void stop(ShellTask shellTask);

    void stopAll();
}
