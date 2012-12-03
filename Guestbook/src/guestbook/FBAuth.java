package guestbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



@SuppressWarnings("serial")
public class FBAuth extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    String appId = "118371754989184";
    String appSecret = "a785b19b316494e82d70ab4ab93147ef";
    String redirectUrl = "http://localhost:8888/fbauth";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] state = md.digest("mystate".getBytes());
      String authUrl = "http://www.facebook.com/dialog/oauth/?" +
          "client_id=" + appId +
          "&redirect_uri=" + redirectUrl +
          "&scope=user_birthday,read_stream" +
          "&state=" + state;
      
      String code = req.getParameter("code");
      if (code == null) {
        resp.sendRedirect(authUrl);
      } else {
        String getTokenUrl = "https://graph.facebook.com/oauth/access_token?" +
            "client_id=" + appId +
            "&redirect_uri=" + redirectUrl +
            "&client_secret=" + appSecret +
            "&code=" + code;
        URL url = new URL(getTokenUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          res += inputLine;
        }
        in.close();
        System.out.println(res);
        String accessToken = res.split("=")[1].split("&expires")[0];
        System.out.println(accessToken);
      }
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
