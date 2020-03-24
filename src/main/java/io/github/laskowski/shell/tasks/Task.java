package io.github.laskowski.shell.tasks;

import io.github.laskowski.shell.rules.RetryRule;
import io.github.laskowski.shell.scripts.Script;
import io.github.laskowski.shell.subscribers.Subscriber;

import java.util.function.Function;

public interface Task {

    void start();

    void start(Object... arguments);

    void startPublishing(Subscriber subscriber);

    void stop();

    String getName();

    boolean isService();

    Function<Subscriber, Boolean> getReadyFunction();

    int getReadyFunctionTimeout();

    Script getScript(Object... arguments);

    default boolean retry() {
        RetryRule retryRuleAnnotation = this.getClass().getAnnotation(RetryRule.class);
        return retryRuleAnnotation != null;
    }
}
