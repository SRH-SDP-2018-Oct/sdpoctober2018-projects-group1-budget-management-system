package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import system.management.budget.valueObjects.UserRegistrationVO;

public class LoginRegistrationDAOImpl {
	//private static final Logger LOGGER = Logger.getLogger(LoginRegistrationDAOImpl.class);
	public String createConnection(String username,String pass){
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bms_schema", "root","root1234");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Account");
			while(rs.next()) {
				String name = rs.getString("email");
				if (username.equals(name)) {
					String password = rs.getString("password");
					if(pass.equals(password)) 
					{	
						String currentAccountId = rs.getString("account_id");
						return currentAccountId;
						
					}
						
				}
				
			}
			stmt.close();
			System.out.println("Database connection success");
    	} catch (Exception e) {
			e.printStackTrace();
		} 
		return "login failed";
    }
	
	public boolean registrationDbConnection(UserRegistrationVO regClass){
		boolean registrationResult = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bms_schema", "root","root1234");
			
			//Statement stmt = con.createStatement();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Account VALUES (?,?,?)");
			//String dbQuery= "INSERT INTO USER_REGISTRATION (firstname,lastname,name) VALUES ('" +regClass.getFirstName()+ "','"+regClass.getLastName()+"','"+regClass.getEmailID()+"');";
			stmt.setString(1, regClass.getFirstName());
			stmt.setString(2, regClass.getLastName());
			stmt.setString(3, regClass.getEmailID());
			registrationResult = stmt.execute();
			stmt.close();
			System.out.println(registrationResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registrationResult;
	}
}