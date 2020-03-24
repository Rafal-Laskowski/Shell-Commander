package io.github.laskowski.shell.example;

import io.github.laskowski.shell.scripts.Script;
import io.github.laskowski.shell.scripts.ShellScript;
import io.github.laskowski.shell.subscribers.Subscriber;
import io.github.laskowski.shell.tasks.DefaultTask;

import java.util.function.Function;

public class PythonCheckTask extends DefaultTask {

    @Override
    public String getName() {
        return "PythonCheck";
    }

    @Override
    public boolean isService() {
        return false;
    }

    @Override
    public Function<Subscriber, Boolean> getReadyFunction() {
        return subscriber -> subscriber.getLines().stream().anyMatch(line -> line.matches("(.+)(Python \\d.\\d.\\d)"));
    }

    @Override
    public int getReadyFunctionTimeout() {
        return 5;
    }

    @Override
    public Script getScript(Object... arguments) {
        return new ShellScript(getName()).command("python --version");
    }
}
