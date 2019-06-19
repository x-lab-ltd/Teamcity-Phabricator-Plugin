package uk.xlab.teamcity.phabricator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.agent.AgentBuildFeature;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.util.EventDispatcher;
import uk.xlab.teamcity.phabricator.logging.PhabricatorAgentLogger;
import uk.xlab.teamcity.phabricator.logging.PhabricatorBuildProgressLogger;

/**
 * Extend the AgentLifeCycleAdapter to check for builds which have the
 * Phabricator build feature enabled. If enabled then patch the updated code
 * using arcanist making sure the build is against these changed sources.
 *
 */
public class AgentBuildExtension extends AgentLifeCycleAdapter {

    private PhabricatorAgentLogger agentLogLogger;
    private PhabricatorBuildProgressLogger buildLogger;
    private PhabricatorPluginConfig phabricatorConfig;
    private boolean phabricatorTriggeredBuild = false;

    public AgentBuildExtension(@NotNull final EventDispatcher<AgentLifeCycleListener> eventDispatcher,
            @NotNull final PhabricatorAgentLogger phabLogger) {
        eventDispatcher.addListener(this);

        agentLogLogger = phabLogger;
        phabricatorConfig = new PhabricatorPluginConfig();
        phabricatorConfig.setLogger(agentLogLogger);
    }

    /**
     * From the associated parameters determine if the phabricator build feature is
     * enabled and we have all the information to successfully patch changes from
     * the phabricator differential revision
     */
    @Override
    public void buildStarted(@NotNull AgentRunningBuild runningBuild) {
        // Reset the check for a phabricator build
        phabricatorTriggeredBuild = false;

        // Setup logger to print to build output
        buildLogger = new PhabricatorBuildProgressLogger(runningBuild.getBuildLogger());

        // Attempt to get the parameters set by the phabricator build feature. If non
        // are set then the feature is not turned on.
        Collection<AgentBuildFeature> phabricatorBuildFeatureParameters = runningBuild
                .getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE);

        if (phabricatorBuildFeatureParameters.isEmpty()) {
            return;
        }

        // Check that the relevant build and phabricator settings/configurations are
        // present
        Map<String, String> params = new HashMap<>();
        params.putAll(phabricatorBuildFeatureParameters.iterator().next().getParameters());
        params.putAll(runningBuild.getSharedConfigParameters());

        // Setup plugin specific configuration
        phabricatorConfig.setParameters(params);

        // Now we have set all the parameters we need to check if
        // everything is present and correct for us to continue
        if (!phabricatorConfig.isPluginSetup()) {
            agentLogLogger.info("Plugin incorrectly configured");
            return;
        }

        phabricatorTriggeredBuild = true;
        agentLogLogger.info("Plugin ready");
        buildLogger.message("Active");

        buildLogger.logParameter(Constants.PHABRICATOR_URL_SETTING, phabricatorConfig.getPhabricatorURL().toString());
        buildLogger.logParameter(Constants.PHABRICATOR_ARCANIST_PATH_SETTING, phabricatorConfig.getPathToArcanist());
        buildLogger.logParameter(Constants.BUILD_ID, phabricatorConfig.getBuildId());
        buildLogger.logParameter(Constants.DIFF_ID, phabricatorConfig.getDiffId());
        buildLogger.logParameter(Constants.HARBORMASTER_PHID, phabricatorConfig.getHarbormasterPHID());
        buildLogger.logParameter(Constants.REVISION_ID, phabricatorConfig.getRevisionId());
    }

    /**
     * Once the sources have been updated via the native TeamCity process check if
     * the Phabricator build feature is enabled and use arcanist to patch in changes
     * from a associated phabricator differential revision.
     */
    @Override
    public void sourcesUpdated(@NotNull AgentRunningBuild runningBuild) {

        if (!phabricatorTriggeredBuild) {
            return;
        }

        buildLogger.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, "Attempting arc patch"));
        agentLogLogger.info("Attempting arc patch");

        ArcanistClient arcanistClient = new ArcanistClient(phabricatorConfig.getPathToArcanist(),
                runningBuild.getCheckoutDirectory().getPath(), phabricatorConfig.getPhabricatorURL().toString(),
                phabricatorConfig.getConduitToken(), agentLogLogger);

        int patchCode = arcanistClient.patch(phabricatorConfig.getDiffId());
        agentLogLogger.info(String.format("Arc patch exited with code: %s", patchCode));

        if (patchCode > 0) {
            runningBuild.stopBuild("Patch failed to apply. Check the agent output log for patch failure detals.");
            agentLogLogger.info("Patch failed to apply, stopping build");
            return;
        }

        buildLogger.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, "Patch completed"));
        agentLogLogger.info("Patch completed");
    }
}
