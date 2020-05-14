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
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.io.InputStream;

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

        Collection<Node> nodes = adminClient.describeCluster().nodes().get();
        for(Node broker: nodes){
            LOG.info("Host: "+broker.host());

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session=jsch.getSession("root", broker.host(), 22);
            session.setPassword("x");
            session.setConfig(config);
            session.connect();
            LOG.info("Connected");

            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand("ls");
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            StringBuilder outputBuffer = new StringBuilder();
            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }

            LOG.info(outputBuffer.toString());

            channel.disconnect();
        }

        return null;
    }
}
