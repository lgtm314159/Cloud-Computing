<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.0/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/stylesheets/radio.css" />
  <link rel="stylesheet" href="/stylesheets/main.css" />
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.9.0/jquery-ui.js"></script>
  <script type="text/javascript" src="/js/jPlayer/js/jquery.jplayer.min.js"></script>
  <script>
    $(function() {
      $("#mainMenu").tabs({
        beforeLoad: function(event, ui) {
          ui.jqXHR.error(function() {
            ui.panel.html(
              "Couldn't load this page. We'll try to fix this as soon as " +
              "possible. If this wouldn't be a demo.");
          });
        }
      });
    });
  </script>
</head>
<body>
<div id="outerDiv">
  <div id="mainMenu">
    <div id="header">
    <h1> Facebook Radio </h1>
    <ul id="nav_tab">
      <li><a href="#tab-home" id="homeLink">Home</a></li>
      <li><a href="/pages/login.jsp" id="loginLink">Login</a></li>
      <li><a href="/pages/radio.jsp" id="radioLink">Radio</a></li>
      <li><a href="/pages/video.jsp" id="videoLink">Video</a></li>
      <li><a href="/pages/about.jsp" id="aboutLink">About</a></li>
    </ul>
    </div> <!-- end header -->

    <div id="tab-home">
      <h2 class="title">Welcome to Facebook Radio</a></h2>
      <div class="post">
        <p class="meta">
        <span class="date">November 11, 2012</span>
        <span class="poster">Posted by <a href="#">Someone</a></span>
        </p>
        <div style="clear: both;">&nbsp;</div>
        <div class="entry">
          <p>
            This is the best site that i have ever used. Just thought i would drop
            you a line to let u know what i thought. this is so much better than
            other sites.
          </p>
          <p class="links"><a href="#">Read More</a>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">Comments</a></p>
        </div>
        <p class="meta">
        <span class="date">November 11, 2012</span>
        <span class="poster">Posted by <a href="#">Someone</a></span>
        </p>
        <div style="clear: both;">&nbsp;</div>
        <div class="entry">
          <p>
            I love this app sooooooo much! Thank you guys!
          </p>
          <p class="links"><a href="#">Read More</a>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">Comments</a></p>
        </div>
        <p class="meta">
        <span class="date">November 11, 2012</span>
        <span class="poster">Posted by <a href="#">Someone</a></span>
        </p>
        <div style="clear: both;">&nbsp;</div>
        <div class="entry">
          <p>
            Best app ever!
          </p>
          <p class="links"><a href="#">Read More</a>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">Comments</a></p>
        </div>
      </div>
    </div>  <!-- end tab-home -->
  </div>

</div> <!-- end outerDiv -->

</body>
</html>