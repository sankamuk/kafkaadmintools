package com.mukherjee.sankar.kafka.kafkaadmintools.controller;

import com.jcraft.jsch.JSchException;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Process;
import com.mukherjee.sankar.kafka.kafkaadmintools.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

@RestController
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping(path = "/process/{processName}")
    public Collection<Process> getAllServices(
            @PathVariable(name = "processName", required = true) String processName)
            throws IOException, InterruptedException, ExecutionException, JSchException {
        return processService.getProcessState(processName);
    }

}
