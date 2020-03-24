package io.github.laskowski.shell.subscribers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

public class DefaultSubscriber implements Subscriber {
    protected volatile Flow.Subscription subscription;
    protected final List<String> lines = new ArrayList<>();

    @Override
    public List<String> getLines() {
        return new ArrayList<>(lines).stream().filter(Objects::nonNull).collect(Collectors.toList());
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
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
