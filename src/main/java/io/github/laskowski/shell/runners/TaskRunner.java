package io.github.laskowski.shell.runners;

import io.github.laskowski.shell.process.ProcessKiller;
import io.github.laskowski.shell.subscribers.DefaultSubscriber;
import io.github.laskowski.shell.subscribers.Subscriber;
import io.github.laskowski.shell.tasks.Task;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TaskRunner {

    default void run(Task task) {
        run(task, new DefaultSubscriber());
    }

    void run(Task task, Subscriber subscriber);

    void run(Task task, Subscriber subscriber, Object... arguments);

    void stop(Task task);

    default Optional<List<ProcessHandle>> getProcessesToKill(Task task, List<ProcessHandle> listOfProcessesBeforeStartingThread) {
        return task.isService() ?
                Optional.of(ProcessHandle
                        .allProcesses()
                        .filter(processHandle -> listOfProcessesBeforeStartingThread
                                .stream()
                                .noneMatch(ph -> ph.pid() == processHandle.pid()))
                        .collect(Collectors.toList())) : Optional.empty();
    }

    void stopAll();

    ProcessKiller getProcessKiller();
}
