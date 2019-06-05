<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<c:set var="phabricatorUrl" value="${propertiesBean.properties['plugin.phabricatorUrl']}" />
<c:set var="conduitToken" value="${propertiesBean.properties['plugin.conduitToken']}" />
<c:set var="pathToArc" value="${propertiesBean.properties['plugin.pathToArc']}" />

<tr><td colspan="2">Report build status in real-time to your Phabricator instance.</td></tr>
<tr><th>Phabricator URL:</th><td><props:textProperty name="plugin.phabricatorUrl"/></td></tr>
<tr><th>Conduit Token:</th><td><props:passwordProperty name="plugin.conduitToken" size="54"/></td></tr>
<tr><th>Path To Arcanist:</th><td><props:textProperty name="plugin.pathToArc"/></td></tr>
