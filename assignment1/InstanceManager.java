package assignment1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;

public class InstanceManager {
  private final ArrayList<Employee> employees;
  private final Ec2OpWrapper ec2OpWrapper;
  private final HashMap<String, ArrayList<Integer>> policy;
  private final AmazonEC2 ec2;
  
  public InstanceManager(ArrayList<Employee> employees,
      HashMap<String, ArrayList<Integer>> policy,
      AWSCredentials credentials) {
    this.employees = employees;
    this.ec2 = new AmazonEC2Client(credentials);
    ec2OpWrapper = new Ec2OpWrapper(ec2, credentials);
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
      Collection<String> amiIds = new ArrayList<String>();
      for(Employee employee: employees) {
        String amiName = employee.getUsername() + "-" + new Random().nextInt();
        String amiId = snapshotAndTermInst(employee.getInstanceId(),
            employee.getVolumeIds(), employee.getIp(), amiName);
        amiIds.add(amiId);
        employee.setAmiId(amiId);
      }
      
      boolean isImaging = true;
      System.out.println("Waiting for snapshots to be fully created...");
      while (isImaging)
      {
        isImaging = false;
        DescribeImagesRequest describeRequest = new DescribeImagesRequest().withImageIds(amiIds);
        DescribeImagesResult describeResponse = ec2.describeImages(describeRequest);
        List<Image> images = describeResponse.getImages();
        for(Image image: images) {
          if (image.getState() != "available") {
            isImaging = true;
            break;
          }
        }
        if (isImaging) {
          try {
            Thread.sleep(10000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      System.out.println("Snapshots have been fully created!");
    } catch (AmazonServiceException ase) {
      System.err.println("Caught Exception: " + ase.getMessage());
      System.err.println("Reponse Status Code: " + ase.getStatusCode());
      System.err.println("Error Code: " + ase.getErrorCode());
      System.err.println("Request ID: " + ase.getRequestId());
    }
  }

  public void examAndTermIdleInsts() {
    for(Employee employee: employees) {
      if ((ec2OpWrapper.getCpuUsage(employee.getInstanceId(), 10)) < 5) {
        String amiName = employee.getUsername() + "-" + new Random().nextInt();
        String amiId = snapshotAndTermInst(employee.getInstanceId(),
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

  public String snapshotAndTermInst(String instanceId,
      ArrayList<String> volumeIds, String ip, String amiName) 
      throws AmazonServiceException {
    ec2OpWrapper.detachVolumes(instanceId, volumeIds);
    ec2OpWrapper.dissociateElasticIp(ip);
    String amiId = ec2OpWrapper.snapshot(instanceId, amiName);
    ec2OpWrapper.terminateInstance(instanceId);
    return amiId;
  }
}
