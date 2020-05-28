package io.github.laskowski.shell.output.messages;

import io.github.laskowski.shell.output.service.ServiceReadyPredicate;

public class MessageMatches implements ServiceReadyPredicate, ServiceErrorPredicate {
    private final String expected;

    public MessageMatches(String expected) {
        this.expected = expected;
    }

    @Override
    public boolean test(String s) {
        return s.matches(expected);
    }
}
