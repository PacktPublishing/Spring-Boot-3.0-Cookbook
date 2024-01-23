package com.packt.football;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/serviceinfo")
@RestController
public class ServiceInformationController {

    @Value("${football.instanceId}")
    private String instanceId;

    @GetMapping
    public String getInstanceId() {
        return instanceId;
    }
}
