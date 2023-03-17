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
    <div class="full-name">${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img
            src="img/pencil.png"></a></div>
    <div class="section_title">
        Контакты
    </div>
    <div class="contacts">
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <div><%=contactEntry.getKey().toHtml(contactEntry.getValue())%>
            </div>
        </c:forEach>
    </div>
    <jsp:include page="fragments/view_textsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.OBJECTIVE.name()%>"/>
    </jsp:include>
    <jsp:include page="fragments/view_textsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.PERSONAL.name()%>"/>
    </jsp:include>
    <jsp:include page="fragments/view_listtextsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.ACHIEVEMENT.name()%>"/>
    </jsp:include>
    <jsp:include page="fragments/view_listtextsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.QUALIFICATIONS.name()%>"/>
    </jsp:include>
    <jsp:include page="fragments/view_organizationsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.EXPERIENCE.name()%>"/>
    </jsp:include>
    <jsp:include page="fragments/view_organizationsection.jsp">
        <jsp:param name="sectionType" value="<%=SectionType.EDUCATION.name()%>"/>
    </jsp:include>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>