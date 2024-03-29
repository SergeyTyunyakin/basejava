<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>

<c:set var="sectionType" value="${param.sectionType}"/>
<jsp:useBean id="sectionType" type="java.lang.String"/>

<c:set var="textValue" value=""/>
<c:if test="${resume.getSection(SectionType.valueOf(sectionType)) != null}">
    <c:set var="textSection" value="${resume.getSection(SectionType.valueOf(sectionType))}"/>
    <jsp:useBean id="textSection" type="com.urise.webapp.model.ListTextSection"/>
    <c:set var="textValue" value="<%=textSection.toString()%>"/>
</c:if>
<div class="section_edit_title"><%=SectionType.valueOf(sectionType).getTitle()%>
</div>
<textarea name="${SectionType.valueOf(sectionType)}" rows=3
          cols="60">${textValue}</textarea>
