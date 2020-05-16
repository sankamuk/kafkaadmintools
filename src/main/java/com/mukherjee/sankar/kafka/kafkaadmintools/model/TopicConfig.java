package com.mukherjee.sankar.kafka.kafkaadmintools.model;

import javax.validation.constraints.NotNull;

public class TopicConfig {

    @NotNull
    private String key;

    public String getKey() {
        return key;
    }

    @NotNull
    private String value;

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Topic Config {");
        sb.append("Key='").append(key)
                .append("\', Value='").append(value);
        sb.append("\'}");
        return sb.toString();
    }


    public static final class TopicConfigBuilder {

        private String key;
        private String value;

        private TopicConfigBuilder() {
        }

        public static TopicConfig.TopicConfigBuilder aTopicConfig() {
            return new TopicConfig.TopicConfigBuilder();
        }

        public TopicConfig.TopicConfigBuilder withKey(String key) {
            this.key = key;
            return this;
        }

        public TopicConfig.TopicConfigBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public TopicConfig build() {
            TopicConfig config = new TopicConfig();
            config.key = this.key;
            config.value = this.value;
            return config;
        }
    }
}
