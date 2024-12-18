package main.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/update")
public class update extends HttpServlet {

	// Reuse database connection details from Server.java
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mycat";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Ansh7905$";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		// Retrieve form parameters
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String message = req.getParameter("message");

		// Debugging output
		System.out.println("Updating record with name: " + name);
		System.out.println("New email: " + email);
		System.out.println("New message: " + message);

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection to the database
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			// Prepare SQL statement to update the database
			String sql = "UPDATE contacts SET name=?, message=? ,email =? WHERE name=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, message);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4,name);

			// Execute the SQL update
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				pw.println("<h1>Data successfully updated!</h1>");
			} else {
				pw.println("<h1>No record found to update.</h1>");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			pw.println("<h1>Error: " + e.getMessage() + "</h1>");
		} finally {
			// Close resources to prevent memory leaks
			try {
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

