<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base>
  <jsp:attribute name="title">
    Verify
  </jsp:attribute>
  <jsp:attribute name="header">
    <h1>Verify</h1>
  </jsp:attribute>
  <jsp:body>
    <form accept-charset="UTF-8" method="post">
      <div class="row">
        <div class="col-25">
          <label>Enter your code:</label></div>
        <div class="col-75">
          <input name="code" id="code" type="number"/>
        </div>
      </div>
      <div class="row">
        <button name="button" type="submit">Verify</button>
      </div>
    </form>
  </jsp:body>
</t:base>