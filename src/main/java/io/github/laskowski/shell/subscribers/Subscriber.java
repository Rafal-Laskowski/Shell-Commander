package io.github.laskowski.shell.subscribers;

import java.util.List;
import java.util.concurrent.Flow;

public interface Subscriber extends Flow.Subscriber<String> {

    List<String> getLines();
}
