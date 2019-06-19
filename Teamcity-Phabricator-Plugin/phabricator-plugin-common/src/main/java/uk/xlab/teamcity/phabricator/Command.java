package uk.xlab.teamcity.phabricator;

import java.io.File;

import static uk.xlab.teamcity.phabricator.CommonUtils.*;

/**
 * Wrapper for executing a command on the host system
 * 
 */
public class Command {

    private final File workingDir;
    private String[] fullCommand;
    private ProcessBuilder processBuilder;
    private Process process;

    /**
     * Compose the command which is to be executed
     * 
     * @param executableAndArguments An array of the command to be executed.
     * @param workingDirectory       The directory the command will be executed
     *                               from.
     */
    public Command(final String[] executableAndArguments, final String workingDirectory) {
        workingDir = isNullOrEmpty(workingDirectory) ? null : new File(workingDirectory);
        fullCommand = executableAndArguments;

        // ProcessBuilder requires the full command to be an array with the
        // executable the first element;
        processBuilder = new ProcessBuilder(executableAndArguments);
        processBuilder.directory(workingDir);
        processBuilder.inheritIO();
    }

    /**
     * Execute the command and wait for it to finish gathering the exit code of the
     * process
     * 
     * @return The exit code from the command which has been executed.
     * @throws Exception If there is an exception when running the command throw to
     *                   allowing the executing class to deal with error handling.
     */
    public int executeAndWait() throws Exception {
        // May well fail and throw an exception but we will handle that in the agent
        // plugin.
        process = processBuilder.start();
        return process.waitFor();
    }

    /**
     * Output the command and working directory for this class. It should only be
     * used sparingly.
     */
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
