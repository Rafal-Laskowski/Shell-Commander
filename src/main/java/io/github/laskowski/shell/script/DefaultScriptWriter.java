package io.github.laskowski.shell.script;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DefaultScriptWriter extends TXTWriter implements ScriptWriter {
    private static final DefaultScriptInfoProvider SCRIPT_INFO_PROVIDER = DefaultScriptInfoProvider.getInstance();

    public DefaultScriptWriter(String fileName) {
        super(SCRIPT_INFO_PROVIDER.getScriptDirectory(), fileName.concat(SCRIPT_INFO_PROVIDER.getShellArguments().getExtension()));
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

    @Override
    public Script build() {
        return this::save;
    }
}
