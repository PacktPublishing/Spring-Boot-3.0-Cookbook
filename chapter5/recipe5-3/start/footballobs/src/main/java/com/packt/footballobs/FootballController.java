package com.packt.footballobs;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/football")
public class FootballController {

    private FileLoader fileLoader;

    public FootballController(FileLoader fileLoader){
        this.fileLoader = fileLoader;
    }

    @GetMapping
    public List<String> getTeams(){
        return fileLoader.getTeams();
    }
    
}
