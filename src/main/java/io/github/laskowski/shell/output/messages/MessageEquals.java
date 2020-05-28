package io.github.laskowski.shell.output.messages;

import io.github.laskowski.shell.output.service.ServiceReadyPredicate;

public class MessageEquals implements ServiceErrorPredicate, ServiceReadyPredicate {
    private final String expected;

    public MessageEquals(String expected) {
        this.expected = expected;
    }

    @Override
    public boolean test(String s) {
        return s.equals(expected);
    }
}
