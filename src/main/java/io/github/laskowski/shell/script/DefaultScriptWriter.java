package io.github.laskowski.shell.script;

import io.github.laskowski.os.OperatingSystem;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public class DefaultScriptWriter extends TXTWriter implements ScriptWriter {
    private static final DefaultScriptInfoProvider SCRIPT_INFO_PROVIDER = DefaultScriptInfoProvider.getInstance();

    public DefaultScriptWriter(String fileName) {
        this(SCRIPT_INFO_PROVIDER.getScriptDirectory(), fileName.concat(SCRIPT_INFO_PROVIDER.getShellArguments().getExtension()));
    }

    public DefaultScriptWriter(File directory, String fileName) {
        super(directory, fileName);
    }

    public DefaultScriptWriter runCommands(Class<?> clazz) {
        List<CommandEntity> commands = new CommandsMapper(clazz).getCommands();
        for (CommandEntity command : commands) {
            switch (command.getCommandType()) {
                case CD:
                    cd(command.getVariableValue());
                    break;
                case VARIABLE:
                    setVariable(command.getAnnotationValue(), command.getVariableValue());
                    break;
                case RUN:
                    command(command.getVariableValue());
                    break;
                default:
                    throw new RuntimeException("Not implemented");
            }
        }

        return this;
    }

    public DefaultScriptWriter setVariable(String variable, String value) {
        super.write("%s %s=%s", SCRIPT_INFO_PROVIDER.getShellArguments().getVariableCommand(), variable, value);
        super.newLine();
        return this;
    }

    public DefaultScriptWriter setVariable(String variable, Optional<String> optionalValue) {
        optionalValue.ifPresent(s -> setVariable(variable, s));
        return this;
    }

    public DefaultScriptWriter cd(String path) {
        super.write("cd %s", path);
        super.newLine();
        return this;
    }

    public DefaultScriptWriter command(String string, Object... args) {
        super.write(String.format(Objects.requireNonNull(string), args));
        super.newLine();
        return this;
    }

    public DefaultScriptWriter osBasedCommand(OSBasedCommand osBasedCommand) {
        OperatingSystem operatingSystem = SCRIPT_INFO_PROVIDER.getOperatingSystemDiscoveryStrategy().getOS();
        switch (operatingSystem) {
            case WINDOWS: command(osBasedCommand.getWindowsCommand());
                break;
            case MAC: command(osBasedCommand.getMacCommand());
                break;
            case UNIX: command(osBasedCommand.getUnixCommand());
                break;
        }

        return this;
    }

    @Override
    public Script build() {
        return this::save;
    }
}
