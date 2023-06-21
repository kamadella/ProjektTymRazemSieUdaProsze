<%--
  Created by IntelliJ IDEA.
  User: kamil
  Date: 21.06.2023
  Time: 09:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ogłoszenia</title>
</head>
<body>
<table>
    <thead>
    <tr>
        <th>Tytuł</th>
        <th>Opis</th>
        <th>Id kategorii</th>
        <th>status</th>
        <th>Operacje</th>
    </tr>
    </thead>
    <tbody>
    <%-- c:forEach renderuje zawartość dla każdego elementu na liscie, wewnatrz znacznika kolejne elementy będą dostępne pod nazwą wskazaną przez atrybut var --%>
    <c:forEach items='${advertisementList}' var='advertisement'>
        <tr>
                <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zwiera znaczniki - zabezpiecza przez atakiem XSS --%>
            <td>${fn:escapeXml(advertisement.title)}</td>
            <td>${fn:escapeXml(advertisement.description)}</td>
            <td>${fn:escapeXml(advertisement.id_category)}</td>
                    <td>${fn:escapeXml(advertisement.status)}</td>
                    <td>
                    <%-- c:url dodaje do url nazwę aplikacji (context root) oraz identifykator sesji jsessionid jeśli sesja jest włączona i brak obsługi ciasteczek --%>
                <a href="<c:url value='/advertisement/edit/${advertisement.id}'/>">Edytuj</a>,
                <a href="<c:url value='/advertisement/remove/${advertisement.id}'/>">Usuń</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<a href="<c:url value='/advertisement/edit'/>">Dodaj Ogloszenie</a>
</body>
</html>
