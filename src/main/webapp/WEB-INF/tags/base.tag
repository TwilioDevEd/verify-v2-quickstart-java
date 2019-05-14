<%@tag import="com.twilio.verify_quickstart.models.User" %>
<%@tag description="Page Layout Tag" pageEncoding="UTF-8"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>
        <jsp:invoke fragment="title"/> - Twilio Verify
    </title>
    <meta content="width=device-width, initial-scale=1" name="viewport" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/intlTelInput.css"/>
</head>
<body>
<nav>
    <h1>Twilio Verify</h1>
    <ul>
        <% User user = (User) session.getAttribute("user"); %>
        <core:choose>
            <core:when test="${user != null}">
                <li>
                    <span>Welcome, ${user.username}</span>
                </li>
                <li><a href="${pageContext.request.contextPath}/logout">Log Out</a></li>
            </core:when>
            <core:otherwise>
                <li><a href="${pageContext.request.contextPath}/register" id="registerLink">Register</a></li>
                <li><a href="${pageContext.request.contextPath}/login" id="loginLink">Log In</a></li>
            </core:otherwise>
        </core:choose>
    </ul>
</nav>
<section class="content">
    <header>
        <jsp:invoke fragment="header"/>
    </header>
    <core:if test="${message != null}">
    <div class="flash">
        ${message}
    </div>
    </core:if>
    <div>
        <jsp:doBody/>
    </div>
</section>
</body>
</html>