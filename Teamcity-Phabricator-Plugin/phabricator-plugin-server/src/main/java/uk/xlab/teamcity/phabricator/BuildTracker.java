package uk.xlab.teamcity.phabricator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jetbrains.buildServer.serverSide.SBuildFeatureDescriptor;
import jetbrains.buildServer.serverSide.SRunningBuild;

/**
 * Class that is run in a thread once a build has started. If the build does not
 * have the phabricator build feature then the tread should come to an end
 * otherwise wait for the build to finish and report back to phabricator
 * 
 * @author steven.cooney
 *
 */
public class BuildTracker implements Runnable {

	private SRunningBuild build;
	private PhabricatorServerLogger logger;
	private PhabricatorPluginConfig phabricatorConfig;

	public BuildTracker(SRunningBuild runningBuild, PhabricatorServerLogger phabLogger) {
		build = runningBuild;
		logger = phabLogger;

		phabricatorConfig = new PhabricatorPluginConfig();
		phabricatorConfig.setLogger(logger);
	}

	@Override
	public void run() {
		// Attempt to get the parameters set by the phabricator build feature. If non
		// are set then the feature is not turned on.
		Collection<SBuildFeatureDescriptor> phabricatorBuildFeatureParameters = build
				.getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE);

		// Check if the build is part of a configuration which
		// uses the phabricator build feature.
		if (!phabricatorBuildFeatureParameters.isEmpty()) {
			logger.info("Tracking build " + build.getBuildNumber());

			// Gather together all the build and phabricator parameters
			Map<String, String> params = new HashMap<>();
			params.putAll(build.getBuildOwnParameters());
			params.putAll(phabricatorBuildFeatureParameters.iterator().next().getParameters());

			// Setup plugin specific configuration
			// TODO: implement AppConfig as PluginConfig
			phabricatorConfig.setParameters(params);

			while (!build.isFinished()) {
				// Wait until the build finishes
			}

			logger.info(String.format("Build %s finished: %s", build.getBuildNumber(), build.getBuildStatus()));

			if (build.getStatusDescriptor().isSuccessful()) {
				logger.info("Successful Build");
			}
		}
	}
}
