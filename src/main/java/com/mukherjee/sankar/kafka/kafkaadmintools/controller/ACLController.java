package com.mukherjee.sankar.kafka.kafkaadmintools.controller;

import com.mukherjee.sankar.kafka.kafkaadmintools.model.ACL;
import com.mukherjee.sankar.kafka.kafkaadmintools.model.Topic;
import com.mukherjee.sankar.kafka.kafkaadmintools.services.ACLService;
import com.mukherjee.sankar.kafka.kafkaadmintools.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

@RestController
public class ACLController {

  @Autowired
  private ACLService aclService;

  @GetMapping(path = "/acl/{topicName}")
  public Collection<ACL> getACL(
          @PathVariable(name = "topicName", required = true) String topicName)
          throws ExecutionException, InterruptedException {
    return aclService.getACL(topicName);
  }

  @PostMapping(path = "/acl")
  public String addACL(@Valid @RequestBody(required = true) ACL acl) {
    try {
      aclService.addACL(acl);
      return "true";
    } catch (ExecutionException e) {
      e.printStackTrace();
      return "false";
    } catch (InterruptedException e) {
      e.printStackTrace();
      return "false";
    }

  }

  @DeleteMapping("/acl/{userName}/{topicName}")
  public String deleteACL(@PathVariable(name = "userName", required = true) String userName,
                          @PathVariable(name = "topicName", required = true) String topicName) {
    try {
      aclService.deleteACL(ACL.ACLBuilder.aACL()
              .withUserName(userName)
              .withResourceName(topicName)
              .build());

      return "true";
    } catch (ExecutionException e) {
      e.printStackTrace();
      return "false";
    } catch (InterruptedException e) {
      e.printStackTrace();
      return "false";
    }

  }

}
