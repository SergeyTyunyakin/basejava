<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>

<c:set var="sectionType" value="${param.sectionType}"/>
<jsp:useBean id="sectionType" type="java.lang.String"/>

<c:if test="${resume.getSection(SectionType.valueOf(sectionType)) != null}">
    <c:set var="textSection" value="${resume.getSection(SectionType.valueOf(sectionType))}"/>
    <jsp:useBean id="textSection" type="com.urise.webapp.model.ListTextSection"/>
    <div class="section_title">
        <%=SectionType.valueOf(sectionType).getTitle()%>
    </div>
    <div class="position">
        <c:forEach items="${textSection.items}" var="item">
            <jsp:useBean id="item" type="java.lang.String"/>
            <li><%=item%>
            </li>
        </c:forEach>
    </div>
</c:if>