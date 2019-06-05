package uk.xlab.teamcity.phabricator;

import static uk.xlab.teamcity.phabricator.CommonUtils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandBuilder {

    private String command = null;
    private String action = null;
    private String workingDir = null;
    private List<String> args = new ArrayList<String>();

    public CommandBuilder setWorkingDir(String workingDir) {
        if (isNullOrEmpty(workingDir))
            throw new IllegalArgumentException("Need to provide valid working directory");
        else
            this.workingDir = workingDir;
        return this;
    }

    public CommandBuilder setCommand(String cmd) {
        if (isNullOrEmpty(cmd))
            throw new IllegalArgumentException("Need to provide a valid command");
        else
            this.command = cmd;
        return this;
    }

    public CommandBuilder setAction(String action) {
        if (isNullOrEmpty(action))
            throw new IllegalArgumentException("Need to provide a valid action");
        else
            this.action = action;
        return this;
    }

    public CommandBuilder setArg(String arg) {
        if (isNullOrEmpty(arg))
            throw new IllegalArgumentException("Need to provide a valid argument");
        else
            this.args.add(arg);
        return this;
    }

    public CommandBuilder setFlagWithValueEquals(String key, String value) {
        this.args.add(String.format("%s=%s", formatFlag(key), value));
        return this;
    }

    public Command build() {
        if (isNullOrEmpty(this.command)) {
            throw new IllegalArgumentException("Must provide a command");
        }

        else {
            this.args.add(0, this.command);
        }

        if (!isNullOrEmpty(this.action)) {
            this.args.add(1, this.action);
        }

        return new Command(args.toArray(new String[args.size()]), this.workingDir);
    }

    private static String formatFlag(String flag) {
        Pattern withFlag = Pattern.compile("^\\-\\-[a-zA-Z0-9-]+$");
        Pattern singleWord = Pattern.compile("^\\w$");
        Matcher m = withFlag.matcher(flag.trim());
        Matcher m1 = singleWord.matcher(flag.trim());
        if (m.matches()) {
            return flag.trim();
        } else if (m1.matches()) {
            return String.format("--%s", flag);
        } else {
            throw new IllegalArgumentException(String.format("%s is not a valid flag", flag));
        }
    }
}
