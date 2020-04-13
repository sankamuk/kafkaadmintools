package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.mukherjee.sankar.kafka.kafkaadmintools.model.ACL;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic.TopicBuilder;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.resource.Resource;
import org.apache.kafka.common.resource.ResourceFilter;
import org.apache.kafka.common.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
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

  public String getACL(String topicName)
          throws ExecutionException, InterruptedException {

    return adminClient
            .describeAcls(new AclBindingFilter(new ResourceFilter(ResourceType.TOPIC, topicName),
                    new AccessControlEntryFilter(null, "*", AclOperation.ANY, AclPermissionType.ANY)))
            .values()
            .get()
            .stream()
            .map(aclDesc -> ACL.ACLBuilder
            .aACL()
            .withUserName(aclDesc.entry().principal().toString())
            .withResourceName(aclDesc.resource().name().toString())
            .build())
            .collect(Collectors.toList())
            .toString();

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

  public void addACL(ACL acl) throws ExecutionException, InterruptedException {

    LOG.info("ACL User: "+acl.getUserName()+", Topic: "+acl.getResourceName());

    AclBinding a1 = new AclBinding(new Resource(ResourceType.TOPIC, acl.getResourceName()),
            new AccessControlEntry("User:"+acl.getUserName(), "*", AclOperation.DESCRIBE, AclPermissionType.ALLOW
            ));
    LOG.info(a1.toString());
    Collection<AclBinding> aclList = Arrays.asList(a1);
    LOG.info(adminClient
            .createAcls(aclList)
            .values()
            .values()
            .toString());

  }

  public void deleteTopic(String name) throws ExecutionException, InterruptedException {
    adminClient.deleteTopics(Arrays.asList(name))
            .values()
            .get(name)
            .get();
  }

  public void deleteACL(ACL acl) throws ExecutionException, InterruptedException {
    LOG.info("Testing User: "+ acl.getUserName() + ", Topic"+ acl.getResourceName());
    Collection<AclBinding> aclsBindings = adminClient
            .describeAcls(new AclBindingFilter(new ResourceFilter(ResourceType.TOPIC, acl.getResourceName()),
                    new AccessControlEntryFilter("User:"+acl.getUserName(), "*", AclOperation.ANY, AclPermissionType.ANY)))
            .values()
            .get();

    Collection<AclBindingFilter> acls = new ArrayList<AclBindingFilter>();

    for(AclBinding a: aclsBindings){
      acls.add(new AclBindingFilter(
              new ResourceFilter(a.resource().resourceType(), a.resource().name()),
              new AccessControlEntryFilter(a.entry().principal(),
                      a.entry().host(),
                      a.entry().operation(),
                      a.entry().permissionType())
      ));
    }

    adminClient.deleteAcls(acls);

  }

  private Collection<String> fetchAllTopicNames() throws ExecutionException, InterruptedException {
    return adminClient
        .listTopics(new ListTopicsOptions().timeoutMs(500))
        .names()
        .get();
  }

}
