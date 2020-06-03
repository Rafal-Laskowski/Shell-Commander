package io.github.laskowski.shell.script;

public interface CommandEntity {
    CommandType getCommandType();
    String getAnnotationValue();
    String getVariableValue();
}
