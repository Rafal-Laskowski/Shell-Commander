package io.github.laskowski.shell;

import java.util.concurrent.Flow;

public interface Publisher {

    void startPublishing(Process process);

    void registerSubscriber(Flow.Subscriber<String> subscriber);

    void stop();
}
