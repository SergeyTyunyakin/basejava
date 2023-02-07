package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SerializationStrategy {
    private final String NULL_STR = "NULL_STR";

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            writeCollection(dos, contacts.entrySet(), e -> {
                dos.writeUTF(e.getKey().name());
                dos.writeUTF(e.getValue());
            });

            Map<SectionType, AbstractSection> sections = r.getSections();
            writeCollection(dos, sections.entrySet(), section -> {
                dos.writeUTF(section.getKey().name());
                switch (section.getKey()) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section.getValue()).getText());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            writeCollection(dos, ((ListTextSection) section.getValue()).getItems(), dos::writeUTF);
                    case EXPERIENCE, EDUCATION -> {
                        writeCollection(dos, ((OrganizationSection) section.getValue()).getItems(), organization -> {
                            dos.writeUTF(organization.getLink().getName());
                            writeUTFWithNull(dos, organization.getLink().getUrl());
                            writeCollection(dos, organization.getPeriods(), period -> {
                                dos.writeUTF(period.getDateFrom().toString());
                                dos.writeUTF(period.getDateTo().toString());
                                dos.writeUTF(period.getTitle());
                                writeUTFWithNull(dos, period.getDescription());
                            });
                        });
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readCollection(dis, () -> {
                var sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> {
                        TextSection section = new TextSection();
                        section.setText(dis.readUTF());
                        resume.addSection(sectionType, section);
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListTextSection section = new ListTextSection(readList(dis, dis::readUTF));
                        resume.addSection(sectionType, section);
                    }
                    case EXPERIENCE, EDUCATION -> {
                        OrganizationSection section = new OrganizationSection();
                        readCollection(dis, () -> {
                            Organization org = new Organization();
                            Link link = new Link(dis.readUTF(), readUTFWithNull(dis));
                            org.setLink(link);

                            readCollection(dis, () -> {
                                Organization.Period period = new Organization.Period();
                                period.setDateFrom(LocalDate.parse(dis.readUTF()));
                                period.setDateTo(LocalDate.parse(dis.readUTF()));
                                period.setTitle(dis.readUTF());
                                period.setDescription(readUTFWithNull(dis));
                                org.addPeriod(period);
                            });
                            section.addItem(org);
                        });

                        resume.addSection(sectionType, section);

                    }

                    default -> throw new IllegalStateException("Unexpected value: " + sectionType);
                }
            });
            return resume;
        }
    }

    protected void writeUTFWithNull(DataOutputStream dos, String str) throws IOException {
        if (str == null) {
            dos.writeUTF(NULL_STR);
        } else {
            dos.writeUTF(str);
        }
    }

    protected String readUTFWithNull(DataInputStream dis) throws IOException {
        String str = dis.readUTF();
        if (str.equals(NULL_STR)) {
            return null;
        } else {
            return str;
        }
    }


    private interface Reader<E> {
        E readElement() throws IOException;
    }

    private interface Writer<E> {
        void writeElement(E element) throws IOException;
    }

    private interface CollectionReader {
        void readCollectionItem() throws IOException;
    }

    private <E> void writeCollection(DataOutputStream dos, Collection<E> collection, Writer<E> writer) throws IOException {
        dos.writeInt(collection.size());
        for (var element : collection) {
            writer.writeElement(element);
        }
    }

    private <E> List<E> readList(DataInputStream dis, Reader<E> reader) throws IOException {
        int size = dis.readInt();
        List<E> list = new ArrayList<>(size);
        for (var i = 0; i < size; i++) {
            list.add(reader.readElement());
        }
        return list;
    }

    private void readCollection(DataInputStream dis, CollectionReader reader) throws IOException {
        int size = dis.readInt();
        for (var i = 0; i < size; i++) {
            reader.readCollectionItem();
        }
    }

}
