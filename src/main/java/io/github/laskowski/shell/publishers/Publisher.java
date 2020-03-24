package io.github.laskowski.shell.publishers;

import io.github.laskowski.shell.subscribers.Subscriber;

import java.util.concurrent.Flow;

public interface Publisher<T> extends Flow.Publisher<String> {

    void startPublishing(T t);

    void registerSubscriber(Subscriber subscriber);

    void stop();
}
