package uk.xlab.teamcity.phabricator;

import java.io.File;

import static uk.xlab.teamcity.phabricator.CommonUtils.*;

public class Command {

    private final File workingDir;
    private String[] fullCommand;
    private ProcessBuilder processBuilder;
    private Process process;

    public Command(final String[] executableAndArguments, final String workingDirectory) {
        workingDir = isNullOrEmpty(workingDirectory) ? null : new File(workingDirectory);
        fullCommand = executableAndArguments;

        // ProcessBuilder requires the full command to be an array to prepend the
        // executable to the front;
        processBuilder = new ProcessBuilder(executableAndArguments);
        processBuilder.directory(workingDir);
        processBuilder.inheritIO();
    }

    public int executeAndWait() throws Exception {
        // May well fail and throw an exception but we will handle that in the agent
        // plugin.
        process = processBuilder.start();
        return process.waitFor();
    }

    @Override
    public String toString() {
        if (fullCommand.length < 1) {
            return "Command not setup";
        }

        String printableCommand = String.join(" ", fullCommand);

        // The workingDir might not have been explicitly set
        if (processBuilder.directory() == null) {
            return String.format("Command: %s", printableCommand);
        }

        return String.format("WorkingDir: %s - Command: %s", processBuilder.directory().toString(), printableCommand);
    }
}
