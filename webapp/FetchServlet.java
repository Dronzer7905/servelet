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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/fetch")
public class FetchServlet extends HttpServlet {

	// Database connection details
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mycat";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Ansh7905$";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection to the database
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			// Prepare SQL statement to fetch data
			String sql = "SELECT name, email, message FROM contacts";
			preparedStatement = connection.prepareStatement(sql);

			// Execute the query
			resultSet = preparedStatement.executeQuery();

			// Process the result set
			pw.println("<h1>Contact List</h1>");
			pw.println("<table border='1'><tr><th>Name</th><th>Email</th><th>Message</th></tr>");
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String message = resultSet.getString("message");
				pw.println("<tr><td>" + name + "</td><td>" + email + "</td><td>" + message + "</td></tr>");
			}
			pw.println("</table>");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			pw.println("<h1>Error: " + e.getMessage() + "</h1>");
		} finally {
			// Close resources to prevent memory leaks
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

