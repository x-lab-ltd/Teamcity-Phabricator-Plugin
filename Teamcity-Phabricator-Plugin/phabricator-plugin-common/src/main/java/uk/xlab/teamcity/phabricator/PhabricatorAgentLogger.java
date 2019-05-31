package uk.xlab.teamcity.phabricator;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorAgentLogger implements IPhabricatorPluginLogger {

    @Override
    public void info(String message) {
        Loggers.AGENT.info(String.format("Phabricator Plugin - %s", message));
    }

    @Override
    public void warn(String message, Exception e) {
        Loggers.AGENT.warn(String.format("Phabricator Plugin - %s", message), e);
    }

    @Override
    public void error(String message, Exception e) {
        Loggers.AGENT.error(String.format("Phabricator Plugin - %s", message), e);
    }
}
