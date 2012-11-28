<%@page import="java.io.*, 

java.util.*,java.io.IOException,java.io.PrintWriter,java.util.ArrayList,javax.servlet.http.HttpServletResponse,com.amazonaws.AmazonServiceException,com.amazonaws.auth.AWSCredentials, com.amazonaws.auth.PropertiesCredentials,com.amazonaws.services.s3.AmazonS3Client,com.amazonaws.services.s3.model.ObjectListing,com.amazonaws.services.s3.model.S3Object,com.amazonaws.services.s3.model.S3ObjectSummary"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
<script>

function uploadS3()
{

var fullPath = document.getElementById('file').value;
if (fullPath) {
var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
var filename = fullPath.substring(startIndex);
if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
        filename = filename.substring(1);
}
}

document.getElementById('key').value = 'uploads/' + filename;

}

</script>
</head>


<body>



<script type='text/javascript' src='https://d1bmnhp0r7jm2y.cloudfront.net/jwplayer.js'></script>  
<div id='mediaplayer'>This text will be replaced</div>

<% 

			AWSCredentials credentials = new PropertiesCredentials(
    			new File("AwsCredentials.properties"));

         try{
        	 AmazonS3Client s3Client = new AmazonS3Client(credentials);

        			 ObjectListing images = s3Client.listObjects("rameezjunyang"); 
        			 ArrayList<String> objectList = new ArrayList<String>();

        			 List<S3ObjectSummary> list = images.getObjectSummaries();
				int i=0;
				String container = "container";

        			for(S3ObjectSummary image: list) {
				    container = container+i;
				 i++;

        			     S3Object obj = s3Client.getObject("rameezjunyang", image.getKey());
        			     if(obj.getKey().contains(".mp4")){

						   %>
				<b><%= obj.getKey() %></b>
				<div id='<%=container%>'></div>
	<%

						String fileName = "'https://d1bmnhp0r7jm2y.cloudfront.net/"+obj.getKey()+"'"; %>

						<script type="text/javascript">
		   jwplayer('<%=container%>').setup({
		      'id': 'playerID',
		      'width': '200',
		      'height': '200',
		      'provider': 'rtmp',
		      'streamer': 'rtmp://s1ct8cza3d6c1u.cloudfront.net/cfx/st/',
		      'file':<%=fileName%>,
		      'autostart' : 'false',
		      'modes': [
		         {type: 'flash', src: 'https://d1bmnhp0r7jm2y.cloudfront.net/jwplayer.flash.swf'},
		      ]
		   });
		</script>
<% 		
        			   
	}   


}
        			
        			  s3Client.shutdown();
        			  

        	            
         } catch (Exception ase) {
                 
         }

	
		
%>	

		




    <form action="https://rameezjunyang.s3.amazonaws.com/" method="post" onsubmit="return uploadS3();" enctype="multipart/form-data">
      <input type="hidden" name="key" id="key" value="">
      <input type="hidden" name="AWSAccessKeyId" value="AKIAJ4OVGPCH56SJU25Q">
      <input type="hidden" name="acl" value="public-read">
      <input type="hidden" name="success_action_redirect" value="http://ec2-23-22-40-168.compute-1.amazonaws.com:8080/display.jsp">
      <input type="hidden" name="policy" value="eyJleHBpcmF0aW9uIjogIjIwMTMtMTItMzFUMDA6MDA6MDBaIiwiY29uZGl0aW9ucyI6IFt7ImJ1Y2tldCI6ICJyYW1lZXpqdW55YW5nIn0sWyJzdGFydHMtd2l0aCIsICIka2V5IiwgInVwbG9hZHMvIl0seyJhY2wiOiAicHVibGljLXJlYWQifSx7InN1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0IjogImh0dHA6Ly9lYzItMjMtMjItNDAtMTY4LmNvbXB1dGUtMS5hbWF6b25hd3MuY29tOjgwODAvZGlzcGxheS5qc3AifSxbInN0YXJ0cy13aXRoIiwgIiRDb250ZW50LVR5cGUiLCAiIl0sXX0=">
      <input type="hidden" name="signature" value="pSobqKmpD9RZXbKwkN67C0+bKj0=">
      <input type="hidden" name="Content-Type" value="">
      <!-- Include any additional input fields here -->

      File to upload to S3:
      <input name="file" type="file" id="file">
      <br>
      <input type="submit" value="Upload File to S3">
    </form>
	



</body>
</html>
