<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="addNew" value="${addNew}">
        <div class="section_edit_title">ФИО</div>
        <input class="field" type="text" name="fullName" size="60" placeholder="ФИО" value="${resume.fullName}" required
               pattern=".*\S+.*"
               title="ФИО не может состоять из пробелов">
        <div class="section_edit_title">Контакты</div>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <input type="text" name="${type.name()}" size=60 value="${resume.getContact(type)}"
                   placeholder="${type.title}">
        </c:forEach>
        <jsp:include page="fragments/edit_textsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.OBJECTIVE.name()%>"/>
        </jsp:include>
        <jsp:include page="fragments/edit_textsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.PERSONAL.name()%>"/>
        </jsp:include>
        <jsp:include page="fragments/edit_listtextsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.ACHIEVEMENT.name()%>"/>
        </jsp:include>
        <jsp:include page="fragments/edit_listtextsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.QUALIFICATIONS.name()%>"/>
        </jsp:include>
        <jsp:include page="fragments/edit_organizationsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.EXPERIENCE.name()%>"/>
        </jsp:include>
        <jsp:include page="fragments/edit_organizationsection.jsp">
            <jsp:param name="sectionType" value="<%=SectionType.EDUCATION.name()%>"/>
        </jsp:include>
        <div class="button-section">
            <button class="green-submit-button" type="submit">Сохранить</button>
            <button class="red-cancel-button" type="reset" onclick="window.history.back()">Отменить</button>
        </div>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>