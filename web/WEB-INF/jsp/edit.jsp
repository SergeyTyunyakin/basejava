<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" href="favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
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
<script>
    function addOrgSection(section) {
        let newSection = document.getElementById('orgTemplate').cloneNode(true);
        let insertSection = document.getElementById('insert' + section);
        newSection.style.display = '';
        let divId = Date.now();
        newSection.setAttribute('id', divId);
        newSection.setAttribute('name', divId);

        for (let child of newSection.children) {
            if (child.getAttribute('id') === 'orgLink') {
                for (let childChild of child.children) {
                    if (childChild.getAttribute('id') === 'delOrgDiv') {
                        for (let ccChild of childChild.children) {
                            if (ccChild.getAttribute('name') === 'delOrg') {
                                ccChild.setAttribute('onclick', 'delDiv(' + divId + ')');
                            }
                        }
                        childChild.removeAttribute('id');
                    }
                    if (childChild.getAttribute('name') === 'PeriodTemplate') {
                        childChild.remove();
                    }
                }
                child.removeAttribute('id');
            }
            if (child.getAttribute('name') === 'addPer') {
                child.setAttribute('onclick', 'addPeriod(' + divId + ',"' + section + '")');
            }
            if (child.getAttribute('name') === 'sectionId') {
                child.setAttribute('value', divId);
            }
            if (child.getAttribute('name') === 'sectionType') {
                child.setAttribute('value', section);
            }
            if (child.getAttribute('id') === 'insertPeriodLine') {
                child.setAttribute('id', 'insertPeriod' + divId);
                for (let childChild of child.children) {
                    if (childChild.getAttribute('id') === 'periodTemplate') {
                        childChild.remove();
                    }
                }
            }
        }
        insertSection.insertBefore(newSection, insertSection.firstChild);
        addPeriod(divId, section);
    }

    function addPeriod(sectionId, sectionType) {
        let newPeriod = document.getElementById('periodTemplate').cloneNode(true);
        let insertPeriod = document.getElementById('insertPeriod' + sectionId);

        let divId = 'period' + Date.now();
        newPeriod.setAttribute('id', divId);

        for (let child of newPeriod.children) {
            if (child.getAttribute('id') === 'periodLineTemplate') {
                for (let childChild of child.children) {
                    if (childChild.getAttribute('name') === 'delPeriod') {
                        childChild.setAttribute('onclick', 'delDiv(' + '"' + divId + '")');
                    }
                    child.setAttribute('id', divId);
                    if (childChild.getAttribute('name') === 'periodFrom') {
                        childChild.setAttribute('required', '');
                    }
                }
            }
            if (child.getAttribute('name') === 'parentSectionId') {
                child.setAttribute('value', sectionId);
            }
            if (child.getAttribute('name') === 'parentSectionType') {
                child.setAttribute('value', sectionType);
            }

        }

        insertPeriod.insertBefore(newPeriod, insertPeriod.firstChild);
    }

    function delDiv(divId) {
        document.getElementById(divId).remove();
    }
</script>
</body>
</html>