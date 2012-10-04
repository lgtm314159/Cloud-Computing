package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;

public class InstanceManager {
  private final ArrayList<Employee> employees;
  private final AWSCredentials credentials;
  private final AmazonEC2 ec2;
  private final AmazonS3Client s3;
  private final EC2OpWrapper ec2OpWrapper;
  private final HashMap<String, ArrayList<Integer>> policy;
  
  public InstanceManager(ArrayList<Employee> employees,
      AWSCredentials credentials) {
    this.employees = employees;
    this.credentials = credentials;
    ec2 = new AmazonEC2Client(credentials);
    s3 = new AmazonS3Client(credentials);
    ec2OpWrapper = new EC2OpWrapper(ec2);
    policy = new HashMap<String, ArrayList<Integer>>();
    initializePolicy();
  }

  public void initializePolicy() {
    ArrayList<Integer> tcpPorts = new ArrayList<Integer>();
    tcpPorts.add(22);
    tcpPorts.add(80);
    tcpPorts.add(443);
    policy.put("tcp", tcpPorts);
  }

  // Initial creation of all instances on Day1 morning.
  public void initialize() {
    for(Employee employee: employees) {
      try {
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
      } catch (AmazonServiceException ase) {
        System.err.println("Caught Exception: " + ase.getMessage());
        System.err.println("Reponse Status Code: " + ase.getStatusCode());
        System.err.println("Error Code: " + ase.getErrorCode());
        System.err.println("Request ID: " + ase.getRequestId());
      }
    }
  }

  public void createMorningInstances() {
    for(Employee employee: employees) {
      try {
        String createdInstanceId = createInstance(employee.getAmiId(),
            employee.getKeyPairName(), employee.getGroup(), employee.getIp(),
            employee.getVolumeIds(), employee.getUsername());
        employee.setInstanceId(createdInstanceId);
      } catch (AmazonServiceException ase) {
        System.err.println("Caught Exception: " + ase.getMessage());
        System.err.println("Reponse Status Code: " + ase.getStatusCode());
        System.err.println("Error Code: " + ase.getErrorCode());
        System.err.println("Request ID: " + ase.getRequestId());
      }
    }
  }

  public void afterWorkCleanup() {
    for(Employee employee: employees) {
      String amiName = employee.getUsername() + "-" + new Random().nextInt();
      String amiId = snapshotAndTerminateInst(employee.getInstanceId(),
          employee.getVolumeIds(), employee.getIp(), amiName);
      employee.setAmiId(amiId);      
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
      ArrayList<String> volumeIds, String ip, String amiName) {
    ec2OpWrapper.detachVolumes(instanceId, volumeIds);
    ec2OpWrapper.dissociateElasticIp(ip);
    String amiId = ec2OpWrapper.snapshot(instanceId, amiName);
    //ec2OpWrapper.terminateInstance(instanceId);
    return amiId;
  }
}
