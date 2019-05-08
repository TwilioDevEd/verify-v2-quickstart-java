<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base>
  <jsp:attribute name="title">
    Register
  </jsp:attribute>
  <jsp:attribute name="header">
    <h1>Register</h1>
  </jsp:attribute>
  <jsp:body>
    <form method="post">
      <label for="username">Username</label>
      <input name="username" id="username" required />
      <label for="password">Password</label>
      <input type="password" name="password" id="password" required />
      <label for="phone">Phone number</label>
      <input type="tel" name="phone" id="phone" required />
      <label>Verification method:</label>
      <label><input type="radio" name="channel" value="sms" checked />SMS</label>
      <label><input type="radio" name="channel" value="call" />Call</label>
      <button name="form-submit" type="submit">Sign Up</button>
    </form>
    <script src="${pageContext.request.contextPath}/js/intlTelInput.js"></script>
    <script>
      var input = document.querySelector("#phone");
      window.intlTelInput(input, {
        hiddenInput: 'full_phone',
        preferredCountries: ["us", "gb", "co", "de"],
        utilsScript: '${pageContext.request.contextPath}/js/utils.js'
      });
    </script>
  </jsp:body>
</t:base>