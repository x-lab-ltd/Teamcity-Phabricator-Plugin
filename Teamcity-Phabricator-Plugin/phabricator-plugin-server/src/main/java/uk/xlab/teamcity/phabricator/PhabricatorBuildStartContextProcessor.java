package uk.xlab.teamcity.phabricator;

import java.util.Map;

import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;

public class PhabricatorBuildStartContextProcessor implements BuildStartContextProcessor {

    @Override
    public void updateParameters(BuildStartContext context) {
        // Get build parameters and put them in a shared location for access on the
        // agent
        Map<String, String> parameters = context.getBuild().getBuildOwnParameters();

        context.addSharedParameter(Constants.BRANCH_NAME, parameters.get(Constants.BRANCH_NAME));
        context.addSharedParameter(Constants.BUILD_ID, parameters.get(Constants.BUILD_ID));
        context.addSharedParameter(Constants.DIFF_ID, parameters.get(Constants.DIFF_ID));
        context.addSharedParameter(Constants.HARBORMASTER_PHID, parameters.get(Constants.HARBORMASTER_PHID));
        context.addSharedParameter(Constants.REVISION_ID, parameters.get(Constants.REVISION_ID));
    }
}
