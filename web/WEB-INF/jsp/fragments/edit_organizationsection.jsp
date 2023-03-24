<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>

<c:set var="sectionType" value="${param.sectionType}"/>
<jsp:useBean id="sectionType" type="java.lang.String"/>
<c:set var="textValue" value=""/>

<%int orgId = 0;%>
<%int perId = 0;%>

<div class="section_edit_title"><%=SectionType.valueOf(sectionType).getTitle()%>
    <br>
    <button type="button" onclick="addOrgSection('${sectionType}')"
            class="blue-button">Добавить
        организацию
    </button>
</div>

<div id="orgTemplate" style="display: none">
    <input type="hidden" name="sectionType" value="<%=sectionType%>">
    <input type="hidden" name="sectionId" value="sectionId">
    <input type="text" name="orgName" size=60 value="" placeholder="Наименование организации">
    <div class="orgurlwithbutton" id="orgLink">
        <div class="orgurl">
            <input type="text" name="orgUrl" size=120 value="" placeholder="Ссылка на сайт организации">
        </div>
        <div id="delOrgDiv" class="">
            <button type="button" name="delOrg" onclick="delDiv('orgTemplate')" class="red-button">Удалить организацию
            </button>
        </div>
    </div>
    <button type="button" name="addPer" onclick="addPeriod('sectionId','sectionType')" class="blue-button">Добавить
        период
    </button>
    <div id="insertPeriodLine">
        <div id="periodTemplate">
            <input type="hidden" name="parentSectionId" value="sectionId">
            <input type="hidden" name="parentSectionType" value="<%=sectionType%>">
            <div class="period-section" id="periodLineTemplate">
                <input class="inputperiod" type="text" name="periodFrom" size="10" pattern="[0-9]{2}/[0-9]{4}"
                       placeholder="Начало, ММ/ГГГГ"
                       title="Формат заполнения: &laquo;ММ/ГГГГ&raquo;"
                       value=""/>
                <input class="inputperiod" type="text" name="periodTo" size="10" pattern="[0-9]{2}/[0-9]{4}|сейчас"
                       placeholder="Окончание, ММ/ГГГГ"
                       title="Формат заполнения: &laquo;ММ/ГГГГ&raquo; или оставить пустым для указания &laquo;сейчас&raquo;"
                       value=""/>
                <button type="button" name="delPeriod" onclick="delDiv('periodId')" class="red-button">Удалить период
                </button>
                <br/>
                <input type="text" name="periodTitle" size=60 value="" placeholder="Заголовок">
                <textarea name="periodDescription" rows=3 cols="60" placeholder="Описание"></textarea>

            </div>
        </div>
    </div>
    <div class="spacer"></div>
</div>
<div id="insert<%=sectionType%>">
    <c:choose>
        <c:when test="${resume.getSection(SectionType.valueOf(sectionType)) != null}">
            <c:set var="orgSection" value="${resume.getSection(SectionType.valueOf(sectionType))}"/>
            <jsp:useBean id="orgSection" type="com.urise.webapp.model.OrganizationSection"/>

            <c:forEach items="${orgSection.getSortedItems()}" var="item">
                <jsp:useBean id="item" type="com.urise.webapp.model.Organization"/>
                <% orgId++; %>
                <%String sectionId = sectionType + orgId;%>
                <div id="<%=sectionType+orgId%>">
                    <input type="hidden" name="sectionType" value="<%=sectionType%>">
                    <input type="hidden" name="sectionId" value="<%=sectionId%>">
                    <input type="text" name="orgName" size=60 value="<%=item.getLink().getName()%>"
                           placeholder="Наименование организации">
                    <div class="orgurlwithbutton">
                        <div class="orgurl">
                            <input type="text" name="orgUrl" size=120
                                   value="<%=(item.getLink().getUrl() == null) ? "" : item.getLink().getUrl()%>"
                                   placeholder="Ссылка на сайт организации">
                        </div>
                        <div>
                            <button type="button" name="delOrg" onclick="delDiv('<%=sectionId%>')"
                                    class="red-button">
                                Удалить организацию
                            </button>
                        </div>
                    </div>
                    <button type="button" name="addPer" onclick="addPeriod('<%=sectionId%>', '<%=sectionType%>')"
                            class="blue-button">Добавить
                        период
                    </button>
                    <div id="insertPeriod<%=sectionId%>">
                        <% perId++; %>
                        <%String periodId = sectionId + '_' + perId;%>
                        <div id="<%=periodId%>">
                            <c:forEach items="${item.getPeriods()}" var="period">
                                <jsp:useBean id="period" type="com.urise.webapp.model.Organization.Period"/>
                                <input type="hidden" name="parentSectionId" value="<%=sectionId%>">
                                <input type="hidden" name="parentSectionType" value="<%=sectionType%>">
                                <div class="period-section" id="period<%=periodId%>">
                                    <input class="inputperiod" type="text" name="periodFrom" size="10"
                                           pattern="[0-9]{2}/[0-9]{4}"
                                           placeholder="Начало, ММ/ГГГГ"
                                           title="Формат заполнения: &laquo;ММ/ГГГГ&raquo;"
                                           value="<%=DateUtil.toEditDate(period.getDateFrom())%>" required/>
                                    <input class="inputperiod" type="text" name="periodTo" size="10"
                                           pattern="[0-9]{2}/[0-9]{4}|сейчас"
                                           placeholder="Окончание, ММ/ГГГГ"
                                           title="Формат заполнения: &laquo;ММ/ГГГГ&raquo; или оставить пустым для указания &laquo;сейчас&raquo;"
                                           value="<%=DateUtil.toEditDate(period.getDateTo())%>"/>
                                    <button type="button" name="delPeriod" onclick="delDiv('<%=periodId%>')"
                                            class="red-button">Удалить период
                                    </button>
                                    <br/>
                                    <input type="text" name="periodTitle" size=60
                                           value="<%=(period.getTitle() == null) ? "" : period.getTitle()%>"
                                           placeholder="Заголовок">
                                    <textarea name="periodDescription" rows=3 cols="60"
                                              placeholder="Описание"><%=(period.getDescription() == null) ? "" : period.getDescription()%></textarea>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="spacer"></div>
                </div>
            </c:forEach>
        </c:when>
    </c:choose>
</div>




