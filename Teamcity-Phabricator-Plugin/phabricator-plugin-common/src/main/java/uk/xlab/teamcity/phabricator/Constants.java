package uk.xlab.teamcity.phabricator;

public class Constants {

    public static final String PLUGIN_NAME = "phabricator";
    public static final String PLUGIN_DISPLAY_NAME = "Phabricator Plugin";

    // Build Feature Settings
    public static final String BUILD_FEATURE_TYPE = "phabricator-build-feature";
    public static final String PHABRICATOR_URL_SETTING = "plugin.phabricatorUrl";
    public static final String PHABRICATOR_CONDUIT_TOKEN_SETTING = "plugin.conduitToken";

    // Build Config
    public static final String BUILD_ID = "phabricator.BUILD_ID";
    public static final String DIFF_ID = "phabricator.DIFF_ID";
    public static final String HARBORMASTER_PHID = "phabricator.HARBORMASTER_TARGET_PHID";
    public static final String REVISION_ID = "phabricator.REVISION_ID";
}
