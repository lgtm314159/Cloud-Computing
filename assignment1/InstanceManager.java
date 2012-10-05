package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;

public class InstanceManager {
  private final ArrayList<Employee> employees;
  private final Ec2OpWrapper ec2OpWrapper;
  private final HashMap<String, ArrayList<Integer>> policy;
  
  public InstanceManager(ArrayList<Employee> employees,
      HashMap<String, ArrayList<Integer>> policy,
      AWSCredentials credentials) {
    this.employees = employees;
    ec2OpWrapper = new Ec2OpWrapper(credentials);
    this.policy = policy;
  }

  // Initial creation of all instances on Day1 morning.
  public void initialize() {
    try {
      for(Employee employee: employees) {
        ec2OpWrapper.createKeyPair(employee.getKeyPairName());
        ec2OpWrapper.createSecurityGroup(employee.getGroup(),
            employee.getGroup(), policy);
        employee.setAmiId("ami-137bcf7a");
        String elasticIp = ec2OpWrapper.createElasticIp();
        employee.setIp(elasticIp);
        String volumeId = ec2OpWrapper.createVolume();
        employee.addVolumeId(volumeId);
        
        String createdInstanceId = this.createInstance(employee.getAmiId(),
            employee.getKeyPairName(), employee.getGroup(), employee.getIp(),
            employee.getVolumeIds(), employee.getUsername());
        employee.setInstanceId(createdInstanceId);
      }
    } catch (AmazonServiceException ase) {
      System.err.println("Caught Exception: " + ase.getMessage());
      System.err.println("Reponse Status Code: " + ase.getStatusCode());
      System.err.println("Error Code: " + ase.getErrorCode());
      System.err.println("Request ID: " + ase.getRequestId());
    }
  }

  public void createMorningInstances() {
    try {
      for(Employee employee: employees) {
          String createdInstanceId = createInstance(employee.getAmiId(),
              employee.getKeyPairName(), employee.getGroup(), employee.getIp(),
              employee.getVolumeIds(), employee.getUsername());
          employee.setInstanceId(createdInstanceId);
      }
    } catch (AmazonServiceException ase) {
      System.err.println("Caught Exception: " + ase.getMessage());
      System.err.println("Reponse Status Code: " + ase.getStatusCode());
      System.err.println("Error Code: " + ase.getErrorCode());
      System.err.println("Request ID: " + ase.getRequestId());
    }
  }

  public void afterWorkCleanup() {
    try {
      for(Employee employee: employees) {
        String amiName = employee.getUsername() + "-" + new Random().nextInt();
        String amiId = snapshotAndTerminateInst(employee.getInstanceId(),
            employee.getVolumeIds(), employee.getIp(), amiName);
        employee.setAmiId(amiId);
      }
    } catch (AmazonServiceException ase) {
      System.err.println("Caught Exception: " + ase.getMessage());
      System.err.println("Reponse Status Code: " + ase.getStatusCode());
      System.err.println("Error Code: " + ase.getErrorCode());
      System.err.println("Request ID: " + ase.getRequestId());
    }
  }

  public void examAndTermIdleInsts() {
    for(Employee employee: employees) {
      if ((ec2OpWrapper.getCpuUsage(employee.getInstanceId(), 10)) < 0.05) {
        String amiName = employee.getUsername() + "-" + new Random().nextInt();
        String amiId = snapshotAndTerminateInst(employee.getInstanceId(),
            employee.getVolumeIds(), employee.getIp(), amiName);
        employee.setAmiId(amiId);
      }
    }
  }

  public String createInstance(String amiId, String keyPairName,
      String groupName, String ip, ArrayList<String> volumeIds, String tag) 
      throws AmazonServiceException {
    String createdInstanceId = ec2OpWrapper.createInstance(
        amiId, keyPairName, groupName);
    ec2OpWrapper.createInstanceTag(createdInstanceId, tag);
    ec2OpWrapper.associateElasticIp(createdInstanceId, ip);
    ec2OpWrapper.attachVolumes(createdInstanceId, volumeIds);
    return createdInstanceId;
  }

  public String snapshotAndTerminateInst(String instanceId,
      ArrayList<String> volumeIds, String ip, String amiName) 
      throws AmazonServiceException {
    ec2OpWrapper.detachVolumes(instanceId, volumeIds);
    ec2OpWrapper.dissociateElasticIp(ip);
    String amiId = ec2OpWrapper.snapshot(instanceId, amiName);
    ec2OpWrapper.terminateInstance(instanceId);
    return amiId;
  }
}
