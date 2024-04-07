package com.packt.football.controller;

import javax.management.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequestMapping("/mbeans")
@RestController
public class MBeansController {

    private MBeanServer mbeanServer;

    public MBeansController(MBeanServer mbeanServer) {
        this.mbeanServer = mbeanServer;
    }

    @GetMapping()
    public String listMBeans() {
        Set<ObjectInstance> instances = mbeanServer.queryMBeans(null, null);
        StringBuilder sb = new StringBuilder();
        for (ObjectInstance instance : instances) {
            sb.append(instance.getObjectName().toString()).append("\n");
        }
        return sb.toString();
    }

}
