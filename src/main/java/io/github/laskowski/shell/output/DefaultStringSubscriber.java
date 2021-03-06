package io.github.laskowski.shell.output;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;
import io.github.laskowski.shell.output.messages.ErrorDetectionStrategy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

public class DefaultStringSubscriber implements StringSubscriber {
    protected Flow.Subscription subscription;
    protected final List<String> lines = new ArrayList<>();
    protected final ErrorDetectionStrategy errorDetectionStrategy;

    public DefaultStringSubscriber(@Nullable ErrorDetectionStrategy errorDetectionStrategy) {
        this.errorDetectionStrategy = errorDetectionStrategy;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(100);
    }

    @Override
    public void onNext(String item) {
        System.out.println(item);

        lines.add(item);
        this.subscription.request(100);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {}

    @Override
    public List<String> getLines() throws ErrorDetectedException {
        List<String> lineList = new ArrayList<>(lines).stream().filter(Objects::nonNull).collect(Collectors.toList());

        if (errorDetectionStrategy != null) {
            for (String line : lineList) {
                if (errorDetectionStrategy.test(line)) {
                    throw new ErrorDetectedException("Error Detected!\nLine [%s]", line);
                }
            }
        }

        return lineList;
    }
}
