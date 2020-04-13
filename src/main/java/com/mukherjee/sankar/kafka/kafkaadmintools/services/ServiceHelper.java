package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic;
import org.apache.kafka.clients.admin.NewTopic;

public class ServiceHelper {

  private ServiceHelper() {
  }

  public static NewTopic fromTopic(Topic topic) {
    return new NewTopic(topic.getName(), topic.getPartitions(), topic.getReplicationFactor());
  }

  public static int compareByName(Topic a, Topic b) {
    return a.getName().compareTo(b.getName());
  }


}
