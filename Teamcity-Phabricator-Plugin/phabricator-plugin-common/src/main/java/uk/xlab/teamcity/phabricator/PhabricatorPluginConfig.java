package uk.xlab.teamcity.phabricator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import static uk.xlab.teamcity.phabricator.CommonUtils.*;

/**
 * Holds all the parameters set on the build applied by the harbormaster trigger
 * from phabricator. It also has the methods used to communicate the build
 * result back to harbormaster
 * 
 * @author steven.cooney
 *
 */
public class PhabricatorPluginConfig {

    private PhabricatorPluginLogger logger;
    private Map<String, String> params;

    // Build Feature Variables
    private URL phabricatorUrl;
    private String conduitToken;

    // Harbormaster Variables
    private String branchName;
    private String buildId;
    private String diffId;
    private String harbormasterPHID;
    private String revisionId;

    /**
     * Set the appropriate logger depending if the class is called from the SERVER
     * or AGENT
     * 
     * @param logger
     */
    public void setLogger(PhabricatorPluginLogger logger) {
        this.logger = logger;
    }

    /**
     * Take a copy of all build parameters which will then be parsed
     * 
     * @param parameters
     */
    public void setParameters(Map<String, String> parameters) {
        params = parameters;

        logger.info("Looking for parameters");
        for (String param : params.keySet()) {
            if (!isNullOrEmpty(param)) {
                logger.info(String.format("Found %s", param));

                switch (param) {
                case Constants.PHABRICATOR_URL_SETTING:
                    logger.info(
                            String.format("Found Phabrictor URL: %s", params.get(Constants.PHABRICATOR_URL_SETTING)));
                    try {
                        phabricatorUrl = parsePhabricatorURL(params.get(Constants.PHABRICATOR_URL_SETTING));
                    } catch (MalformedURLException e) {
                        logger.warn(String.format("Failed to parse phabricator URL: %s",
                                params.get(Constants.PHABRICATOR_URL_SETTING)), e);
                    }
                    break;
                case Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING:
                    logger.info("Found Phabricator Conduit Token");
                    conduitToken = params.get(Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING);
                    break;
                case Constants.BRANCH_NAME:
                    logger.info(String.format("Found branch name: %s", params.get(Constants.BRANCH_NAME)));
                    branchName = params.get(Constants.BRANCH_NAME);
                    break;
                case Constants.BUILD_ID:
                    logger.info(String.format("Found build id: %s", params.get(Constants.BUILD_ID)));
                    buildId = params.get(Constants.BUILD_ID);
                    break;
                case Constants.DIFF_ID:
                    logger.info(String.format("Found diff ID: %s", params.get(Constants.DIFF_ID)));
                    diffId = params.get(Constants.DIFF_ID);
                    break;
                case Constants.HARBORMASTER_PHID:
                    logger.info(String.format("Found harbormaster target PHID: %s",
                            params.get(Constants.HARBORMASTER_PHID)));
                    harbormasterPHID = params.get(Constants.HARBORMASTER_PHID);
                    break;
                case Constants.REVISION_ID:
                    logger.info(String.format("Found revision ID: %s", params.get(Constants.REVISION_ID)));
                    revisionId = params.get(Constants.REVISION_ID);
                    break;
                default:
                    // Another parameter which we do not care about
                    break;
                }
            }
        }
    }

    public boolean isPluginSetup() {
        if (!isNull(phabricatorUrl) && !isNullOrEmpty(branchName) && !isNullOrEmpty(buildId) && !isNullOrEmpty(diffId)
                && !isNullOrEmpty(harbormasterPHID) && !isNullOrEmpty(revisionId)) {
            return true;
        }

        return false;
    }

    public URL getPhabricatorURL() {
        return phabricatorUrl;
    }

    public String getConduitToken() {
        return conduitToken;
    }

    public String getHarbormasterPHID() {
        return harbormasterPHID;
    }

    private URL parsePhabricatorURL(String input) throws MalformedURLException {
        URL inputURL = new URL(input);
        return inputURL;
    }

}
