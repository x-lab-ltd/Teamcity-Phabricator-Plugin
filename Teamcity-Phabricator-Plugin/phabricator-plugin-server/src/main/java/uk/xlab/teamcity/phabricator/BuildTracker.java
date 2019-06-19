package uk.xlab.teamcity.phabricator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuildFeatureDescriptor;
import jetbrains.buildServer.serverSide.SRunningBuild;
import uk.xlab.teamcity.phabricator.logging.PhabricatorServerLogger;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Class that is run in a thread once a build has started. If the build does not
 * have the phabricator build feature then the tread should come to an end
 * otherwise wait for the build to finish and report back to phabricator
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
            phabricatorConfig.setParameters(params);

            // Now we have set all the parameters we need to check if
            // everything is present and correct for us to continue
            if (!phabricatorConfig.isPluginSetup()) {
                logger.info("Plugin incorrectly configured");
                return;
            }

            while (!build.isFinished()) {
                // Wait until the build finishes
            }

            logger.info(String.format("Build %s finished: %s", build.getBuildNumber(), build.getBuildStatus()));
            String harbormasterBuildStatus = parseTeamCityBuildStatus(build.getBuildStatus());
            notifyHarbormaster(harbormasterBuildStatus);
        }
    }

    /**
     * Compose and dispatch an API call to harbormaster to notify of the build
     * status
     */
    private void notifyHarbormaster(String buildStatus) {
        URL phabricatorURL = phabricatorConfig.getPhabricatorURL();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String requestEndpoint = String.format("%s/api/harbormaster.sendmessage", phabricatorURL.toString());
            logger.info(String.format("Sending build status to: %s", requestEndpoint));
            HttpPost httpPost = new HttpPost(requestEndpoint);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("api.token", phabricatorConfig.getConduitToken()));
            params.add(new BasicNameValuePair("buildTargetPHID", phabricatorConfig.getHarbormasterPHID()));
            params.add(new BasicNameValuePair("type", buildStatus));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseText = EntityUtils.toString(response.getEntity());
                logger.info(String.format("Phabricator response: %s", responseText));
            }

        } catch (Exception exception) {
            // Just catching any Exception because the request to Phabricator may have
            // failed and we should investigate
            logger.error("Request to harbormaster failed", exception);
        }
    }

    private String parseTeamCityBuildStatus(Status buildFinishedStatus) {
        if (buildFinishedStatus.isSuccessful()) {
            return "pass";
        }

        return "fail";
    }
}
