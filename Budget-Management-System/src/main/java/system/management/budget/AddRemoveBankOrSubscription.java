package system.management.budget;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import system.management.budget.connections.*;


public class AddRemoveBankOrSubscription {

	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public static void menu(int account_id, String username) {
		System.out.println(" **********  Menu Options  ********** ");
		System.out.println(" 1 : Add Bank Account				  ");
		System.out.println(" 2 : Add Subscription				  ");
		System.out.println(" 3 : Delete Bank Account		  	  ");
		System.out.println(" 4 : Delete Subscription			  ");
		System.out.println(" 5 : Previous	 					  ");
		System.out.println(" ************************************ ");
		Scanner scan = new Scanner(System.in);
		int choice = scan.nextInt();
	
		switch (choice){
		case 1 :
			getUserBankDetails(account_id, username);
			break;
		case 2:
			getUserSubscriptionDetails(account_id, username);
			break;
		case 3:
			removeBank(account_id, username);
			break;
		case 4:
			removeSubscription(account_id, username);
			break;
		case 5:
			BudgetPortal.viewDashboard(account_id,username);
			break;
					
		default : System.out.println("Invalid Input");
		}
		scan.close();
	}
	
	public static void getUserBankDetails(int account_id, String username) {
		System.out.println("Add a new bank account\n");
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter your IBAN Number: ");
		String iban = scanner.next();
		
		System.out.print("\nEnter your Account Balance euros: ");
		float balance = scanner.nextFloat();
		
		boolean rtnValue = checkIban(account_id, iban, balance);
		if (rtnValue) {
			System.out.print("\nSuccesfully added account!\nIBAN: "+ iban + "\nBalance: EUROS "+ balance +"\n");
			BudgetPortal.viewDashboard(account_id, username);
		} else {
			System.out.print("Please Try again with a different IBAN.\n");
			scanner.reset();
			getUserBankDetails(account_id, username);
		}
	}
	
	public static void getUserSubscriptionDetails(int account_id, String username) {
		
		try {
			Scanner scanner = new Scanner(System.in);
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

			boolean rtnValue = addSubscription(account_id, subName, startDateSQL, endDateSQL); //This is duplicated in the addbank function
			
			if (rtnValue) {
				System.out.print("\nSuccesfully added subscription!\nName: "+ subName + "\nFrom: "+ startDateSQL + " to "+ endDateSQL);
				BudgetPortal.viewDashboard(account_id, username);
			} else {
				System.out.print("Error adding account.\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkIban(int account_id, String iban, float balance) {
		try {
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery(db.bankSel);
			boolean temp = rs.next();
			
			if (temp == false) {
				return addBank(account_id, iban, balance);
			} else {
				rs.beforeFirst();
				while (rs.next()) {
					String ibanCheck = rs.getString("iban_num");
					if (ibanCheck.equals(iban)) {
						System.out.println("** ERROR: Can't add same acount multiple times **");
						return false;
					}
				}
				return addBank(account_id, iban, balance);	
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean addBank(int account_id, String iban, float balance) {
		try {
			PreparedStatement pStmnt = con.prepareStatement(db.bankAdd);
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
			PreparedStatement pStmnt = con.prepareStatement(db.subAdd);
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

	public static boolean removeBank(int account_id, String username) {
		
		System.out.println("Select which account you would like to delete: ");
		try {
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery(db.bankCheck + account_id);
			boolean deleted = listGenerator(rs,true,account_id,username);
			if (deleted == true) {
				System.out.print("Successfully removed bank account!\n");
				BudgetPortal.viewDashboard(account_id, username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeSubscription(int account_id, String username) {
		
		System.out.println("Select which subscription you would like to delete: ");
		try {
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery(db.subCheck + account_id);
			boolean deleted = listGenerator(rs,false,account_id,username);
			if (deleted == true) {
				System.out.print("Successfully removed subscription!\n");
				BudgetPortal.viewDashboard(account_id, username);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean listGenerator(ResultSet rs, boolean bank, int account_id, String username) {
		try {
			List<String> tempList = new ArrayList<String>();
			while (rs.next()) {
				if (bank == true) {
					tempList.add(rs.getString("iban_num"));
				} else {
					tempList.add(rs.getString("subscription_name"));
				}
			}
			int counter = 0;
			for (int i=0; i<  tempList.size(); i++ ) {
				counter++;
				System.out.print(counter+") " + tempList.get(i)+"\n");
			if (counter == tempList.size()) {
				System.out.print((counter+1)+") Back to Menu\n");
				}
			}
			return deleter(bank, account_id, username, tempList );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean deleter(boolean bank, int account_id, String username, List<String> list) {
		try{
			Scanner scanner = new Scanner(System.in);
			System.out.print("Selection: ");
			int choice = scanner.nextInt();
			if (choice == list.size()+1) {
				BudgetPortal.viewDashboard(account_id, username);
			}
			String toBeDeleted = list.get(choice-1);
			System.out.print("Are you sure you want to delete the account: " + toBeDeleted +"\n(Y or N): ");
			String confirmation = scanner.next();
			if (confirmation.equals("Y") == false && confirmation.equals("y") == false) {
				menu(account_id, username);
			}
			if (bank == true) {
				PreparedStatement pStmnt = con.prepareStatement(db.bankDel);
				pStmnt.setInt(1, account_id);
				pStmnt.setString(2, toBeDeleted);
				pStmnt.execute();
				return true;
			} else {
				PreparedStatement pStmnt = con.prepareStatement(db.subDel);
				pStmnt.setInt(1, account_id);
				pStmnt.setString(2, toBeDeleted);
				pStmnt.execute();
				return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
		}

}//Class


