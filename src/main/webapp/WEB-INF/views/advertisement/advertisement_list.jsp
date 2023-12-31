<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- fmt:setLocale konfiguruje ustawienia lokalne dla tej strony na polskie; używane przez fmt:formatNumber przy formatowaniu liczby--%>
<fmt:setLocale value = "pl_PL"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ogłoszenia</title>
</head>
<body>
<form action="<c:url value='/logout'/>" method="post">
    <input type="submit" value="logout">
</form>
<table>
    <thead>
    <tr>
        <th>Tytuł</th>
        <th>Opis</th>
        <th>Kategoria</th>
        <c:if test="${isAdmin == true}">
            <th>status</th>
        </c:if>
        <th>Zamieszczone przez</th>
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
            <td>${fn:escapeXml(advertisement.category.name)}</td>
            <c:if test="${isAdmin == true}">
                <td>${fn:escapeXml(advertisement.status)}</td>
            </c:if>
            <td>${fn:escapeXml(advertisement(advertisement.user.login))}</td>
            <td>
                    <%-- c:url dodaje do url nazwę aplikacji (context root) oraz identifykator sesji jsessionid jeśli sesja jest włączona i brak obsługi ciasteczek --%>
                <c:if test="${id == advertisement.user.getId() || isAdmin == true}">
                <a href="<c:url value='/advertisement/edit/${advertisement.id}'/>">Edytuj</a>,
                <a href="<c:url value='/advertisement/remove/${advertisement.id}'/>">Usuń</a>
                </c:if>
                <c:if test="${isAdmin == true && advertisement.status == false}">
                    ,<a href="<c:url value='/advertisement/accept/${advertisement.id}'/>">Akceptuj ogłoszenie</a>

                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<a href="<c:url value='/advertisement/edit'/>">Dodaj Ogloszenie</a>--%>
<c:if test="${isAdmin == true}">
    <a href="<c:url value='/category/list'/>">Zarządzaj kategoriami</a>
</c:if>
<a href="<c:url value='/advertisement/myadverts'/>">Moje Ogłoszenia</a>
</body>
</html>
