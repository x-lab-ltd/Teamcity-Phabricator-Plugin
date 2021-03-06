package uk.xlab.teamcity.phabricator.logging;

import jetbrains.buildServer.log.Loggers;
import uk.xlab.teamcity.phabricator.Constants;

/**
 * Logging class which outputs messages to TeamCity Server Logs. Any logging
 * from this class is also prefixed with this plugin's name
 *
 */
public final class PhabricatorServerLogger implements PhabricatorPluginLogger {

    @Override
    public void info(String message) {
        Loggers.SERVER.info(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message));
    }

    @Override
    public void warn(String message, Exception e) {
        Loggers.SERVER.warn(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e);
    }

    @Override
    public void error(String message, Exception e) {
        Loggers.SERVER.error(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e);
    }
}
