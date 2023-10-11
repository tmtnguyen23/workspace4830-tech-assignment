import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		validateUser(username, password, response);
	}

	void validateUser(String username, String password, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
	            "transitional//en\">\n"; //
		out.println(docType + //
	            "<html>\n" + //
	            "<head></head>\n" + //
	            "<body bgcolor=\"white\">\n" + //
	            "<link href=\"https://fonts.googleapis.com/css2?family=Caveat&family=Montserrat:wght@400;600&display=swap\" rel=\"stylesheet\">"//
	            +"<link rel=\"stylesheet\" href=\"style.css\">");
		
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			
			String selectSQL = "SELECT * FROM employee_credentials WHERE username LIKE ? and password LIKE ?";
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				response.sendRedirect("index.html");
			} else {
				out.println("<p style='color:red'>Wrong username and password. Please try again <a href='login.html'>here.</a></p>");
			}
			rs.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     try {
		         if (preparedStatement != null) {
		            preparedStatement.close();
		         }
		      } catch (SQLException se2) {
		    	  se2.printStackTrace();
		      }
		      try {
		         if (connection != null) {
		            connection.close();
		         }
		      } catch (SQLException se) {
		         se.printStackTrace();
		      }
	      }
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
