package uk.xlab.teamcity.phabricator;

public class Constants {

    public static final String PLUGIN_NAME = "phabricator";
    public static final String PLUGIN_DISPLAY_NAME = "Phabricator Plugin";

    // Build Feature
    public static final String BUILD_FEATURE_TYPE = "phabricator-build-feature";
    public static final String PHABRICATOR_URL_SETTING = "phabricator_url_setting";

    // Build Config
    public static final String BRANCH_NAME = "env.PHAB_BRANCH_NAME";
    public static final String BUILD_ID = "env.PHAB_BUILD_ID";
    public static final String DIFF_ID = "env.PHAB_DIFF_ID";
    public static final String HARBORMASTER_PHID = "env.PHAB_HARBORMASTER_TARGET_PHID";
    public static final String REVISION_ID = "env.PHAB_REVISION_ID";
}
