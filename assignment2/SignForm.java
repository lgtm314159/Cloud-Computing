package assignment2

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class SignForm {
  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    String policy_document =
        "{\"expiration\": \"2013-12-31T00:00:00Z\"," +
          "\"conditions\": [" +
            "{\"bucket\": \"rameezjunyang\"}," +
            "[\"starts-with\", \"$key\", \"uploads/\"]," +
            "{\"acl\": \"public-read\"}," +
            "{\"success_action_redirect\": \"http://ec2-23-22-40-168.compute-1.amazonaws.com:8080/display.jsp\"}," +
            "[\"starts-with\", \"$Content-Type\", \"\"]," +
          "]" +
        "}";
    // Calculate policy and signature values from the given policy document and AWS credentials.
    String policy = new String(
        Base64.encodeBase64(policy_document.getBytes("UTF-8")), "ASCII");
    System.out.println(policy);
    String aws_secret_key = "<YOUR_AWS_SECRET_KEY>";
    Mac hmac = Mac.getInstance("HmacSHA1");
    hmac.init(new SecretKeySpec(
        aws_secret_key.getBytes("UTF-8"), "HmacSHA1"));
    String signature = new String(
        Base64.encodeBase64(hmac.doFinal(policy.getBytes("UTF-8"))), "ASCII");
    System.out.println(signature);
  }
}

