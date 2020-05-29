package io.github.laskowski.shell.output.messages;

import io.github.laskowski.shell.output.service.ServiceReadyPredicate;

import java.util.function.Predicate;

public abstract class MessageAction implements ServiceReadyPredicate, ServiceErrorPredicate, MessageExclusionStrategy {
    protected final String expected;

    public MessageAction(String expected) {
        this.expected = expected;
    }

    public abstract Predicate<String> getPredicate();

    @Override
    public boolean test(String message) {
        return getPredicate().test(message);
    }
}
