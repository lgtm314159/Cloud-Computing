<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <div id="fb-root"></div>
  <script>
    // Additional JS functions here
    window.fbAsyncInit = function() {
      FB.init({
        appId      : '118371754989184', // App ID
        channelUrl : '//xjytestapp.appspot.com/channel.html', // Channel File
        status     : true, // check login status
        cookie     : true, // enable cookies to allow the server to access the session
        xfbml      : true  // parse XFBML
      });
    
      FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
          // connected
          document.getElementById('fb-logout').style.display = 'block';
        } else if (response.status === 'not_authorized') {
          // not_authorized
          login();
        } else {
          // not_logged_in
          login();
        }
       });
  
      // Additional init code here
  
    };

    function logout() {
      FB.logout(function(response) {
          console.log('User is now logged out');
      });
    }
  
    // Load the SDK Asynchronously
    (function(d){
       var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
       if (d.getElementById(id)) {return;}
       js = d.createElement('script'); js.id = id; js.async = true;
       js.src = "//connect.facebook.net/en_US/all.js";
       ref.parentNode.insertBefore(js, ref);
     }(document));
  </script>

  <div id="login" class="fb-login-button" data-show-faces="true" data-width="200" data-max-rows="1" data-size="xlarge"></div>
  <button id="fb-logout" onclick="logout()">Log out</button>
