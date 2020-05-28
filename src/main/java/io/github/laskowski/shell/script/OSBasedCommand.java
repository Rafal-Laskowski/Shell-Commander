package io.github.laskowski.shell.script;

public interface OSBasedCommand {
    String getWindowsCommand();
    String getMacCommand();
    String getUnixCommand();
}
