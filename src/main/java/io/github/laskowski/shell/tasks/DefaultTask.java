package io.github.laskowski.shell.tasks;

import io.github.laskowski.shell.exceptions.TimeoutException;
import io.github.laskowski.shell.publishers.DefaultPublisher;
import io.github.laskowski.shell.publishers.Publisher;
import io.github.laskowski.shell.subscribers.Subscriber;
import io.github.laskowski.shell.utils.Wait;

import java.io.IOException;

public abstract class DefaultTask implements Task {
    private volatile Publisher<Process> publisher;
    private volatile Process process;

    public DefaultTask(Publisher<Process> publisher) {
        this.publisher = publisher;
    }

    public DefaultTask() {}

    @Override
    public void start() {
        start(new Object[0]);
    }

    @Override
    public void start(Object... arguments) {
        try {
            setDefaultPublisher();

            try {
                process = new ProcessAction().build(getScript(arguments)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            if (retry()) {
                start(arguments);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void stop() {
        Wait.until(5, () -> process != null);
        publisher.stop();

        process.destroy();
        Wait.until(10, () -> !process.isAlive());
    }

    @Override
    public void startPublishing(Subscriber subscriber) {
        try {
            Wait.until(5, () -> process != null);
            publisher.registerSubscriber(subscriber);
            publisher.startPublishing(process);
        } catch (TimeoutException e) {
            throw new IllegalStateException("Cannot start output listener. Process is null");
        }
    }

    private void setDefaultPublisher() {
        if (this.publisher == null) {
            this.publisher = new DefaultPublisher(getName());
        }
    }
}
