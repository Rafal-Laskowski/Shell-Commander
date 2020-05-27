package io.github.laskowski.shell.script;

interface CommandEntity {
    CommandType getCommandType();
    String getAnnotationValue();
    String getVariableValue();
}
