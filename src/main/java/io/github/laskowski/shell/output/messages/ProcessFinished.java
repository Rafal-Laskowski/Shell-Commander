package io.github.laskowski.shell.output.messages;

import static io.github.laskowski.shell.output.DefaultPublisher.LAST_MESSAGE;

public class ProcessFinished extends MessageEquals {

    public ProcessFinished() {
        super(LAST_MESSAGE);
    }
}
