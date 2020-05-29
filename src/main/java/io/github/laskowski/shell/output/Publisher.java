package io.github.laskowski.shell.output;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;
import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import java.util.concurrent.Flow;

public interface Publisher<T> {

    void startPublishing(T process) throws ErrorDetectedException;

    void publishMessage(String message, MessageExclusionStrategy messageExclusionStrategy, ErrorDetectionStrategy errorDetectionStrategy) throws ErrorDetectedException;

    void registerSubscriber(Flow.Subscriber<String> subscriber);
}
