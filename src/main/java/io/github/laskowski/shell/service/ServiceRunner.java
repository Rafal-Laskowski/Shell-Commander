package io.github.laskowski.shell.service;

public interface ServiceRunner<T> {
    void run(Service<T> service);
}
