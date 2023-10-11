

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReserveLaptop
 */
@WebServlet("/ReserveServlet")
public class ReserveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReserveServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String date_end = request.getParameter("date_return");
		informUser(firstname, lastname, date_end, response);
	}
	
	void informUser(String firstname, String lastname, String date_end, HttpServletResponse response) throws ServletException, IOException{
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
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		Connection connection = null;
		
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			
			String selectSQL = "SELECT laptop_name FROM laptops WHERE AVAILABLE LIKE 1";
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				String laptop_name = rs.getString("laptop_name").trim();
				out.println(laptop_name + " has been assigned to you. Please come to Room 123 and grab " + laptop_name + //
						" from the laptop cabinet and return it to the designated tray by 4pm " + date_end + //
						"</body></html>");
				String updateSQL = "UPDATE laptops SET AVAILABLE = 0 WHERE laptop_name LIKE ?";
				preparedStatement2 = connection.prepareStatement(updateSQL);
				preparedStatement2.setString(1,laptop_name);
				preparedStatement2.executeUpdate();
				
				String insertSQL ="INSERT INTO reservation_list (id, firstname, lastname, laptop, date_start, date_return) VALUES (default, ?, ?, ?, ?, ?)";
				preparedStatement3 = connection.prepareStatement(insertSQL);
				preparedStatement3.setString(1,firstname);
				preparedStatement3.setString(2,lastname);
				preparedStatement3.setString(3, laptop_name);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				preparedStatement3.setString(4, dateFormat.format(date));
				preparedStatement3.setString(5, date_end);
				preparedStatement3.execute();

			} else {
				out.println("<p style='color:red'>Sorry, there is no laptop available at this time. Please leave a feedback or contact us here.<a href='contact.html'>here.</a></p>");
			}
			rs.close();
			preparedStatement.close();
			preparedStatement2.close();
			preparedStatement3.close();
			connection.close();
		}	catch (SQLException se) {
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
