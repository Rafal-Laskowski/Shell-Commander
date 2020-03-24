package io.github.laskowski.shell.process;

import io.github.laskowski.shell.publishers.DefaultPublisher;
import io.github.laskowski.shell.runners.DefaultTaskRunner;
import io.github.laskowski.shell.scripts.Script;
import io.github.laskowski.shell.scripts.ShellScript;
import io.github.laskowski.shell.subscribers.DefaultSubscriber;
import io.github.laskowski.shell.subscribers.Subscriber;
import io.github.laskowski.shell.tasks.DefaultTask;
import io.github.laskowski.shell.utils.OperatingSystem;

import java.util.function.Function;

public class DefaultProcessKiller extends DefaultTask implements ProcessKiller {

    @Override
    public void kill(ProcessHandle pid) {
        DefaultTaskRunner.getInstance().run(this, new DefaultSubscriber(), pid);
    }

    @Override
    public String getName() {
        return "Process killer";
    }

    @Override
    public boolean isService() {
        return false;
    }

    @Override
    public Function<Subscriber, Boolean> getReadyFunction() {
        return DefaultPublisher::processFinished;
    }

    @Override
    public int getReadyFunctionTimeout() {
        return 5;
    }

    @Override
    public Script getScript(Object... arguments) {
        ProcessHandle processHandle = (ProcessHandle) arguments[0];
        return new ShellScript(getName()).command(killProcessCommand(), processHandle.pid());
    }

    private static String killProcessCommand() {
        if (OperatingSystem.get().equals(OperatingSystem.OS.WINDOWS)) {
            return "taskkill /PID %d /F";
        } else {
            return "kill -9 %d";
        }
    }
}
