package com.packt.football.controller;

import javax.management.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/mbeans")
@RestController
public class MBeansController {

    private MBeanServer mbeanServer;

    public MBeansController(MBeanServer mbeanServer) {
        this.mbeanServer = mbeanServer;
    }

    @GetMapping()
    public List<String> listMBeans() {
        Set<ObjectInstance> instances = mbeanServer.queryMBeans(null, null);
        return instances
                .stream()
                .map(instance -> instance.getObjectName().toString())
                .collect(Collectors.toList());
    }

}
