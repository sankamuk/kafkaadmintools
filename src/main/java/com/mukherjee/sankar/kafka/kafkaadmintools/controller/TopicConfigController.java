package com.mukherjee.sankar.kafka.kafkaadmintools.controller;

import com.mukherjee.sankar.kafka.kafkaadmintools.model.TopicConfig;
import com.mukherjee.sankar.kafka.kafkaadmintools.services.TopicConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;

@RestController
public class TopicConfigController {

    @Autowired
    private TopicConfigService topicConfigService;

    @GetMapping(path = "/topicconfig/{topicName}")
    public Collection<TopicConfig> getTopicConfig(
            @PathVariable(name = "topicName", required = true) String topicName)
            throws IOException, InterruptedException, ExecutionException{
        return topicConfigService.getTopicConfig(topicName);
    }

    @PostMapping(path = "/topicconfig/{topicName}")
    public void updateTopicConfig(@Valid @RequestBody(required = true) TopicConfig topicConfig,
            @PathVariable(name = "topicName", required = true) String topicName)
            throws IOException, InterruptedException, ExecutionException {

        topicConfigService.updateConfig(topicName, topicConfig);

    }
}
