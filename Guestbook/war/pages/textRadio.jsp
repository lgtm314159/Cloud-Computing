<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.restfb.FacebookClient" %>
<%@ page import="com.restfb.DefaultFacebookClient" %>
<%@ page import="com.restfb.types.User" %>
<%@ page import="com.restfb.Connection" %>
<%@ page import="com.restfb.types.Post" %>


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
Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);
int size = myFriends.getData().size();


%>

<p>Count of my friends: <%=size %></p>






<h1>Hello</h1>
<h1><%=accessToken%></h1>

</body>
</html>