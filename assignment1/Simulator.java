package assignment1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;

public class Simulator {
  public static void main(String[] args) {
    try {
      AWSCredentials credentials = new PropertiesCredentials(
          Simulator.class.getResourceAsStream("AwsCredentials.properties"));
      ArrayList<Employee> employees = createEmployees();
      HashMap<String, ArrayList<Integer>> policy = createPolicy();
      InstanceManager instManager = new InstanceManager(employees, policy,
          credentials);
      instManager.initialize();
      System.out.println("cleaning up after work...");
      instManager.afterWorkCleanup();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static HashMap<String, ArrayList<Integer>> createPolicy() {
    HashMap<String, ArrayList<Integer>> policy =
        new HashMap<String, ArrayList<Integer>>();
    ArrayList<Integer> tcpPorts = new ArrayList<Integer>();
    tcpPorts.add(22);
    tcpPorts.add(80);
    tcpPorts.add(443);
    policy.put("tcp", tcpPorts);
    return policy;
  }
  
  public static ArrayList<Employee> createEmployees() {
    ArrayList<Employee> employees = new ArrayList<Employee>();
    Employee employee1 = new Employee("employee1", "superuser");
    employee1.setKeyPairName("employee1-keypair");
    Employee employee2 = new Employee("employee2", "user");
    employee2.setKeyPairName("employee2-keypair");
    employees.add(employee1);
    employees.add(employee2);
    return employees;
  }
}
