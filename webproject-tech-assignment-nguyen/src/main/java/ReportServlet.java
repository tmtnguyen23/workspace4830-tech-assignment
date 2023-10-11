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
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
	            "transitional//en\">\n"; //
		out.println(docType + //
	            "<html>\n" + //
	            "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + //  
	            "<body bgcolor=\"white\">\n" + //
	            "<link href=\"https://fonts.googleapis.com/css2?family=Caveat&family=Montserrat:wght@400;600&display=swap\" rel=\"stylesheet\">"//
	            +"<link rel=\"stylesheet\" href=\"style.css\"></head>");
		out.println("<body><header>" + //
				"<div class=\"logo-container\" style=\"display:flex\">" + //
				"<img src=\"logo-company.png\" alt=\"our company logo\" width=\"100px\" height = \"100px\">" + //
				"<h1> Laptop Pool Checkout System </h1>" + //
				"</div> </header>");
		out.println("<body> <nav> <ul>" + //
				"<li><a href=\"home.html\">Home</a></li>" + //
                "<li><a href=\"reserve.html\">Reserve a Laptop</a></li>" + //
                "<li><a href=\"issues.html\">Report Issue</a></li>" + //
                "<li><a href=\"contact.html\">Contact us</a></li>" + // 
				"</ul> </nav>");
		
		out.println("<p> Thank you for your feedback! We will work on it and reach out in 2 business days.<p></br>" + //
	        "<p> Go back to <a href=\"home.html\">Home Page</a> <p>");

		out.println("</body></html>");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
