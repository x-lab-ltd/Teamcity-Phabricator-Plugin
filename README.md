# Splint
_X-Lab's Linting Spine_

## Things
This repository holds the plugins created to link together our internal systems. The original premise to link Phabricator, TeamCity and SonarQube to enable linting on differential reviews.

Below are the plugins:
* Harbormaster-Teamcity-Plugin
* Teamcity-Phabricator-Plugin
* SonarQube-Phabricator-Plugin

### Harbomaster-Teamcity-Plugin

The harbormaster plugin allows us to trigger a build configuration within TeamCity as part of a harbormaster build plan.

The plugin requires:
1. TeamCity URI
2. Build Configuration to trigger a build for
3. TeamCity access token to authenticate with the server

To deploy simply drag the contents of the folder to `src/extensions/` on the Phabricator instance and then restart the application.

## Useful Links

* https://confluence.jetbrains.com/display/TCD10/Web+UI+Extensions
* http://javadoc.jetbrains.net/teamcity/openapi/current/jetbrains/buildServer/serverSide/BuildServerAdapter.html
* https://github.com/JetBrains/teamcity-sdk-maven-plugin/wiki/Developing-TeamCity-plugin
* https://www.quali.com/blog/teamcity-plugin-development-journey/
* https://plugins.jetbrains.com/docs/teamcity/plugin-development-faq.html
* https://plugins.jetbrains.com/docs/teamcity/getting-started-with-plugin-development.html