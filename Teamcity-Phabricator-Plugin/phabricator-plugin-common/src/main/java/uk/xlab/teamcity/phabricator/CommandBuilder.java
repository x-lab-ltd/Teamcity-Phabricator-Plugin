package uk.xlab.teamcity.phabricator;

import static uk.xlab.teamcity.phabricator.CommonUtils.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Build up the command which will be executed on the agent during relevant
 * builds
 *
 */
public class CommandBuilder {

    private String command = null;
    private String action = null;
    private String workingDir = null;
    private List<String> args = new ArrayList<String>();

    /**
     * Set the directory which the command should be run from.
     * 
     * @param workingDir The directory the command should be run from.
     * @return The builder instance so things can be chained.
     */
    public CommandBuilder setWorkingDir(String workingDir) {
        if (isNullOrEmpty(workingDir)) {
            throw new IllegalArgumentException("Need to provide valid working directory");
        } else {
            this.workingDir = workingDir;
        }
        return this;
    }

    /**
     * Set the command/executable that will be run.
     * 
     * @param cmd The command to be executed.
     * @return The builder instance so things can be chained.
     */
    public CommandBuilder setCommand(String cmd) {
        if (isNullOrEmpty(cmd)) {
            throw new IllegalArgumentException("Need to provide a valid command");
        } else {
            this.command = cmd;
        }
        return this;
    }

    /**
     * Set the action the command will perform this comes immediately after the
     * executable. E.g git pull where pull is the action.
     * 
     * @param action
     * @return The builder instance so things can be chained.
     */
    public CommandBuilder setAction(String action) {
        if (isNullOrEmpty(action)) {
            throw new IllegalArgumentException("Need to provide a valid action");
        } else {
            this.action = action;
        }
        return this;
    }

    /**
     * Set arguments that will be added to the command which will be executed
     * 
     * @param arg An argument to be added to the command
     * @return The builder instance so things can be chained.
     */
    public CommandBuilder setArg(String arg) {
        if (isNullOrEmpty(arg)) {
            throw new IllegalArgumentException("Need to provide a valid argument");
        } else {
            this.args.add(arg);
        }
        return this;
    }

    /**
     * Set arguments which take the form of a key value pair. E.g token=12344
     * 
     * @param key   The initial part of the argument
     * @param value The value of the argument to be added after an the key and
     *              equals
     * @return The builder instance so things can be chained.
     */
    public CommandBuilder setFlagWithValueEquals(String key, String value) {
        this.args.add(String.format("%s=%s", key, value));
        return this;
    }

    /**
     * Using the values set on this builder create a Command object than can be
     * executed.
     * 
     * @return Command which and be executed at a later point
     */
    public Command build() {
        if (isNullOrEmpty(this.command)) {
            throw new IllegalArgumentException("Must provide a command");
        } else {
            this.args.add(0, this.command);
        }

        if (!isNullOrEmpty(this.action)) {
            this.args.add(1, this.action);
        }

        return new Command(args.toArray(new String[args.size()]), this.workingDir);
    }
}
