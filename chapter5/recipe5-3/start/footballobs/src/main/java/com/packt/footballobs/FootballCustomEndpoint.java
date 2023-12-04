package com.packt.footballobs;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

@Endpoint(id = "football")
public class FootballCustomEndpoint {

    private FileLoader fileLoader;

    FootballCustomEndpoint(FileLoader fileLoader){
        this.fileLoader = fileLoader;
    }
    
    @ReadOperation
    public String getFileVersion(){
        return fileLoader.getFileName();
    }

    @WriteOperation
    public void refreshFile(){
        try {
            fileLoader.loadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
