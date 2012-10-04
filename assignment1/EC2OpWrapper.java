package assignment1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.ec2.model.DisassociateAddressRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2OpWrapper {
  private final AmazonEC2 ec2;
  
  public EC2OpWrapper(AmazonEC2 ec2) {
    this.ec2 = ec2;
  }

  public void createKeyPair(String keyPairName)
      throws AmazonServiceException {
    System.out.println("Creating key pair...");
    CreateKeyPairRequest kpRequest = new CreateKeyPairRequest(keyPairName);
    CreateKeyPairResult kpResult = ec2.createKeyPair(kpRequest);
    try {
      FileOutputStream out = new FileOutputStream(keyPairName + ".pem");
      PrintStream ps = new PrintStream(out);
      ps.print(kpResult.getKeyPair().getKeyMaterial());
      ps.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  public void createSecurityGroup(String groupName, String description,
      HashMap<String, ArrayList<Integer>> policy)
      throws AmazonServiceException {
    // Create the group.
    CreateSecurityGroupRequest sgRequest = 
        new CreateSecurityGroupRequest(groupName, "Custom security group");
    ec2.createSecurityGroup(sgRequest);
    // Setup firewall policy for the created group.
    String ipAddr = "0.0.0.0/0";
    ArrayList<String> ipRanges = new ArrayList<String>();
    ipRanges.add(ipAddr);
    ArrayList<IpPermission> ipPermissions = new ArrayList<IpPermission>();
    if (policy.containsKey("tcp")) {
      for (int port: policy.get("tcp")) {
        IpPermission ipPermission = new IpPermission();
        ipPermission.setIpProtocol("tcp");
        ipPermission.setFromPort(port);
        ipPermission.setToPort(port);
        ipPermission.setIpRanges(ipRanges);
        ipPermissions.add(ipPermission);
      }
    }
    if (policy.containsKey("udp")) {
      for (int port: policy.get("udp")) {
        IpPermission ipPermission = new IpPermission();
        ipPermission.setIpProtocol("udp");
        ipPermission.setFromPort(port);
        ipPermission.setToPort(port);
        ipPermission.setIpRanges(ipRanges);
        ipPermissions.add(ipPermission);
      }
    }
    AuthorizeSecurityGroupIngressRequest ingressRequest =
        new AuthorizeSecurityGroupIngressRequest(groupName, ipPermissions);
    ec2.authorizeSecurityGroupIngress(ingressRequest);
  }

  public String createInstance(String amiId, String keyPairName,
      String groupName) throws AmazonServiceException {
    int minInstanceCount = 1; // create 1 instance.
    int maxInstanceCount = 1;
    RunInstancesRequest rir = new RunInstancesRequest(amiId, minInstanceCount, maxInstanceCount);
    rir.setKeyName(keyPairName);
    ArrayList<String> securityGroups = new ArrayList<String>();
    securityGroups.add(groupName);
    rir.setSecurityGroups(securityGroups);
    Placement placement = new Placement("us-east-1a");
    rir.setPlacement(placement);
    RunInstancesResult result = ec2.runInstances(rir);

    // Get instanceId from the result.
    List<Instance> resultInstance = result.getReservation().getInstances();
    String createdInstanceId = null;

    for (Instance ins : resultInstance){
     createdInstanceId = ins.getInstanceId();
     System.out.println("New instance has been created: " + createdInstanceId);
     System.out.println("Instance is associated with key: " + ins.getKeyName());
    }
    
    // Wait for the created instance to be fully provisioned.
    System.out.println(
        "Waiting for the instance we just created to be fully " +
        "provisioned...");
    DescribeInstanceStatusRequest describeInstanceRequest =
        new DescribeInstanceStatusRequest().withInstanceIds(createdInstanceId);
    DescribeInstanceStatusResult describeInstanceResult =
        ec2.describeInstanceStatus(describeInstanceRequest);
    List<InstanceStatus> state = describeInstanceResult.getInstanceStatuses();
    while (state.size() < 1 ||
        !state.get(0).getInstanceState().getName().equals("running")) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      describeInstanceResult = ec2.describeInstanceStatus(describeInstanceRequest);
      state = describeInstanceResult.getInstanceStatuses();
    }
    System.out.println("Instance has been fully provisoined");
    
    return createdInstanceId;
  }
  
  public void createInstanceTag(String createdInstanceId, String tag)
      throws AmazonServiceException {
    List<String> resources = new LinkedList<String>();
    List<Tag> tags = new LinkedList<Tag>();
    Tag nameTag = new Tag("Name", tag);
   
    resources.add(createdInstanceId);
    tags.add(nameTag);
   
    CreateTagsRequest ctr = new CreateTagsRequest(resources, tags);
    ec2.createTags(ctr);
  }

  public String createVolume() throws AmazonServiceException {
    // Create a volume.
    CreateVolumeRequest cvr = new CreateVolumeRequest();
    cvr.setAvailabilityZone("us-east-1a");
    cvr.setSize(5); // size = 5 gigabytes
    CreateVolumeResult volumeResult = ec2.createVolume(cvr);
    String createdVolumeId = volumeResult.getVolume().getVolumeId();
    return createdVolumeId;
  }

  public void attachVolumes(String instanceId, ArrayList<String> volumeIds)
      throws AmazonServiceException {
    // Attached device's naming starts from /dev/sdf.
    char c = 'f';
    for(String volumeId: volumeIds) {
      AttachVolumeRequest avr = new AttachVolumeRequest();
      avr.setVolumeId(volumeId);
      avr.setInstanceId(instanceId);
      avr.setDevice("/dev/sd" + c);
      ec2.attachVolume(avr);
      System.out.println("****Attached volume " + volumeId + " to " + instanceId + "****");
      c++;
    }
  }

  public void detachVolumes(String instanceId, ArrayList<String> volumeIds) {
    DetachVolumeRequest dvr = new DetachVolumeRequest();
    dvr.setInstanceId(instanceId);
    for(String volumeId: volumeIds) {
      dvr.setVolumeId(volumeId);
      ec2.detachVolume(dvr);
      System.out.println("Detach volume " + volumeId);
    }
  }

  public String createAmi(String instanceId, String username) {
    CreateImageRequest cir = new CreateImageRequest();
    cir.setInstanceId(instanceId);
    cir.setName(username + "-" + new Random().nextInt());
    CreateImageResult createImageResult = ec2.createImage(cir);
    String createdImageId = createImageResult.getImageId();
    System.out.println("Ami created with id " + createdImageId);
    return createdImageId;
  }

  public String createElasticIp() {
    AllocateAddressResult elasticResult = ec2.allocateAddress();
    String elasticIp = elasticResult.getPublicIp();
    return elasticIp;
  }

  public void associateElasticIp(String instanceId, String elasticIp) {
    AssociateAddressRequest aar = new AssociateAddressRequest();
    aar.setInstanceId(instanceId);
    aar.setPublicIp(elasticIp);
    ec2.associateAddress(aar);
  }

  public void dissociateElasticIp(String elasticIp) {
    System.out.println("Disassociate IP " + elasticIp);
    DisassociateAddressRequest dar = new DisassociateAddressRequest();
    dar.setPublicIp(elasticIp);
    ec2.disassociateAddress(dar);
  }

  public String snapshot(String instanceId, String amiName) {
    CreateImageRequest cir = new CreateImageRequest();
    cir.setInstanceId(instanceId);
    cir.setName(amiName);
    CreateImageResult createImageResult = ec2.createImage(cir);
    String createdImageId = createImageResult.getImageId();
    return createdImageId;
  }

  public void terminateInstance(String instanceId) {
    System.out.println("Stop the Instance " + instanceId + " ...");
    List<String> instanceIds = new LinkedList<String>();
    instanceIds.add(instanceId);
    // Stop instance.
    StopInstancesRequest stopIR = new StopInstancesRequest(instanceIds);
    ec2.stopInstances(stopIR);
    // Terminate instance.
    System.out.println("Terminate the Instance " + instanceId + " ...");
    TerminateInstancesRequest tir = new TerminateInstancesRequest(instanceIds);
    ec2.terminateInstances(tir);
  }
}
