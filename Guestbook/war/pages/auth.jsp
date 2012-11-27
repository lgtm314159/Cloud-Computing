<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.google.gson.Gson" %>

<%
String appId = "118371754989184";
String appSecret = "a785b19b316494e82d70ab4ab93147ef";
String query = "client_id=" + appId + "&client_secret=" + appSecret +
    "&grant_type=client_credentials";
String authUrl = "https://graph.facebook.com/oauth/access_token?" + query;

URL url1 = new URL(authUrl);
BufferedReader in = new BufferedReader(new InputStreamReader(url1.openStream()));
String res = "";
String inputLine;
while ((inputLine = in.readLine()) != null) {
  res += inputLine;
}
in.close();
String accessToken = res.split("=")[1];
pageContext.setAttribute("accessToken", accessToken);
%>

<p> The accessToken is ${accessToken} </p>

<%
// Sample test that uses the access token to retrieve the app's information.
String graphUrl = "https://graph.facebook.com/app?access_token=" +
    URLEncoder.encode(accessToken, "UTF-8");
URL url2 = new URL(graphUrl);
in = new BufferedReader(new InputStreamReader(url2.openStream()));
res = "";
while ((inputLine = in.readLine()) != null) {
  res += inputLine;
}
in.close();

System.out.println(res);
Gson gson = new Gson();
Map<String, String> appInfo = gson.fromJson(res, Map.class);
Iterator it = appInfo.entrySet().iterator();
while (it.hasNext()) {
  Map.Entry pair = (Map.Entry)it.next();
  pageContext.setAttribute("key", pair.getKey());
  pageContext.setAttribute("value", pair.getValue());  
  %>
  <p>${key} is ${value}</p>
  <%
}
%>
