package com.urise.webapp.web;

import com.urise.webapp.model.*;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.util.Config;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;
    private static ServletContext context;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        context = getServletContext();
        storage = Config.get().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        boolean addNew = Boolean.parseBoolean(request.getParameter("addNew"));

        fullName = fullName.strip();
        if (fullName.isEmpty()) {
            fullName = "Имя не указано";
        }
        Resume r;
        if (!addNew) {
            r = storage.get(uuid);
            r.setFullName(fullName);
        } else {
            r = new Resume(uuid, fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.strip().length() != 0) {
                r.addContact(type, value.strip());
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType sectionType : SectionType.values()) {
            String value = request.getParameter(sectionType.name());
            if (value == null) {
                continue;
            }
            if (value.strip().length() == 0) {
                r.getSections().remove(sectionType);
                continue;
            }
            value = value.strip();
            switch (sectionType) {
                case OBJECTIVE, PERSONAL -> r.addSection(sectionType, new TextSection(value));
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    r.addSection(sectionType, new ListTextSection(List.of(Arrays.stream(value.split("\n")).filter(e -> e.trim().length() > 0).toArray(String[]::new))));
                }
            }
        }

        r.getSections().put(SectionType.EXPERIENCE, addOrganizationSection(request, SectionType.EXPERIENCE));
        r.getSections().put(SectionType.EDUCATION, addOrganizationSection(request, SectionType.EDUCATION));

        if (!addNew) {
            storage.update(r);
        } else {
            storage.save(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null || (uuid == null && !action.equals("add"))) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        Boolean addNew = false;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                r = new Resume();
                addNew = true;
                action = "edit";
                request.setAttribute("uuid", r.getUuid());
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.setAttribute("addNew", addNew);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private OrganizationSection addOrganizationSection(HttpServletRequest request, SectionType processedSectionType) {

        OrganizationSection section = new OrganizationSection();
        Map<String, String[]> params = request.getParameterMap();

        String[] organizationNames = params.get("orgName");
        String[] organizationUrls = params.get("orgUrl");
        String[] sectionTypeNames = params.get("sectionType");
        String[] periodFrom = params.get("periodFrom");
        String[] periodTo = params.get("periodTo");

        String[] sectionTypeLink = params.get("parentSectionType");
        String[] sectionIdLink = params.get("parentSectionId");

        Map<String, List<Integer>> mapSectionIdLink = new HashMap<>();

        for (int i = 0; i < sectionIdLink.length; i++) {
            if (!sectionTypeLink[i].equals(processedSectionType.name())) {
                continue;
            }
            if (mapSectionIdLink.get(sectionIdLink[i]) == null) {
                mapSectionIdLink.put(sectionIdLink[i], new ArrayList<>(List.of(i)));
            } else {
                mapSectionIdLink.get(sectionIdLink[i]).add(i);
            }
        }

        Map<Link, List<Organization.Period>> organizationMap = new HashMap<>(organizationNames.length);

        for (int i = 0; i < organizationNames.length; i++) {
            if (!sectionTypeNames[i].equals(processedSectionType.name()) ||
                    organizationNames[i].strip().equals("") && organizationUrls[i].strip().equals("")) {
                continue;
            }
            String organizationName = organizationNames[i].strip();
            String organizationUrl = organizationUrls[i].strip();
            Link link = new Link(organizationName, organizationUrl);

            List<Organization.Period> ops = new ArrayList<>();
            if (mapSectionIdLink.get(params.get("sectionId")[i]) != null) {
                for (var j : mapSectionIdLink.get(params.get("sectionId")[i])) {
                    String organizationTitle = params.get("periodTitle")[j].strip();
                    String organizationDescr = params.get("periodDescription")[j].strip();
                    Organization.Period op = new Organization.Period(
                            DateUtil.ofBegMon(periodFrom[j]),
                            DateUtil.ofEndMon(periodTo[j]),
                            organizationTitle,
                            organizationDescr);
                    if (periodFrom[j] != "") {
                        ops.add(op);
                    }
                }
            }
            organizationMap.put(link, ops);
        }

        for (var orgEntry : organizationMap.entrySet()) {
            section.addItem(new Organization(orgEntry.getKey(), orgEntry.getValue()));
        }
        return section;
    }

    public static ServletContext getContext() {
        return context;
    }
}
