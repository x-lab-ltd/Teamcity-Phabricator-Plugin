package uk.xlab.teamcity.phabricator;

import java.util.Map;

import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;

/**
 * At the start of a build gather the parameters the agent plugin requires and
 * set them in shared parameters. The agent can then read them from the shared
 * parameters.
 *
 */
public class PhabricatorBuildStartContextProcessor implements BuildStartContextProcessor {

    @Override
    public void updateParameters(BuildStartContext context) {
        // Get build parameters and put them in a shared location for access on the
        // agent
        Map<String, String> parameters = context.getBuild().getBuildOwnParameters();

        if (parameters.containsKey(Constants.BUILD_ID)) {
            context.addSharedParameter(Constants.BUILD_ID, parameters.get(Constants.BUILD_ID));
        }

        if (parameters.containsKey(Constants.DIFF_ID)) {
            context.addSharedParameter(Constants.DIFF_ID, parameters.get(Constants.DIFF_ID));
        }

        if (parameters.containsKey(Constants.HARBORMASTER_PHID)) {
            context.addSharedParameter(Constants.HARBORMASTER_PHID, parameters.get(Constants.HARBORMASTER_PHID));
        }

        if (parameters.containsKey(Constants.REVISION_ID)) {
            context.addSharedParameter(Constants.REVISION_ID, parameters.get(Constants.REVISION_ID));
        }
    }
}
