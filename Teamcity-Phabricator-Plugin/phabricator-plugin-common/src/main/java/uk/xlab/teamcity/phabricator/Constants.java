package uk.xlab.teamcity.phabricator;

/**
 * A set of constants that are used throughout the server and agent plugins
 *
 */
public class Constants {

    public static final String PLUGIN_NAME = "phabricator";
    public static final String PLUGIN_DISPLAY_NAME = "Phabricator Plugin";
    public static final String LOGGING_PREFIX_TEMPLATE = "Phabricator Plugin - %s";

    // Build Feature Settings
    public static final String BUILD_FEATURE_TYPE = "phabricator-build-feature";
    public static final String PHABRICATOR_URL_SETTING = "plugin.phabricatorUrl";
    public static final String PHABRICATOR_CONDUIT_TOKEN_SETTING = "plugin.conduitToken";
    public static final String PHABRICATOR_ARCANIST_PATH_SETTING = "plugin.pathToArc";

    // Build Config
    public static final String BUILD_ID = "phabricator.BUILD_ID";
    public static final String DIFF_ID = "phabricator.DIFF_ID";
    public static final String HARBORMASTER_PHID = "phabricator.HARBORMASTER_TARGET_PHID";
    public static final String REVISION_ID = "phabricator.REVISION_ID";
}
