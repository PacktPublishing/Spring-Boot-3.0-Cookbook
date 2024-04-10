package com.packt.footballobs.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.packt.footballobs.actuator.FootballCustomEndpoint;
import com.packt.footballobs.service.FileLoader;

@Configuration
public class FootballConfiguration {
    
    @Value("${football.folder}")
    private String folder;

    @Bean
    public FileLoader fileLoader() throws IOException{        
        return new FileLoader(folder);
    }

    @Bean
    public FootballCustomEndpoint footballCustomEndpoint(FileLoader fileLoader){
        return new FootballCustomEndpoint(fileLoader);
    }
}
