package io.github.laskowski.shell.tasks;

public interface Action<T, R> {

    R build(T t);
}
