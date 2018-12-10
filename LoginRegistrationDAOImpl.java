package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


import org.apache.log4j.Logger;

import system.management.budget.BudgetPortal;
import system.management.budget.valueObjects.UserRegistrationVO;


public class LoginRegistrationDAOImpl {
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public int createConnection(String username,String pass){
		
    	try {
    		
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Account");
			while(rs.next()) {
				String name = rs.getString("email");
				if (username.equals(name)) {
					String password = rs.getString("password");
					if(pass.equals(password)) 
					{	
						int currentAccountId = rs.getInt("account_id");
						return currentAccountId;
						
					}
						
				}
				
			}
			stmt.close();
			System.out.println("Database connection success");
    	} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
    }
	
	public boolean registrationDbConnection(UserRegistrationVO userDetails){

		try {
			
			PreparedStatement stmt = con.prepareStatement(db.accountAdd);
			stmt.setString(1, userDetails.getEmailID());
			stmt.setString(2, userDetails.getFirstName());
			stmt.setString(3, userDetails.getLastName());
			stmt.setString(4, userDetails.getGender());
			stmt.setString(5, userDetails.getPassword());
			stmt.setString(6, userDetails.getRecoveryAnswer());
			stmt.execute();
			stmt.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean forgotPasswordDbConnection(String email,String recoveryAnswer) {
		
		try {
			String password;
			String reenteredPassword;
			Scanner scanner = new Scanner(System.in);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMS_Schema", "root","root");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT email,recovery_answer FROM Account");
			while(rs.next()){
				String emailId= rs.getString("email");
				if(emailId.equals(email)) {
					String securityAnswer= rs.getString("recovery_answer");
					if(securityAnswer.equals(recoveryAnswer)) {
						do {		
							System.out.println("\nPlease enter your new password : ");
							password=scanner.nextLine();
							BudgetPortal.goBackToPortal(password);
							System.out.println("\nPlease reenter your password : ");
							reenteredPassword=scanner.nextLine();
							if(!password.equals(reenteredPassword)) {
								System.out.println("\nYour password doesn't match. Please enter again. \n");
							}
						}
						while(!password.equals(reenteredPassword));
						
							Statement updatestmt = con.createStatement();
							updatestmt.execute("UPDATE Account SET password = '"+reenteredPassword+"' WHERE email='"+emailId+"'");
							System.out.println("\nYour password has been updated , Please Login again.\n");
							updatestmt.close();
						
					
						return true;
					}
					else {
						System.out.println("\nYour answer doesn't match. Please enter again. \n");
						return false;
					}
				}
					
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
