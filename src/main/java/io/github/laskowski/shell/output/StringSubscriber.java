package io.github.laskowski.shell.output;

import java.util.List;
import java.util.concurrent.Flow;

public interface StringSubscriber extends Flow.Subscriber<String> {
    List<String> getLines();
}
