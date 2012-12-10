<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.restfb.FacebookClient" %>
<%@ page import="com.restfb.DefaultFacebookClient" %>
<%@ page import="com.restfb.types.User" %>
<%@ page import="com.restfb.types.CategorizedFacebookType" %>
<%@ page import="com.restfb.Connection" %>
<%@ page import="com.restfb.types.Post" %>
<%@ page import="com.restfb.Parameter" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.lang.*" %>

<%@ page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String accessToken = request.getParameter("accessToken");
FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
Date oneWeekAgo = new Date(System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 1L)/4);
Connection<Post> myFeed = facebookClient.fetchConnection("me/home", Post.class,Parameter.with("type", "user"),
	    Parameter.with("since", oneWeekAgo));
Connection<User> targetedSearch = facebookClient.fetchConnection("me/home", User.class, Parameter.with("type", "user"));
Connection<User> myself = facebookClient.fetchConnection("me/feed",User.class);


%>

<% 
String msg = "";
String pp ="";
for (List<Post> myFeedConnectionPage : myFeed)
  for (Post post : myFeedConnectionPage){
	  msg = post.getMessage(); 
	  pp =	post.getFrom().getName();
	  if(msg!=null ){
%>
   <p><%=pp%> said <%=msg%></p>
    <% }} %>
    

</body>
</html>