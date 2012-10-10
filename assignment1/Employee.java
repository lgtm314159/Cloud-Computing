package assignment1;

import java.util.ArrayList;

public class Employee {
  private String username;
  private String group;
  private String ip;
  private String hostname;
  private String instanceId;
  private String amiId;
  private String keyPairName;
  private ArrayList<String> volumeIds;
  private String bucketName;
  private boolean isActive;
  
  public Employee(String username, String group) {
    this.username = username;
    this.group = group;
    this.volumeIds = new ArrayList<String>();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getGroup() {
    return group;
  }
  
  public void setGroup(String group) {
    this.group = group;
  }
  
  public String getIp() {
    return ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip;
  }
  
  public String getHostnames() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getAmiId() {
    return amiId;
  }

  public void setAmiId(String amiId) {
    this.amiId = amiId;
  }
  
  public ArrayList<String> getVolumeIds() {
    return volumeIds;
  }

  public void addVolumeId(String volumeId) {
    volumeIds.add(volumeId);
  }

  public String getKeyPairName() {
    return keyPairName;
  }

  public void setKeyPairName(String keyPairName) {
    this.keyPairName = keyPairName;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }
  
  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActiveStat(boolean isActive) {
    this.isActive = isActive;
  }
}
