<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" href="favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <title>Список всех резюме</title>
</head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <a href="resume?action=add">
        <table class="ar">
            <tr>
                <td><img src="img/add_resume.png"></td>
                <td>Добавить резюме</td>
            </tr>
        </table>
    </a>
    <br>
    <table class="table">
        <thead>
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th colspan=2 style="text-align: center; vertical-align: middle;">Управление
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td><%=ContactType.EMAIL.toHtml(resume.getContact(ContactType.EMAIL))%>
                </td>
                <td style="text-align: center; vertical-align: middle;"><a
                        href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"
                                                                             alt="Удалить"
                                                                             title="Удалить"></a></td>
                <td style="text-align: center; vertical-align: middle;"><a
                        href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                           alt="Редактировать"
                                                                           title="Редактировать"></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>