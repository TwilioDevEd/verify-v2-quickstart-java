<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>

<t:base>
  <jsp:attribute name="title">
    Users
  </jsp:attribute>
  <jsp:attribute name="header">
    <h1>Users</h1>
  </jsp:attribute>
  <jsp:body>
    <table>
      <thead>
        <tr>
          <th>Username</th>
          <th>Phone Number</th>
          <th>Verified</th>
        </tr>
      </thead>
      <tbody>
        <core:forEach items="${users}" var="user">
          <tr>
            <td><core:out value="${user.username}"/></td>
            <td><core:out value="${user.phoneNumber}"/></td>
            <td><core:out value="${user.verified}"/></td>
          </tr>
        </core:forEach>
      </tbody>
    </table>
  </jsp:body>
</t:base>
