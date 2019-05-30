package uk.xlab.teamcity.phabricator;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorPluginLogger {

	public void serverInfo(String message) {
        Loggers.SERVER.info(String.format("Phabricator Plugin: %s", message));
    }

    public void serverWarn(String message, Exception e) {
        Loggers.SERVER.warn(message, e);
    }

    public void agentInfo(String message) {
        Loggers.AGENT.info(String.format("Phabricator Plugin: %s", message));
    }
    
    public void agentWarn(String message, Exception e) {
        Loggers.AGENT.warn(String.format("Phabricator Plugin: %s", message), e);
    }
}
