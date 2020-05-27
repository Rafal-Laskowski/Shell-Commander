package io.github.laskowski.shell.output;

public interface OutputListener<T> {
    Publisher<T> getPublisher();
    StringSubscriber getSubscriber();
}
