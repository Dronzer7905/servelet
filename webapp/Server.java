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

// Define the servlet and map it to the URL pattern "/test"
@WebServlet("/test")
public class Server extends HttpServlet {

	// Database connection details
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mycat";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Ansh7905$";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Set response content type to HTML
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		// Retrieve form parameters
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String message = req.getParameter("message");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection to the database
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			// Prepare SQL statement to insert form data into the database
			String sql = "INSERT INTO contacts (name, email, message) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, message);

			// Execute the SQL update
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				// If insertion is successful, display success message
				pw.println("<h1>Data successfully inserted!</h1>");
			} else {
				// If insertion fails, display failure message
				pw.println("<h1>Failed to insert data.</h1>");
			}

		} catch (ClassNotFoundException | SQLException e) {
			// Handle exceptions and display error message
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
