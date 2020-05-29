package io.github.laskowski.shell.output.messages;

import java.util.function.Predicate;

public class MessageMatches extends MessageAction {

    public MessageMatches(String regex) {
        super(regex);
    }

    @Override
    public Predicate<String> getPredicate() {
        return message -> message.matches(expected);
    }
}
