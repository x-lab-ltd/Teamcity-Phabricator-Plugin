package uk.xlab.teamcity.phabricator;

import jetbrains.buildServer.log.Loggers;

public final class PhabricatorServerLogger implements IPhabricatorPluginLogger {

	@Override
	public void info(String message) {
		Loggers.SERVER.info(String.format("Phabricator Plugin: %s", message));
	}

	@Override
    public void warn(String message, Exception e) {
        Loggers.SERVER.warn(String.format("Phabricator Plugin: %s", message), e);
    }

//    public void agentInfo(String message) {
//        Loggers.AGENT.info(String.format("Phabricator Plugin: %s", message));
//    }
//    
//    public void agentWarn(String message, Exception e) {
//        Loggers.AGENT.warn(String.format("Phabricator Plugin: %s", message), e);
//    }

}
