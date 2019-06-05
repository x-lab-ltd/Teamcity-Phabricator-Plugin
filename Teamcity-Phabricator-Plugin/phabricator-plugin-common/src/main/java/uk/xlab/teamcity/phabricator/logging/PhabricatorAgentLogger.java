package uk.xlab.teamcity.phabricator.logging;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorAgentLogger implements PhabricatorPluginLogger {

    private final String LOGGING_PREFIX_TEMPLATE = "Phabricator Plugin - %s";

    @Override
    public void info(String message) {
        Loggers.AGENT.info(String.format(LOGGING_PREFIX_TEMPLATE, message));
    }

    @Override
    public void warn(String message, Exception e) {
        Loggers.AGENT.warn(String.format(LOGGING_PREFIX_TEMPLATE, message), e);
    }

    @Override
    public void error(String message, Exception e) {
        Loggers.AGENT.error(String.format(LOGGING_PREFIX_TEMPLATE, message), e);
    }
}
