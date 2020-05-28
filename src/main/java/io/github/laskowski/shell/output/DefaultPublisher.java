package io.github.laskowski.shell.output;

import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultPublisher extends SubmissionPublisher<String> implements Publisher<Process> {
    public static final String LAST_MESSAGE = "PROCESS FINISHED";
    protected final MessageExclusionStrategy messageExclusionStrategy;
    protected volatile boolean shouldPublish = true;

    public DefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy) {
        this.messageExclusionStrategy = messageExclusionStrategy;
    }

    public void startPublishing(Process process) {
        startPublishing(process, false);
    }

    public void startPublishing(Process process, boolean includeErrorStream) {
        Consumer<String> publishMessageIfPresent = line -> publishMessage(line, messageExclusionStrategy);

        Thread processDetectionThread = new Thread(new ProcessDetectionRunnable(process));

        LineReader inputStreamLineReader = new LineReader(process.getInputStream());
        LineReader errorStreamLineReader = new LineReader(process.getErrorStream());

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        processDetectionThread.start();

        while (shouldPublish) {
            try {
                inputStreamLineReader.readLine().ifPresent(publishMessageIfPresent);

                if (includeErrorStream) {
                    errorStreamLineReader.readLine().ifPresent(publishMessageIfPresent);
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

    protected static class Sleeper {

        static void sleep(Duration duration) {
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException ignore) {
            }
        }
    }

    protected class ProcessDetectionRunnable implements Runnable {
        private volatile Process process;

        ProcessDetectionRunnable(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            while(process.isAlive()) {
                Sleeper.sleep(Duration.ofMillis(500));
            }

            Sleeper.sleep(Duration.ofMillis(500));
            stopPublishing();
        }
    }

    protected class LineReader {
        private BufferedReader stdInput;

        LineReader(InputStream inputStream) {
            stdInput = new BufferedReader(new InputStreamReader(inputStream));
        }

        public Optional<String> readLine() throws IOException {
            boolean isOutReady = stdInput.ready();
            if (isOutReady) {
                return Optional.ofNullable(stdInput.readLine());
            } else {
                return Optional.empty();
            }
        }
    }
}
