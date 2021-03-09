package com.quan12yt.discoveryclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/services")
    public List<String> getClients(){
        return (List<String>) discoveryClient.getServices();

    }

    @GetMapping("/service")
    public List<ServiceInstance> getService(String id){
        List<ServiceInstance> instances = discoveryClient.getInstances(id);
        instances.forEach(u -> {
            System.out.println("URL : " + u.getUri());
            System.out.println("Host : " + u.getHost());
            System.out.println("Port : " + u.getPort());
        });
        return instances;
    }
}
