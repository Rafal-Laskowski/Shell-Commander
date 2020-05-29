package io.github.laskowski.shell.output;

public class DefaultOutputListener implements OutputListener<Process> {

    @Override
    public Publisher<Process> getPublisher() {
        return new DefaultPublisher(null, null);
    }

    @Override
    public StringSubscriber getSubscriber() {
        return new DefaultStringSubscriber();
    }
}
