package com.mukherjee.sankar.kafka.kafkaadmintools.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Topic {

  @NotNull
  private String name;

  @NotNull
  @Min(1)
  private int partitions;

  @NotNull
  @Min(1)
  private short replicationFactor;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Topic{");
    sb.append("name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public int getPartitions() {
    return partitions;
  }

  public short getReplicationFactor() {
    return replicationFactor;
  }


  public static final class TopicBuilder {

    private String name;
    private int partitions;
    private short replicationFactor;

    private TopicBuilder() {
    }

    public static TopicBuilder aTopic() {
      return new TopicBuilder();
    }

    public TopicBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TopicBuilder withPartitions(int partitions) {
      this.partitions = partitions;
      return this;
    }

    public TopicBuilder withReplicationFactor(short replicationFactor) {
      this.replicationFactor = replicationFactor;
      return this;
    }

    public Topic build() {
      Topic topic = new Topic();
      topic.setName(name);
      topic.partitions = this.partitions;
      topic.replicationFactor = this.replicationFactor;
      return topic;
    }
  }
}
