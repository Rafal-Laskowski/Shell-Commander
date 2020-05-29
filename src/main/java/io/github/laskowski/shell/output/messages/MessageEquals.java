package io.github.laskowski.shell.output.messages;

import java.util.function.Predicate;

public class MessageEquals extends MessageAction {

    public MessageEquals(String expected) {
        super(expected);
    }

    @Override
    public Predicate<String> getPredicate() {
        return message -> message.equals(expected);
    }
}
