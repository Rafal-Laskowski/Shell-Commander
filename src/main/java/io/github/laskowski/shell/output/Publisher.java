package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import java.util.concurrent.Flow;

public interface Publisher<T> {

    void startPublishing(T process);

    void stopPublishing();

    void publishMessage(String message, MessageExclusionStrategy messageExclusionStrategy);

    void registerSubscriber(Flow.Subscriber<String> subscriber);
}
