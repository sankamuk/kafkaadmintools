package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.jcraft.jsch.*;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Process;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class ProcessService {

    private static Logger LOG = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    private AdminClient adminClient;


    @PreDestroy
    private void preDestroy() {
        LOG.info("CLose Kafka admin client: {}", adminClient);
        adminClient.close(3, TimeUnit.SECONDS);
    }

    public Collection<Process> getProcessState(String processName)
            throws IOException, InterruptedException, ExecutionException, JSchException {

        Collection<Process> result = new ArrayList<Process>();
        Collection<Node> nodes = adminClient.describeCluster().nodes().get();
        for(Node broker: nodes){
            LOG.info("Host: "+broker.host());
            if (ServiceHelper.runRemoteCommand(broker.host(), "/opt/kafka/bin/kafka status", "started")){
                LOG.info("Success");
                result.add(Process.ProcessBuilder.aProcess()
                        .withHostName(broker.host())
                        .withState("Running")
                        .build());
            } else {
                LOG.info("Failed");
                result.add(Process.ProcessBuilder.aProcess()
                        .withHostName(broker.host())
                        .withState("Stopped")
                        .build());
            }

        }

        return result;
    }
}
