package uk.xlab.teamcity.phabricator;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorServerLogger implements IPhabricatorPluginLogger {

    @Override
    public void info(String message) {
        Loggers.SERVER.info(String.format("Phabricator Plugin - %s", message));
    }

    @Override
    public void warn(String message, Exception e) {
        Loggers.SERVER.warn(String.format("Phabricator Plugin - %s", message), e);
    }

    @Override
    public void error(String message, Exception e) {
        Loggers.SERVER.error(String.format("Phabricator Plugin - %s", message), e);
    }
}
