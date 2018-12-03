package system.management.budget;

import java.util.Scanner;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;  


public class AddRemoveBankOrSubscription {

	static Scanner scanner = new Scanner(System.in);
	
	private static Connection dbConnect(){
		Connection conFail = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMS_Schema", "root","root");
			return con;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conFail;
	}
	
	public static void getUserBankDetails(int account_id, Scanner scanner) {
		//System.out.println(String.join("", Collections.nCopies(50,"-")));
		System.out.println("Add a new bank account\n");
		
		System.out.print("Enter your IBAN Number: ");
		String iban = scanner.next();
		
		System.out.print("\nEnter your Account Balance euros: ");
		float balance = scanner.nextFloat();
		scanner.close();
		
		boolean rtnValue = checkIban(account_id, iban, balance);
		if (rtnValue) {
			System.out.print("\nSuccesfully added account!\nIBAN: "+ iban + "\nBalance: EUROS "+ balance +"\n");
		} else {
			System.out.print("Error adding account.\n");
		}
	}
	
	public static void getUserSubscriptionDetails(int account_id, Scanner scanner) {
		
		try {
			System.out.print("Enter the name of the subscription (ie Netflix): ");
			String subName = scanner.next();
			
			System.out.print("Enter start date in format [DD-MM-YYYY]: "); //Can code be condensed using for-loop and same vars
			String startDateString = scanner.next();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date1 = sdf1.parse(startDateString);
			java.sql.Date startDateSQL = new java.sql.Date(date1.getTime());
			
			System.out.print("Enter start end in format [DD-MM-YYYY]: ");
			String endDateString = scanner.next();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date2 = sdf2.parse(endDateString);
			java.sql.Date endDateSQL = new java.sql.Date(date2.getTime());	
			
			scanner.close();

			boolean rtnValue = addSubscription(account_id, subName, startDateSQL, endDateSQL); //This is duplicated in the addbank function
			
			if (rtnValue) {
				System.out.print("\nSuccesfully added subscription!\nName: "+ subName + "\nFrom: "+ startDateSQL + " to "+ endDateSQL);
			} else {
				System.out.print("Error adding account.\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkIban(int account_id, String iban, float balance) {
		try {
			Connection con = dbConnect();
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery("SELECT * FROM Bank");
			boolean temp = rs.next();
			
			if (temp == false) {
				return addBank(account_id, iban, balance, con);
			} else {
				while (temp) {			
					String ibanCheck = rs.getString("iban_num");
					if (ibanCheck.equals(iban)) {
						System.out.println("** ERROR: Can't add same acount multiple times **");
						getUserBankDetails(account_id,scanner);
					} else {
						return addBank(account_id, iban, balance, con);
					}
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean addBank(int account_id, String iban, float balance, Connection con) {
		try {
			PreparedStatement pStmnt = con.prepareStatement("INSERT INTO Bank (iban_num,balance,account_id) VALUES (?,?,?)");
			pStmnt.setString(1, iban);
			pStmnt.setFloat(2, balance);
			pStmnt.setInt(3, account_id);
			pStmnt.execute();
			pStmnt.close();
			return true;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addSubscription(int account_id, String subName, java.sql.Date startDateSQL, java.sql.Date endDateSQL) {
		try {
			Connection con = dbConnect();
			
			PreparedStatement pStmnt = con.prepareStatement("INSERT INTO Subscriptions (subscription_name,subscription_start_date,subscription_end_date,account_id) VALUES (?,?,?,?)");
			pStmnt.setString(1, subName);
			pStmnt.setDate(2, startDateSQL);
			pStmnt.setDate(3, endDateSQL);
			pStmnt.setInt(4, account_id);
			pStmnt.execute();
			return true;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean removeBank(int account_id, Scanner scanner) {
		
		System.out.println("Select which account you would like to delete: ");
		try {
			Connection con = dbConnect();
			
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery("SELECT * FROM Bank WHERE account_id = "+ account_id);
			List<String> ibanList = new ArrayList<String>();
			while (rs.next()) {
				ibanList.add(rs.getString("iban_num"));
			}
			int counter = 0;
			for (int i=0; i<  ibanList.size(); i++ ) {
				counter++;
				System.out.print(counter+") " + ibanList.get(i)+"\n");
			}

			System.out.print("Selection: ");
			int chooseAccount = scanner.nextInt();
			
			String accountToDelete = ibanList.get(chooseAccount-1);
			
			System.out.print("Are you sure you want to delete the account with IBAN: " + accountToDelete +"\n(Y or N): ");
			String confirmation = scanner.next();
			
			if (confirmation.equals("Y") || confirmation.equals("y")) {
				PreparedStatement pStmnt = con.prepareStatement("DELETE FROM BMS_Schema.Bank WHERE account_id = (?) AND iban_num = (?)");
				pStmnt.setInt(1, account_id);
				pStmnt.setString(2, accountToDelete);
				pStmnt.execute();
				scanner.close();
				System.out.print("Successfully removed bank account!");
				return true;
			} else {
				removeBank(account_id, scanner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeSubscription(int account_id, Scanner scanner) {
		
		System.out.println("Select which subscription you would like to delete: ");
		try {
			Connection con = dbConnect();
			
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery("SELECT * FROM Subscriptions WHERE account_id = "+ account_id);
			List<String> subList = new ArrayList<String>();
			while (rs.next()) {
				subList.add(rs.getString("subscription_name"));
			}
			int counter = 0;
			for (int i=0; i<  subList.size(); i++ ) {
				counter++;
				System.out.print(counter+") " + subList.get(i)+"\n");
			}
			
			System.out.print("Selection: ");
			int chooseSubscription = scanner.nextInt();
			
			String subscriptionToDelete = subList.get(chooseSubscription-1);
			
			System.out.print("Are you sure you want to delete the Subscripiton: " + subscriptionToDelete +"\n(Y or N): ");
			String confirmation = scanner.next();
			
			if (confirmation.equals("Y") || confirmation.equals("y")) {
				PreparedStatement pStmnt = con.prepareStatement("DELETE FROM BMS_Schema.Subscriptions WHERE account_id = (?) AND subscription_name = (?)");
				pStmnt.setInt(1, account_id);
				pStmnt.setString(2, subscriptionToDelete);
				pStmnt.execute();
				scanner.close();
				System.out.print("Successfully removed subscription!");
				return true;
			} else {
				removeSubscription(account_id, scanner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}


