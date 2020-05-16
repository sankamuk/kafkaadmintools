package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.jcraft.jsch.*;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.ACL;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ServiceHelper {

  private ServiceHelper() {
  }

  private static Logger LOG = LoggerFactory.getLogger(ServiceHelper.class);

  public static NewTopic fromTopic(Topic topic) {
    return new NewTopic(topic.getName(), topic.getPartitions(), topic.getReplicationFactor());
  }

  public static int compareByName(Topic a, Topic b) {

      return a.getName().compareTo(b.getName());
  }

  public static int compareACL(ACL a, ACL b) {
    return (a.getUserName().compareTo(b.getUserName() +
            a.getResourceName().compareTo(b.getResourceName()))
    );
  }

  public static boolean runRemoteCommand(String host, String command, String sucess)
          throws JSchException, IOException {

      LOG.info(String.format("Function runRemoteCommand, Host %s Command %s Success string %s",
              host, command, sucess));
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      JSch jsch = new JSch();
      Session session=jsch.getSession("root", host, 22);
      session.setPassword("x");
      session.setConfig(config);
      session.connect();
      LOG.info("Connected");

      Channel channel = session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
      InputStream commandOutput = channel.getInputStream();
      channel.connect();
      int readByte = commandOutput.read();

      StringBuilder outputBuffer = new StringBuilder();
      while(readByte != 0xffffffff)
      {
          outputBuffer.append((char)readByte);
          readByte = commandOutput.read();
      }

      channel.disconnect();
      if (outputBuffer.toString().contains(sucess)){
          LOG.info("Found success string in output");
          return true;
      } else {
          LOG.info("Failed to find success string in output");
          return false;
      }

  }


}
