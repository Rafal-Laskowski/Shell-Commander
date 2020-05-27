package io.github.laskowski.shell.output.messages;

import java.util.function.Predicate;

public interface ErrorDetectionStrategy extends Predicate<String> {
}
