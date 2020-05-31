package io.github.laskowski.shell.output;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;
import io.github.laskowski.shell.output.messages.MessageExclusionStrategy;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class DefaultPublisher extends SubmissionPublisher<String> implements Publisher<Process> {
    public static final String LAST_MESSAGE = "PROCESS FINISHED";
    protected final MessageExclusionStrategy messageExclusionStrategy;
    protected final ErrorDetectionStrategy errorDetectionStrategy;
    protected volatile boolean shouldPublish = true;

    public DefaultPublisher(@Nullable MessageExclusionStrategy messageExclusionStrategy, @Nullable ErrorDetectionStrategy errorDetectionStrategy) {
        this.messageExclusionStrategy = messageExclusionStrategy;
        this.errorDetectionStrategy = errorDetectionStrategy;
    }

    public void startPublishing(Process process) throws ErrorDetectedException {
        startPublishing(process, false);
    }

    public void startPublishing(Process process, boolean includeErrorStream) throws ErrorDetectedException {

        Thread processDetectionThread = new Thread(new ProcessDetectionRunnable(process));

        LineReader inputStreamLineReader = new LineReader(process.getInputStream());
        LineReader errorStreamLineReader = new LineReader(process.getErrorStream());

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        processDetectionThread.start();

        while (shouldPublish) {
            try {
                System.out.println("1");
                publish(inputStreamLineReader);

                if (includeErrorStream) {
                    System.out.println("2");
                    publish(errorStreamLineReader);
                }

                System.out.println("3");
                process.waitFor(1, TimeUnit.MILLISECONDS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException ignore) {}
        }
        System.out.println("DEFAULT PUBLISHER STOPPED");

        try {
            stdInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        publishMessage(LAST_MESSAGE, messageExclusionStrategy, errorDetectionStrategy);
    }

    @Override
    public void stop() {
        shouldPublish = false;
    }

    @Override
    public void publishMessage(String message, @Nullable MessageExclusionStrategy messageExclusionStrategy, @Nullable ErrorDetectionStrategy errorDetectionStrategy) throws ErrorDetectedException {
        if (message != null) {
            if (errorDetectionStrategy != null) {
                if (errorDetectionStrategy.test(message)) {
                    throw new ErrorDetectedException("Error Detected!\nLine [%s]", message);
                }
            }

            if (messageExclusionStrategy == null) {
                submit(message);
            } else {
                if (!messageExclusionStrategy.test(message)) {
                    submit(message);
                }
            }
        }
    }

    @Override
    public void registerSubscriber(Flow.Subscriber<String> subscriber) {
        if (subscriber != null) {
            this.subscribe(subscriber);
        } else throw new IllegalArgumentException("You need to provide subscriber to Publisher!");
    }

    private void publish(LineReader lineReader) throws IOException, ErrorDetectedException {
        publishMessage(lineReader.readLine(), messageExclusionStrategy, errorDetectionStrategy);
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
            stop();
        }
    }

    protected static class LineReader {
        private BufferedReader stdInput;

        LineReader(InputStream inputStream) {
            stdInput = new BufferedReader(new InputStreamReader(inputStream));
        }

        public String readLine() throws IOException {
            boolean isOutReady = stdInput.ready();
            if (isOutReady) {
                return stdInput.readLine();
            } else {
                return null;
            }
        }
    }
}
