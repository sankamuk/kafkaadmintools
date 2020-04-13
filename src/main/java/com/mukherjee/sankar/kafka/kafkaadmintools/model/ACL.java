package com.mukherjee.sankar.kafka.kafkaadmintools.model;

import javax.validation.constraints.NotNull;

public class ACL {

  @NotNull
  private String userName;

  public String getUserName() {
    return userName;
  }

  @NotNull
  private String resourceName;

  public String getResourceName() {
    return resourceName;
  }


  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ACL{");
    sb.append("Principal='").append(userName)
            .append("\', Name='").append(resourceName);
    sb.append("\'}");
    return sb.toString();
  }


  public static final class ACLBuilder {

    private String userName;
    private String resourceName;

    private ACLBuilder() {
    }

    public static ACLBuilder aACL() {
      return new ACLBuilder();
    }

    public ACLBuilder withUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public ACLBuilder withResourceName(String resourceName) {
      this.resourceName = resourceName;
      return this;
    }

    public ACL build() {
      ACL acl = new ACL();
      acl.userName = this.userName;
      acl.resourceName = this.resourceName;
      return acl;
    }
  }
}
