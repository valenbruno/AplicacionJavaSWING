package connector;

import java.sql.*;
public class MyConnector {
	 private static Connection con = null;
	 private static String usr = "";
	 private static String psw = "";
	 
	 public static Connection getConnection() { 
		 if(con == null) {
			 try {
				 con = DriverManager.getConnection("jdbc:mysql://localhost/tokyo2021_e3", usr, psw);
			 } 
			 catch (SQLException e) {
				 System.out.println("Error de SQL: "+e.getMessage());
			 }
		 }
		 return con;
	 }
	 
	 public static void closeConnection() throws SQLException {
		 con.close();
	 }
	 
	 public static Connection setConnection(String usr, String psw) {
		 MyConnector.usr = usr;
		 MyConnector.psw = psw;
		 return getConnection();
	 }
}
