package uk.xlab.teamcity.phabricator;

import uk.xlab.teamcity.phabricator.logging.PhabricatorAgentLogger;

/**
 * Wrapper for arcanist and patching in changes from differential revisions
 *
 */
public class ArcanistClient {

    private final String arcPath;
    private final String workingDir;
    private final String conduitAPI;
    private final String token;

    private final PhabricatorAgentLogger agentLogLogger;

    public ArcanistClient(final String pathToArcanist, final String workingDirectory, final String phabricatorURL,
            final String conduitToken, final PhabricatorAgentLogger logger) {
        arcPath = pathToArcanist;
        workingDir = workingDirectory;
        conduitAPI = phabricatorURL;
        token = conduitToken;
        agentLogLogger = logger;
    }

    /**
     * Run "arc patch" using arcanist on the build agent to pull in changes from a
     * differential revision.
     * 
     * @param diffId The identifying diff which has the changes to patch within
     *               differential
     * @return The exit code for "arc patch" or 1 if exceptions occur
     */
    public int patch(String diffId) {
        try {

            Command commandToExecute = new CommandBuilder().setCommand(arcPath).setAction("patch")
                    .setWorkingDir(workingDir).setArg("--diff").setArg(diffId)
                    .setFlagWithValueEquals("--conduit-uri", conduitAPI)
                    .setFlagWithValueEquals("--conduit-token", token).build();

            agentLogLogger.info(String.format("Running Command: %s", commandToExecute.toString()));

            return commandToExecute.executeAndWait();

        } catch (IllegalArgumentException e) {
            agentLogLogger.warn("Building command failed", e);
        } catch (Exception e) {
            agentLogLogger.warn("Patching error", e);
        }

        return 1;
    }
}
