<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 22.06.2023
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Rejestracja</title>
</head>
<body>
<h2>Zarejestruj sie tutaj</h2>

<form action="login" method="post">
  <label for="mail">Adres e-mail:</label><br>
  <input type="text" id="mail" name="mail"><br>
  <label for="username">Login:</label><br>
  <input type="text" id="username" name="username"><br>
  <label for="password">Hasło:</label><br>
  <input type="password" id="password" name="password"><br><br>
  <input type="submit" value="Zarejestruj się">
</form>

<c:if test="${param.error}">
  Zły e-mail/Login/Haslo.
</c:if>

</body>
</html>
