import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReportServlet
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReportServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String contact = request.getParameter("contact");
		String issue_box = request.getParameter("issue_box");
		
		Connection connection = null;
		String insertSQL = "INSERT INTO report_issues (id, username, contact, issue) VALUES (default, ?, ?, ?)";

		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection; 
			PreparedStatement preparedStmt = connection.prepareStatement(insertSQL);
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, contact);
			preparedStmt.setString(3, issue_box);
			preparedStmt.execute();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
	    }	
		
		// Set response content type 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Thank you for your feedback!";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out.println(docType + //
	        "<html>\n" + //
	        "<head><title>" + title + "</title></head>\n" + //
	        "<body bgcolor=\"#f0f0f0\">\n" + //
	        "<h2 align=\"center\">" + title + "</h2>\n" + //
	        "<p> Your feedback has been recorded. Thank you for your time.<p></br>" + //
	        "<p> Go back to <a href=\"home.html\">Home Page</a> <p>");

		out.println("</body></html>");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
