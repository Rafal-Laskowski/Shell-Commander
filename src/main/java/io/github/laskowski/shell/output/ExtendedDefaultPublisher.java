package io.github.laskowski.shell.output;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;
import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;

public class ExtendedDefaultPublisher extends DefaultPublisher implements Publisher<Process> {

    public ExtendedDefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy, @Nullable ErrorDetectionStrategy errorDetectionStrategy) {
        super(messageExclusionStrategy, errorDetectionStrategy);
    }

    public void startPublishing(Process process) throws ErrorDetectedException {
        super.startPublishing(process, true);
    }
}
