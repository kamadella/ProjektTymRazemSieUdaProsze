
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Zaloguj się</h2>

<form action="login" method="post">
    <label for="username">Login:</label><br>
    <input type="text" id="username" name="username"><br>
    <label for="password">Hasło:</label><br>
    <input type="password" id="password" name="password"><br><br>
    <input type="submit" value="Zaloguj się">
</form>

<c:if test="${param.error}">
    Invalid Login/Hasło.
</c:if>

<form action="<c:url value='/registration/save'/>" method="post">
    <input type="submit" value="Zarejestruj sie">
</form>

</body>
</html>