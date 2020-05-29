package io.github.laskowski.shell.output.service;

import java.time.Duration;

public class DefaultServiceReadyStrategy implements ServiceReadyStrategy {

    @Override
    public Duration getTimeout() {
        return Duration.ofSeconds(5);
    }

    @Override
    public String getTimeoutMessage() {
        return "";
    }

    @Override
    public ServiceReadyPredicate getReadyPredicate() {
        return message -> true;
    }
}
