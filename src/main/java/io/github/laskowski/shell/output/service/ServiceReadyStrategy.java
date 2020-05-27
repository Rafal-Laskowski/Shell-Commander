package io.github.laskowski.shell.output.service;

import java.time.Duration;

public interface ServiceReadyStrategy {
    Duration getTimeout();

    String getTimeoutMessage();

    ServiceReadyPredicate getReadyPredicate();
}
