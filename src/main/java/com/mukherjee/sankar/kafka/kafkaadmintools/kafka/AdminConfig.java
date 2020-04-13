package com.mukherjee.sankar.kafka.kafkaadmintools.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConfigurationProperties(value = "kafka")
public class AdminConfig {


  private Map<String, String> admin;

  public void setAdmin(Map<String, String> admin) {
    this.admin = admin;
  }

  public Map<String, String> getAdmin() {
    return admin;
  }

  public Map<String, Object> getGenericProperties() {
    return Collections.<String, Object>unmodifiableMap(admin);
  }

  @Bean
  public AdminClient getClient() {
    return AdminClient.create(getGenericProperties());
  }

}

