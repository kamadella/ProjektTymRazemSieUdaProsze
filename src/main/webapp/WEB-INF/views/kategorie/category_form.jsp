<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Kategorie</title>
  <style>.error { color: red; }</style>
</head>
<body>
<h3>Edycja Kategorii</h3>
<%-- komentarz JSP - nie jest rendrowany --%>
<form method="post">
  <table>
    <tr>
      <td>Tytuł:</td>
      <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zwiera znaczniki - zabezpiecza przez atakiem XSS --%>
      <td><input type="text" name="name" value="${fn:escapeXml(name)}" ></td>
      <%-- errors zawiera ew. błędy konwersji/walidacji dla poszczególnych pól --%>
      <td><span class="error">${errors.name}</span></td>
    </tr>
  </table>
  <input type="submit" value="Save">
</form>
<br>
<a href="<c:url value='/category/list'/>">Lista Kategorii</a>
</body>
</html>

