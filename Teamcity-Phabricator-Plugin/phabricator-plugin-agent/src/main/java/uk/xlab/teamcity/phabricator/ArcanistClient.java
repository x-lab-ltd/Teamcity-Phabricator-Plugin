package uk.xlab.teamcity.phabricator;

import uk.xlab.teamcity.phabricator.logging.PhabricatorAgentLogger;

public class ArcanistClient {

    private final String arcPath;
    private final String workingDir;
    private final String conduitAPI;
    private final String token;

    private final PhabricatorAgentLogger agentLogLogger;

    public ArcanistClient(
            final String pathToArcanist,
            final String workingDirectory,
            final String phabricatorURL,
            final String conduitToken,
            final PhabricatorAgentLogger logger
    ) {
        arcPath = pathToArcanist;
        workingDir = workingDirectory;
        conduitAPI = phabricatorURL;
        token = conduitToken;
        agentLogLogger = logger;
    }

    public int patch(String diffId) {
        try {

            Command commandToExecute = new CommandBuilder()
                    .setCommand(arcPath)
                    .setAction("patch")
                    .setWorkingDir(workingDir)
                    .setArg("--diff")
                    .setArg(diffId)
                    .setFlagWithValueEquals("--conduit-uri", conduitAPI)
                    .setFlagWithValueEquals("--conduit-token", token)
                    .build();
            return commandToExecute.executeAndWait();

        } catch (IllegalArgumentException e) {
            agentLogLogger.warn("Building command failed", e);
        } catch (Exception e) {
            agentLogLogger.warn("Patching error", e);
        }

        return 1;
    }
}
