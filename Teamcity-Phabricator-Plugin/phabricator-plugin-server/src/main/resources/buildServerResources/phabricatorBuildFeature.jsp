<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<c:set var="phabricatorUrl" value="${propertiesBean.properties['phabricator_url_setting']}" />

<tr><td colspan="2">Report build status in real-time to your Phabricator instance.</td></tr>
<tr><th>Phabricator URL:</th><td><props:textProperty name="phabricator_url_setting"/></td></tr>
