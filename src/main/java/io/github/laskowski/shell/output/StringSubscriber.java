package io.github.laskowski.shell.output;

import io.github.laskowski.shell.exceptions.ErrorDetectedException;

import java.util.List;
import java.util.concurrent.Flow;

public interface StringSubscriber extends Flow.Subscriber<String> {
    List<String> getLines() throws ErrorDetectedException;
}
