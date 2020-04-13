package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic.TopicBuilder;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TopicService {

  private static Logger LOG = LoggerFactory.getLogger(TopicService.class);

  @Autowired
  private AdminClient adminClient;

  public Collection<Topic> getAllTopics() throws ExecutionException, InterruptedException {
    return adminClient
        .describeTopics(fetchAllTopicNames())
        .all()
        .get()
        .values()
        .stream()
        .map(topicDesc -> TopicBuilder
            .aTopic()
            .withName(topicDesc.name())
            .withPartitions(topicDesc.partitions().size())
            .withReplicationFactor((short) topicDesc.partitions().get(0).replicas().size())
            .build())
        .sorted(ServiceHelper::compareByName)
        .collect(Collectors.toList());
  }

  @PreDestroy
  private void preDestroy() {
    LOG.info("CLose Kafka admin client: {}", adminClient);
    adminClient.close(3, TimeUnit.SECONDS);
  }

  public Topic getTopic(String topicName)
      throws ExecutionException, InterruptedException {
    return adminClient
        .describeTopics(Arrays.asList(topicName))
        .all()
        .get()
        .values()
        .stream()
        .map(topicDesc -> TopicBuilder
            .aTopic()
            .withName(topicDesc.name())
            .withPartitions(topicDesc.partitions().size())
            .withReplicationFactor((short) topicDesc.partitions().get(0).replicas().size())
            .build())
        .sorted(ServiceHelper::compareByName)
        .collect(Collectors.toList())
        .get(0);
  }


  public void createTopic(Topic topic) throws ExecutionException, InterruptedException {
    adminClient
            .createTopics(Arrays.asList(ServiceHelper.fromTopic(topic)))
            .values()
            .get(topic.getName())
            .get();
  }

  public void deleteTopic(String name) throws ExecutionException, InterruptedException {
    adminClient.deleteTopics(Arrays.asList(name))
            .values()
            .get(name)
            .get();
  }

  private Collection<String> fetchAllTopicNames() throws ExecutionException, InterruptedException {
    return adminClient
        .listTopics(new ListTopicsOptions().timeoutMs(500))
        .names()
        .get();
  }

}
