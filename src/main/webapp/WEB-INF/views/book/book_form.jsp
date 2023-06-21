<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Książki</title>
        <style>.error { color: red; }</style>
    </head>
    <body>
        <h3>Edycja książki</h3>
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
                    <td>Autor:</td>
                    <td><input type="text" name="author" value="${fn:escapeXml(author)}" ></td>
                    <td><span class="error">${errors.author}</span></td>
                </tr>
                <tr>
                    <td>Cena:</td>
                    <td><input type="text" name="price" value="${fn:escapeXml(price)}" > </td>
                    <td><span class="error">${errors.price}</span></td>
                </tr>
            </table>
            <input type="submit" value="Save"> 
        </form>
    </body>
</html>

