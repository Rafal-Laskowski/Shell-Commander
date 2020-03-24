package io.github.laskowski.shell.publishers;

import io.github.laskowski.shell.subscribers.Subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class DefaultPublisher extends SubmissionPublisher<String> implements Publisher<Process> {
    private final String name;

    public DefaultPublisher(String name) {
        this.name = name;
    }

    @Override
    public void startPublishing(Process process) {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";

        boolean isOutReady;
        boolean isProcessAlive;

        while (process.isAlive() && !Thread.currentThread().isInterrupted()) {
            try {
                do {
                    isOutReady = stdInput.ready();
                    if (isOutReady) {
                        line = stdInput.readLine();
                        publishMessage(line);
                    }

                    isProcessAlive = process.isAlive();
                    if (!isProcessAlive) {
                        line = null;
                        process.waitFor(1000, TimeUnit.MILLISECONDS);
                    }

                    Thread.sleep(1);
                } while (line != null && !Thread.currentThread().isInterrupted());
                process.waitFor(100, TimeUnit.MILLISECONDS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException ignore) {}
        }

        try {
            stdInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        publishMessage("Process finished.");
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        if (subscriber != null) {
            this.subscribe(subscriber);
        } else throw new IllegalArgumentException("You need to provide subscriber to DefaultPublisher");
    }

    @Override
    public void stop() {
        Thread.currentThread().interrupt();
    }

    protected void publishMessage(String message) {
        this.submit(String.format("%s | %s", name, message));
    }

    public static boolean processFinished(List<String> lines) {
        return lines.stream().anyMatch(x -> x != null && x.contains("Process finished."));
    }

    public static boolean processFinished(Subscriber subscriber) {
        if (subscriber != null) {
            return processFinished(subscriber.getLines());
        } else {
            throw new IllegalArgumentException("Cannot check if process is finished because subscriber is null");
        }
    }
}
