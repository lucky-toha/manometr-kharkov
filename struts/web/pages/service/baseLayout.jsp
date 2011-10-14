<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
    <title><tiles:getAsString name="title" ignore="true"/></title>

    <tiles:useAttribute id="list" name="cssItems" classname="java.util.List"/>
    <c:forEach var="item" items="${list}">
        <link rel="stylesheet" type="text/css" href="<c:url value="${item}"/>"/>
    </c:forEach>

    <%--<tiles:useAttribute id="jsList" name="jsItems" classname="java.util.List"/>--%>
    <%--<c:forEach var="jsItem" items="${jsList}">--%>
        <%--<script type="text/javascript" src="<c:url value="${jsItem}"/>"/>--%>
    <%--</c:forEach>--%>

    <%--<link type="text/css" rel="stylesheet"--%>
    <%--href="<tiles:getAsString name="IDENTIFIER_HERE"/>"/>--%>

    <%--<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>"/>--%>

    <%--<link rel="stylesheet" type="text/css" href="<c:url value="/css/menu.css"/>"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="<c:url value="/css/smartTable.css"/>"/>--%>
</head>
<body>

<div id="mainDiv">
    <table border="1" class="rootTable" align="center">
        <tr class="header">
            <td colspan="2">
                <tiles:insertAttribute name="header" ignore="true"/>
            </td>
        </tr>
        <tr class="body">
            <td id="menu" valign="top">
                <tiles:insertAttribute name="menu"/>
            </td>
            <td valign="top" id="contentTD">
                <tiles:insertAttribute name="body"/>
            </td>
        </tr>
        <tr class="footer">
            <td colspan="2">
                <tiles:insertAttribute name="footer"/>
            </td>
        </tr>
    </table>
</div>

</body>
</html>
