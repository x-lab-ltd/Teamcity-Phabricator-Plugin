package uk.xlab.teamcity.phabricator;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorServerLogger implements PhabricatorPluginLogger {

    private final String LOGGING_PREFIX_TEMPLATE = "Phabricator Plugin - %s";

    @Override
    public void info(String message) {
        Loggers.SERVER.info(String.format(LOGGING_PREFIX_TEMPLATE, message));
    }

    @Override
    public void warn(String message, Exception e) {
        Loggers.SERVER.warn(String.format(LOGGING_PREFIX_TEMPLATE, message), e);
    }

    @Override
    public void error(String message, Exception e) {
        Loggers.SERVER.error(String.format(LOGGING_PREFIX_TEMPLATE, message), e);
    }
}
