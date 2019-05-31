package uk.xlab.teamcity.phabricator;

import java.util.Map;

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
            if (param != null) {
                logger.info(String.format("Found %s", param));
            }
        }
    }
}
