package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
//        super.init(config);
//        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        SqlStorage storage = Config.get().getSqlStorage();
        PrintWriter wr = response.getWriter();
        List<Resume> resumeList = new ArrayList<>();
        wr.write("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"></head>");
        if (uuid == null) {
            resumeList = storage.getAllSorted();
            wr.write("<H3>Список всех резюме</H3>");
        } else {
            resumeList.add(storage.get(uuid));
            wr.write("<H3>Резюме по запросу</H3>");
        }
        wr.write("<table style=\"width:100%\">");
        wr.write("<tr><th>UUID<th>ФИО</tr>");
        for (Resume resume : resumeList) {
            wr.write("<tr><td>" + resume.getUuid() + "</td><td>" + resume.getFullName() + "</td></tr>");
        }
        wr.write("</table>");
        wr.write("</html>");
    }
}
