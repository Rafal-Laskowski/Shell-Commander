package io.github.laskowski.shell.output.messages;

import java.util.function.Predicate;

public class MessageContains extends MessageAction {

    public MessageContains(String expected) {
        super(expected);
    }

    @Override
    public Predicate<String> getPredicate() {
        return message -> message.contains(expected);
    }

    @Override
    public String toString() {
        return String.format("Message contains: %s", expected);
    }
}
