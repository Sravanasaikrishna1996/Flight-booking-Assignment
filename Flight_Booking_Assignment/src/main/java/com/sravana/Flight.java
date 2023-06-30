package com.sravana;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sravana.DBConnection;

/**
 * Servlet implementation class Flight
 */
@WebServlet("/Flight")
public class Flight extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<html><body>");

		// STEP 2:
		InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
		Properties props = new Properties();
		props.load(in);

		String url = props.getProperty("url");
		String userid = props.getProperty("username");
		String password = props.getProperty("password");

		DBConnection dbConnection = null;

		try {

			dbConnection = new DBConnection(url, userid, password);
			Connection connection = dbConnection.getConnection();		
		
			// STEP 3: Create the Statement Object
			Statement stmt =  connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
			
					
			String source = request.getParameter("from");
			String destination = request.getParameter("to");
			
			out.println("<table><th>flight_name<th>sources<th>destinations<th>price</th><Book_Here>");
			
			if (source != null && !source.isEmpty() && destination != null && !destination.isEmpty())
			{
			    // STEP 4: Get the Results in ResultSet object
				String query = "SELECT * FROM flights WHERE sources='" + source + "' AND destinations='" + destination + "'";
				ResultSet resultSet = stmt.executeQuery(query);

				while (resultSet.next()) {
				    String bookHereLink = "<a href='" + resultSet.getString("Book_Here") + "'>" + resultSet.getString("Book_Here") + "</a>";

				    // Retrieve other flight information from the resultSet
				    String flightName = resultSet.getString("flight_name");
				    String sources = resultSet.getString("sources");
				    String destinations = resultSet.getString("destinations");
				    int price = resultSet.getInt("price");

				    // Generate HTML code dynamically
//				    String flightInfo = "Flight Name: " + flightName 
//				            + "Sources: " + sources + "<br>"
//				            + "Destinations: " + destinations + "<br>"
//				            + "Price: " + price + "<br>"
//				            + "Book Here: " + bookHereLink + "<br><br>";
				   // out.println(flightInfo);
			        out.println("<tr><td>" + flightName + "<td>" + sources + "<td>" + destinations + "<td>" + price + "<td>" + bookHereLink + "</tr>");
			        out.println();
				}

			    out.println("</table>");
			    out.println("</body></html>");
			}
			else if (source.isBlank() || destination.isBlank())
			{
				HttpSession session = request.getSession();
			    out.println("From and to cannot be empty.");
				response.sendRedirect("Error.html");
			}


		} catch (Exception e) {
			out.println(e);
		}
		
	}
	}

