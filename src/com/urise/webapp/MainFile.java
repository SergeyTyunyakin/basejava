package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        directoryListing(dir, "");
    }

    public static void directoryListing(File dir, String offset) {
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            Arrays.stream(fileList).forEach(file -> {
                if (file.isDirectory()) {
                    System.out.println(offset + "Dir: " + file.getName());
                    directoryListing(file, offset + " \t");
                } else if (file.isFile()) {
                    System.out.println(offset + "File: " + file.getName());
                }
            });
        }
    }
}
