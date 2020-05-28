package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class DefaultPublisher extends SubmissionPublisher<String> implements Publisher<Process> {
    public static final String LAST_MESSAGE = "PROCESS FINISHED";
    private final MessageExclusionStrategy messageExclusionStrategy;
    protected volatile boolean shouldPublish = true;

    public DefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy) {
        this.messageExclusionStrategy = messageExclusionStrategy;
    }

    public void startPublishing(Process process) {
        Thread processDetectionThread = new Thread(() -> {
            while(process.isAlive()) {
                Sleeper.sleep(Duration.ofMillis(500));
            }

            Sleeper.sleep(Duration.ofMillis(500));
            stopPublishing();
        });


        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        boolean isOutReady;

        processDetectionThread.start();
        while (shouldPublish) {
            try {
                isOutReady = stdInput.ready();
                if (isOutReady) {
                    line = stdInput.readLine();

                    publishMessage(line, messageExclusionStrategy);
                }

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

    protected void stopPublishing() {
        shouldPublish = false;
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

    private static class Sleeper {

        static void sleep(Duration duration) {
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException ignore) {
            }
        }
    }
}
