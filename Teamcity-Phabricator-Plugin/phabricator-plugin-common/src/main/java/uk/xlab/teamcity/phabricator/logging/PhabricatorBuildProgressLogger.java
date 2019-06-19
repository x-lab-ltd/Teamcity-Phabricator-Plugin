package uk.xlab.teamcity.phabricator.logging;

import jetbrains.buildServer.agent.BuildProgressLogger;
import uk.xlab.teamcity.phabricator.Constants;

/**
 * Logging class which outputs messages to the Build Log. Any logging from this
 * class is also prefixed with this plugin's name
 *
 */
public class PhabricatorBuildProgressLogger {
    private BuildProgressLogger buildLogger;

    public PhabricatorBuildProgressLogger(BuildProgressLogger buildProcessLogger) {
        buildLogger = buildProcessLogger;
    }

    public void message(String message) {
        buildLogger.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message));
    }

    public void logParameter(String parameter, String value) {
        buildLogger.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, String.format("%s: %s", parameter, value)));
    }
}
