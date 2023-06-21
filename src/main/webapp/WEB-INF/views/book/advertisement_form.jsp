<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 21.06.2023
  Time: 09:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Ogloszenia</title>
  <style>.error { color: red; }</style>
</head>
<body>
<h3>Edycja ogloszenia</h3>
<%-- komentarz JSP - nie jest rendrowany --%>
<form method="post">
  <table>
    <tr>
      <td>Tytuł:</td>
      <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zwiera znaczniki - zabezpiecza przez atakiem XSS --%>
      <td><input type="text" name="title" value="${fn:escapeXml(title)}" ></td>
      <%-- errors zawiera ew. błędy konwersji/walidacji dla poszczególnych pól --%>
      <td><span class="error">${errors.title}</span></td>
    </tr>
    <tr>
      <td>Opis:</td>
      <td><input type="text" name="descriprion" value="${fn:escapeXml(descriprion)}" ></td>
      <td><span class="error">${errors.descriprion}</span></td>
    </tr>
    <tr>
      <td>Id kategorii:</td>
      <td><input type="number" name="id_category" value="${fn:escapeXml(id_category)}" > </td>
      <td><span class="error">${errors.id_category}</span></td>
    </tr>

  </table>
  <input type="submit" value="Save">
</form>
</body>
</html>
