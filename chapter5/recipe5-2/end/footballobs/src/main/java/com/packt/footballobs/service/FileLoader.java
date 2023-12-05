package com.packt.footballobs.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileLoader {
    private String fileName;
    private List<String> teams;
    private String folder;

    public FileLoader(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void loadFile() throws IOException {
        Files.list(Paths.get(folder))
                .filter(Files::isRegularFile)
                .findFirst()
                .ifPresent(file -> {
                    try {
                        loadFile(file.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void loadFile(String fileName) throws Exception {
        this.fileName = fileName;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        teams = mapper.readValue(file, new TypeReference<List<String>>() {
        });
    }

    public List<String> getTeams() {
        return teams;
    }
}
