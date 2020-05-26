package io.github.laskowski.shell;

public interface OutputListener {
    Publisher getPublisher();
    StringSubscriber getSubscriber();
}
