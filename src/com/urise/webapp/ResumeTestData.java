package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume r = createResume("uuid1", "Григорий Кислин");

        System.out.println("*".repeat(50));
        System.out.println(r.getFullName());
        System.out.println("*".repeat(50));

        r.getContacts().forEach((k, v) -> System.out.println(k.getTitle() + ": " + v));
        System.out.println("*".repeat(50));

        r.getSections().forEach((k, v) -> {
            System.out.println(k.getTitle() + ":");
            System.out.println(v.toString());
            System.out.println("*".repeat(50));
        });
    }

    public static Resume createResume(String uuid, String fullName) {
        Resume r = new Resume(uuid, fullName);

        r.addContact(ContactType.PHONE, "+7(921) 855-0482");
        r.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        r.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        r.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        r.addContact(ContactType.HOMEPAGE, "http://gkislin.ru/");

        r.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        r.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        TextItemsSection item1 = new TextItemsSection();
        item1.addItem("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        item1.addItem("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");

        r.addSection(SectionType.ACHIEVEMENT, item1);

        TextItemsSection item2 = new TextItemsSection();
        item2.addItem("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        item2.addItem("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        item2.addItem("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");

        r.addSection(SectionType.QUALIFICATIONS, item2);

        OrganizationSection org1 = new OrganizationSection();
        org1.addItem(new Organization("Alcatel",
                "Инженер по аппаратному и программному тестированию",
                "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).",
                "http://www.alcatel.ru/",
                LocalDate.of(1997, 9, 1),
                LocalDate.of(2005, 1, 31)));

        r.addSection(SectionType.EXPERIENCE, org1);

        OrganizationSection org2 = new OrganizationSection();
        org2.addItem(new Organization("Заочная физико-техническая школа при МФТИ",
                "Закончил с отличием",
                "",
                "https://mipt.ru/",
                LocalDate.of(1997, 9, 1),
                LocalDate.of(2005, 1, 31)));

        r.addSection(SectionType.EDUCATION, org2);

        return r;
    }

}
