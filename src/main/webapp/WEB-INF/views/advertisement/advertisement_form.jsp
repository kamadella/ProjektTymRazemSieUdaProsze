<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
