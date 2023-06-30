package com.passengers;

import com.sravana.DBConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Booking.*;
import com.sravana.*;


@WebServlet("/SubmitPassengerDetail")
public class SubmitPassengerDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // MySQL database connection settings
   
   

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String Firstname = request.getParameter("Firstname");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
		Properties props = new Properties();
		props.load(in);
        String passengers_url = props.getProperty("passengers_url");
		String userid = props.getProperty("username");
		String password = props.getProperty("password");
        
        // Database connection and query
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO passenger_details (Firstname, name, age, gender, email, phone) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Register JDBC driver and establish database connection
            
            DBConnection dbConnection = new DBConnection(passengers_url, userid, password);
		    conn = dbConnection.getConnection();

            // Prepare the SQL statement
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Firstname);
            stmt.setString(2, name);
            stmt.setInt(3, age);
            stmt.setString(4, gender);
            stmt.setString(5, email);
            stmt.setString(6, phone);

            // Execute the query
            stmt.executeUpdate();

            // Display success message
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Passenger details saved successfully.</h1>");
            out.println("</body></html>");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Display error message
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.");
        } finally {
            // Close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
