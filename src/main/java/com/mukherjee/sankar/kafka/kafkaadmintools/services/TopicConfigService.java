package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import com.jcraft.jsch.JSchException;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Process;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.TopicConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TopicConfigService {

    private static Logger LOG = LoggerFactory.getLogger(TopicConfigService.class);

    @Autowired
    private AdminClient adminClient;


    @PreDestroy
    private void preDestroy() {
        LOG.info("CLose Kafka admin client: {}", adminClient);
        adminClient.close(3, TimeUnit.SECONDS);
    }

    public Collection<TopicConfig> getTopicConfig(String topicName)
            throws InterruptedException, ExecutionException {

        Collection<ConfigResource> resource = new ArrayList<ConfigResource>();
        resource.add(new ConfigResource(ConfigResource.Type.TOPIC, topicName));

        Collection<TopicConfig> result = new ArrayList<TopicConfig>();

        for (Config config : adminClient.describeConfigs(resource)
                .all()
                .get()
                .values()) {
            for( ConfigEntry e : config.entries()){
                result.add( TopicConfig.TopicConfigBuilder
                .aTopicConfig()
                .withKey(e.name())
                .withValue(e.value())
                .build());
            }
        }

        return result ;
    }


    public void updateConfig(String topicName, TopicConfig topicConfig)
            throws ExecutionException, InterruptedException {

        ConfigEntry cfg = new ConfigEntry(topicConfig.getKey(), topicConfig.getValue());


        Map<ConfigResource,Config> config = new HashMap<>();
        config.put(new ConfigResource(ConfigResource.Type.TOPIC, topicName),
                new Config(Arrays.asList(cfg)));

        adminClient.alterConfigs(config);


    }
}
