package JWPL.parser_tutorial;

import java.sql.DriverManager;

import java.sql.SQLException;



import com.mysql.jdbc.Connection;

public class DBManager {

	private static Connection connection = null;
	private static String driver = "com.mysql.jdbc.Driver";
    private static  String ind="jdbc:mysql://localhost:3306/wikiquote";
	
	public static void connect() {
		try {
			// Load JDBC/MySQL driver
			Class.forName(driver).newInstance();
			connection =  (Connection) DriverManager.getConnection(ind, "root", "15081985");
			System.out.println("Connect Database Succesfully");
		} catch (Exception ex) {
			System.out
					.println("Si è verificato un errore durante la connessione: "
							+ ex.toString());
			ex.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException ex) {
			System.out.println("Errore di chiusura connesione: "
					+ ex.toString());
			ex.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}
