<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base>
  <jsp:attribute name="title">
    Log In
  </jsp:attribute>
  <jsp:attribute name="header">
    <h1>Log In</h1>
  </jsp:attribute>
  <jsp:body>
    <form method="post">
      <label for="username">Username</label>
      <input name="username" id="username" required>
      <label for="password">Password</label>
      <input type="password" name="password" id="password" required>
      <input type="submit" value="Log In">
    </form>
  </jsp:body>
</t:base>