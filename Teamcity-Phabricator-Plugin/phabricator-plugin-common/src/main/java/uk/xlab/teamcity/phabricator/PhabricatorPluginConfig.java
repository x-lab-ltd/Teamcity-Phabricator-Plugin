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

    private IPhabricatorPluginLogger logger;
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
    public void setLogger(PhabricatorServerLogger logger) {
        this.logger = logger;
    }

    /**
     * Take a copy of all build parameters which will then be parsed
     * 
     * @param parameters
     */
    public void setParameters(Map<String, String> parameters) {
        params = parameters;

        logger.info(String.format("Looking for parameters"));
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
                case Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING:
                    logger.info("Found Phabricator Conduit Token");
                    conduitToken = params.get(Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING);
                    
                case Constants.BRANCH_NAME:
                    logger.info(String.format("Found branch name: %s", params.get(Constants.BRANCH_NAME)));
                    branchName = params.get(Constants.BRANCH_NAME);
                case Constants.BUILD_ID:
                    logger.info(String.format("Found build id: %s", params.get(Constants.BUILD_ID)));
                    buildId = params.get(Constants.BUILD_ID);
                case Constants.DIFF_ID:
                    logger.info(String.format("Found diff ID: %s", params.get(Constants.DIFF_ID)));
                    diffId = params.get(Constants.DIFF_ID);
                case Constants.HARBORMASTER_PHID:
                    logger.info(String.format("Found harbormaster target PHID: %s",
                            params.get(Constants.HARBORMASTER_PHID)));
                    harbormasterPHID = params.get(Constants.HARBORMASTER_PHID);
                case Constants.REVISION_ID:
                    logger.info(String.format("Found revision ID: %s", params.get(Constants.REVISION_ID)));
                    revisionId = params.get(Constants.REVISION_ID);
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
        String scheme = inputURL.getProtocol();
        if (scheme == null)
            scheme = "http";
        int port = inputURL.getPort();
        if (port == -1) {
            if (scheme == "https")
                port = 443;
            else
                port = 80;
        }
        return inputURL;
    }

}
