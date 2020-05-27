package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;
import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;

public class DefaultOutputListener implements OutputListener<Process> {
    private MessageExclusionStrategy messageExclusionStrategy;
    private ErrorDetectionStrategy errorDetectionStrategy;

    public DefaultOutputListener(@Nullable MessageExclusionStrategy messageExclusionStrategy, @Nullable ErrorDetectionStrategy errorDetectionStrategy) {
        this.messageExclusionStrategy = messageExclusionStrategy;
        this.errorDetectionStrategy = errorDetectionStrategy;
    }

    @Override
    public Publisher<Process> getPublisher() {
        return new DefaultPublisher(messageExclusionStrategy);
    }

    @Override
    public StringSubscriber getSubscriber() {
        return new DefaultStringSubscriber(errorDetectionStrategy);
    }
}
