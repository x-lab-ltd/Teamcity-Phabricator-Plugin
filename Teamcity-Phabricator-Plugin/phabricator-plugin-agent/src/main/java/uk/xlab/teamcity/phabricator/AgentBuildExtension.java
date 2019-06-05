package uk.xlab.teamcity.phabricator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.agent.AgentBuildFeature;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.util.EventDispatcher;
import uk.xlab.teamcity.phabricator.logging.PhabricatorAgentLogger;

public class AgentBuildExtension extends AgentLifeCycleAdapter {

    private static final String OUTPUT_PREFIX = "Phabricator Plugin - %s";
    private PhabricatorAgentLogger agentLogLogger;
    private BuildProgressLogger buildLogger;
    private PhabricatorPluginConfig phabricatorConfig;
    private boolean phabricatorTriggeredBuild = false;

    public AgentBuildExtension(@NotNull final EventDispatcher<AgentLifeCycleListener> eventDispatcher,
            @NotNull final PhabricatorAgentLogger phabLogger) {
        eventDispatcher.addListener(this);

        agentLogLogger = phabLogger;
        phabricatorConfig = new PhabricatorPluginConfig();
        phabricatorConfig.setLogger(agentLogLogger);
    }

    @Override
    public void buildStarted(@NotNull AgentRunningBuild runningBuild) {
        // Setup logger to print to build output
        buildLogger = runningBuild.getBuildLogger();

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
            phabricatorTriggeredBuild = false;
            return;
        }

        phabricatorTriggeredBuild = true;
        agentLogLogger.info("Plugin ready");
        buildLogger.message(String.format(OUTPUT_PREFIX, "Active"));

        buildLogger.message(String.format(OUTPUT_PREFIX, String.format("%s: %s", Constants.PHABRICATOR_URL_SETTING,
                phabricatorConfig.getPhabricatorURL().toString())));
        buildLogger.message(String.format(OUTPUT_PREFIX, String.format("%s: %s",
                Constants.PHABRICATOR_ARCANIST_PATH_SETTING, phabricatorConfig.getPathToArcanist())));
        buildLogger.message(String.format(OUTPUT_PREFIX,
                String.format("%s: %s", Constants.BUILD_ID, phabricatorConfig.getBuildId())));
        buildLogger.message(String.format(OUTPUT_PREFIX,
                String.format("%s: %s", Constants.DIFF_ID, phabricatorConfig.getDiffId())));
        buildLogger.message(String.format(OUTPUT_PREFIX, String.format("%s: %s", Constants.HARBORMASTER_PHID,
                phabricatorConfig.getPhabricatorURL().toString())));
        buildLogger.message(String.format(OUTPUT_PREFIX,
                String.format("%s: %s", Constants.REVISION_ID, phabricatorConfig.getRevisionId())));
    }

    @Override
    public void sourcesUpdated(@NotNull AgentRunningBuild runningBuild) {

        if (!phabricatorTriggeredBuild) {
            return;
        }

        buildLogger.message(String.format(OUTPUT_PREFIX, "Attempting arc patch"));
        agentLogLogger.info("Attempting arc patch");

        ArcanistClient arcanistClient = new ArcanistClient(phabricatorConfig.getPathToArcanist(),
                runningBuild.getCheckoutDirectory().getPath(), phabricatorConfig.getPhabricatorURL().toString(),
                phabricatorConfig.getConduitToken(), agentLogLogger);

        int patchCode = arcanistClient.patch(phabricatorConfig.getDiffId());

        if (patchCode > 0) {
            runningBuild.stopBuild("Patch failed to apply. Check the agent output log for patch failure detals.");
        }
    }
}
