package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class ExtendedDefaultPublisher extends DefaultPublisher implements Publisher<Process> {

    public ExtendedDefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy) {
        super(messageExclusionStrategy);
    }

    public void startPublishing(Process process) {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = "";

        boolean isOutReady;
        boolean isErrorReady;
        boolean isProcessAlive;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                do {
                    isOutReady = stdInput.ready();
                    if (isOutReady) {
                        line = stdInput.readLine();
                        publishMessage(line, messageExclusionStrategy);
                    }

                    isErrorReady = stdError.ready();
                    if (isErrorReady) {
                        line = stdError.readLine();
                        publishMessage(line, messageExclusionStrategy);
                    }

                    isProcessAlive = process.isAlive();
                    if (!isProcessAlive) {
                        line = null;
                        process.waitFor(1000, TimeUnit.MILLISECONDS);
                    }

                } while (line != null && !Thread.currentThread().isInterrupted());
                process.waitFor(1, TimeUnit.MILLISECONDS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException ignore) {}
        }

        try {
            stdInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        publishMessage(LAST_MESSAGE, null);
    }

    @Override
    public void publishMessage(String message, @Nullable MessageExclusionStrategy messageExclusionStrategy) {
        if (messageExclusionStrategy == null) {
            submit(message);
        } else {
            if (!messageExclusionStrategy.test(message)) {
                submit(message);
            }
        }
    }

    @Override
    public void registerSubscriber(Flow.Subscriber<String> subscriber) {
        if (subscriber != null) {
            this.subscribe(subscriber);
        } else throw new IllegalArgumentException("You need to provide subscriber to Publisher!");
    }
}
