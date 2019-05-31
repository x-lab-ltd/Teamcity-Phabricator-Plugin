package uk.xlab.teamcity.phabricator;

import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.util.EventDispatcher;

/**
 * Listen for builds been started and track their progress with BuildTracker
 * 
 * @author steven.cooney
 *
 */
public class PhabricatorBuildServerAdapter extends BuildServerAdapter {

    private PhabricatorServerLogger logger;

    public PhabricatorBuildServerAdapter(@NotNull final EventDispatcher<BuildServerListener> buildServerListener,
            @NotNull final PhabricatorServerLogger phabLogger) {
        buildServerListener.addListener(this);
        logger = phabLogger;

        logger.info("Build server adapter registered");
    }

    @Override
    public void buildStarted(@NotNull SRunningBuild runningBuild) {
        super.buildStarted(runningBuild);

        // Do the work in a separate thread to avoid blocking
        // other builds monitored by this adapter (this might be
        // a tad overkill)
        new Thread(new BuildTracker(runningBuild, logger)).start();
    }
}
