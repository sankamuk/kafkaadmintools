package com.mukherjee.sankar.kafka.kafkaadmintools.model;

import javax.validation.constraints.NotNull;

public class Process {

    @NotNull
    private String hostName;

    public String getHostName() {
        return hostName;
    }

    @NotNull
    private String state;

    public String getState() {
        return state;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Process{");
        sb.append("Host='").append(hostName)
                .append("\', State='").append(state);
        sb.append("\'}");
        return sb.toString();
    }


    public static final class ProcessBuilder {

        private String hostName;
        private String state;

        private ProcessBuilder() {
        }

        public static Process.ProcessBuilder aProcess() {
            return new Process.ProcessBuilder();
        }

        public Process.ProcessBuilder withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Process.ProcessBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public Process build() {
            Process process = new Process();
            process.hostName = this.hostName;
            process.state = this.state;
            return process;
        }
    }
}
