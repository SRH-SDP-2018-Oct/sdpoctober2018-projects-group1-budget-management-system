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
	public void forgotPasswordDbConnection(String email,String recoveryAnswer) {
		
		try {
			String password;
			String reenteredPassword;
			Scanner scanner = new Scanner(System.in);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bms_schema", "root","root");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT email,recovery_answer FROM ACCOUNT");
			while(rs.next()){
				String emailId= rs.getString("email");
				if(emailId.equals(email)) {
					String securityAnswer= rs.getString("recovery_answer");
					if(securityAnswer.equals(recoveryAnswer)) {
						do {			
						System.out.println("Please enter your new password:");
						password=scanner.nextLine();
						System.out.println("Please reenter your password");
						reenteredPassword=scanner.nextLine();
						if(!password.equals(reenteredPassword)) {
							System.out.println("Your password doesn't match. Please enter again");
						}
						}
						while(!password.equals(reenteredPassword));
						
						//PreparedStatement updatestmt= con.prepareStatement("UPDATE ACCOUNT SET password = '"+reenteredPassword+"' WHERE email='"+emailId+"'");
						Statement updatestmt = con.createStatement();
						updatestmt.execute("UPDATE ACCOUNT SET password = '"+reenteredPassword+"' WHERE email='"+emailId+"'");
						//updatestmt.setString(1,reenteredPassword);
						//updatestmt.executeQuery();
						updatestmt.close();
						
						scanner.close();
					
					}
				}
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
