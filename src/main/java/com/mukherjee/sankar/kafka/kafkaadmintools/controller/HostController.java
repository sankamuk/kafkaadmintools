package com.mukherjee.sankar.kafka.kafkaadmintools.controller;

import com.mukherjee.sankar.kafka.kafkaadmintools.services.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
public class HostController {

    @Autowired
    private HostService hostService;

    @GetMapping(path = "/services")
    public Collection<String> getAllServices() throws IOException, InterruptedException {
        return hostService.getAllService();
    }

    @GetMapping(path = "/services/{serviceName}")
    public String getServicesState(
            @PathVariable(name = "serviceName", required = true) String serviceName)
            throws IOException, InterruptedException {

        return hostService.getServiceStatus(serviceName);
    }

    @PutMapping(path = "/services/{serviceName}/{serviceStatus}")
    public ResponseEntity setServicesState(
            @PathVariable(name = "serviceName", required = true) String serviceName,
            @PathVariable(name = "serviceStatus", required = true) String serviceStatus)
            throws IOException, InterruptedException {

        int s = hostService.setServiceStatus(serviceName, serviceStatus);
        if (s == 0){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
