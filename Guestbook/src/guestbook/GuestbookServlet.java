package guestbook;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;

import javax.servlet.http.*;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class GuestbookServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();

      if (user != null) {
          //resp.setContentType("text/plain");
          //resp.getWriter().println("Hello, " + user.getNickname());
        resp.sendRedirect("/pages/guestbook.jsp");
      } else {
        resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
      }
  }
  
}
