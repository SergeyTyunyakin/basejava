<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>

<c:set var="sectionType" value="${param.sectionType}"/>
<jsp:useBean id="sectionType" type="java.lang.String"/>

<c:if test="${resume.getSection(SectionType.valueOf(sectionType)) != null}">
    <c:set var="orgSection" value="${resume.getSection(SectionType.valueOf(sectionType))}"/>
    <jsp:useBean id="orgSection" type="com.urise.webapp.model.OrganizationSection"/>
    <div class="section_title">
        <%=SectionType.valueOf(sectionType).getTitle()%>
    </div>
    <c:forEach items="${orgSection.getSortedItems()}" var="item">
        <div class="section-wrapper">
            <jsp:useBean id="item" type="com.urise.webapp.model.Organization"/>
            <div class="job-name"><a class="contact-link"
                    <%=(item.getLink().getUrl() == null || item.getLink().getUrl().equals("")) ? "" : "href=\"" + item.getLink().getUrl() + '"'%>
                                     target="_blank"><%=item.getLink().getName()%>
            </a>
            </div>
            <c:forEach items="${item.getPeriods()}" var="period">
                <jsp:useBean id="period" type="com.urise.webapp.model.Organization.Period"/>

                <div class="period-position">
                    <div class="period"><%=DateUtil.toDisplayDateFrom(period.getDateFrom())%>
                        - <%=DateUtil.toDisplayDateTo(period.getDateTo())%>
                    </div>
                    <div class="period-title"><%=(period.getTitle() == null) ? "" : period.getTitle()%>
                    </div>
                </div>
                <div class="description"><%=(period.getDescription() == null) ? "" : period.getDescription()%>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</c:if>