package com.urise.webapp.web;

import com.urise.webapp.model.*;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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
                case ACHIEVEMENT, QUALIFICATIONS ->
                        r.addSection(sectionType, new ListTextSection((Arrays.stream(value.split("\n")).toList())));
            }
        }

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

    public static ServletContext getContext() {
        return context;
    }
}
