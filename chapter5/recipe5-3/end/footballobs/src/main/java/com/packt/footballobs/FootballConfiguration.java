package com.packt.footballobs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FootballConfiguration {
    
    @Value("${football.folder}")
    private String folder;

    @Bean
    public FileLoader fileLoader() throws IOException{        
        FileLoader fileLoader = new FileLoader(folder);
        fileLoader.loadFile();        
        return fileLoader;
    }

    @Bean
    public FootballCustomEndpoint footballCustomEndpoint(FileLoader fileLoader){
        return new FootballCustomEndpoint(fileLoader);
    }
}
