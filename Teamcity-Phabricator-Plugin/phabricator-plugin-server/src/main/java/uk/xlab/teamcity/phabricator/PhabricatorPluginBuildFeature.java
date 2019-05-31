package uk.xlab.teamcity.phabricator;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.web.openapi.PluginDescriptor;

public class PhabricatorPluginBuildFeature extends BuildFeature {

    private final String myEditUrl;
    private PhabricatorServerLogger logger;

    public PhabricatorPluginBuildFeature(@NotNull final PluginDescriptor pluginDescriptor,
            @NotNull final PhabricatorServerLogger phabLogger) {
        myEditUrl = pluginDescriptor.getPluginResourcesPath("phabricatorBuildFeature.jsp");
        logger = phabLogger;

        logger.info("Build feature registered");
    }

    @Override
    public String getType() {
        return Constants.BUILD_FEATURE_TYPE;
    }

    @Override
    public String getDisplayName() {
        return Constants.PLUGIN_DISPLAY_NAME;
    }

    @Override
    public String getEditParametersUrl() {
        return myEditUrl;
    }

    @Override
    public boolean isMultipleFeaturesPerBuildTypeAllowed() {
        return false;
    }

    /**
     * Populates the Parameters Description on the build features page
     */
    @NotNull
    @Override
    public String describeParameters(@NotNull final Map<String, String> params) {
        String url = "";

        for (String key : params.keySet()) {
            if (key.equals(Constants.PHABRICATOR_URL_SETTING)) {
                url = params.get(key);
            }
        }

        return String.format("Phabricator URL: %s", url);
    }
}
