package com.quan12yt.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class LoadController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @GetMapping
    public List<String> getServices(){
        List<String> lst = restTemplate.getForObject("http://client-service/services", List.class);
        return lst;
    }

    @ResponseBody
    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public String showFirstService() {

        String serviceId = "client-service".toLowerCase();

        // (Need!!) eureka.client.fetchRegistry=true
        List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);

        if (instances == null || instances.isEmpty()) {
            return "No instances for service: " + serviceId;
        }
        String html = "<h2>Instances for Service Id: " + serviceId + "</h2>";

        for (ServiceInstance serviceInstance : instances) {
            html += "<h3>Instance :" + serviceInstance.getUri() + "</h3>";
        }

        // Create a RestTemplate.
        RestTemplate restTemplate = new RestTemplate();

        html += "<br><h4>Call /hello of service: " + serviceId + "</h4>";

        try {
            // May be throw IllegalStateException (No instances available)
            ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);

            html += "<br>===> Load Balancer choose: " + serviceInstance.getUri();

            String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/services";

            html += "<br>Make a Call: " + url;
            html += "<br>";

            String result = restTemplate.getForObject(url, String.class);

            html += "<br>Result: " + result;
        } catch (IllegalStateException e) {
            html += "<br>loadBalancer.choose ERROR: " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            html += "<br>Other ERROR: " + e.getMessage();
            e.printStackTrace();
        }
        return html;
    }

}


