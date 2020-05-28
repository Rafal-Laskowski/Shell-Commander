package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;

public class ExtendedDefaultPublisher extends DefaultPublisher implements Publisher<Process> {

    public ExtendedDefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy) {
        super(messageExclusionStrategy);
    }

    public void startPublishing(Process process) {
        super.startPublishing(process, true);
    }
}
