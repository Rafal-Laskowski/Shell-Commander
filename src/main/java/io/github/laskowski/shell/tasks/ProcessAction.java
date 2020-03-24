package io.github.laskowski.shell.tasks;

import io.github.laskowski.shell.scripts.Script;
import io.github.laskowski.shell.utils.OperatingSystem;
import io.github.laskowski.shell.utils.Terminal;

import java.io.File;

public class ProcessAction implements Action<Script, ProcessBuilder> {

    @Override
    public ProcessBuilder build(Script script) {
        File scriptFile = script.getScriptFile();

        ProcessBuilder processBuilder = new ProcessBuilder(
                Terminal.getCommandExecutor(),
                Terminal.getExecutorArgument(),
                (OperatingSystem.OS.WINDOWS.equals(OperatingSystem.get()))? scriptFile.getName() : "./" + scriptFile.getName());
        processBuilder.directory(Script.DEFAULT_LOCATION);
        return processBuilder;
    }
}
