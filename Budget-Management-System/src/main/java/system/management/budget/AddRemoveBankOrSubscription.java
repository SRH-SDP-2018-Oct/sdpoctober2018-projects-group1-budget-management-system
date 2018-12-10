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
		System.out.println("\n");
		System.out.println("*******************  Menu Options  *******************");
		System.out.println(" 1 : Add Bank Account				  				  ");
		System.out.println(" 2 : Add Subscription				  				  ");
		System.out.println(" 3 : Delete Bank Account		  	  				  ");
		System.out.println(" 4 : Delete Subscription			  				  ");
		System.out.println(" 5 : Go Back to Dashboard			  				  ");
		BudgetPortal.printSeparator(55);
		System.out.println("Go For :");
		Scanner scan = new Scanner(System.in);
		int choice = scan.nextInt();
		BudgetPortal.printSeparator(55);
	
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
					
		default : 
			System.out.println("Invalid Input");
			menu(account_id, username);
		}
		scan.close();
	}
	
	public static void getUserBankDetails(int account_id, String username) {
		System.out.println("Add a new bank account\n");
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter your IBAN Number : ");
		String iban = scanner.next();
		
		System.out.print("\nEnter your bank name : ");
		String bank_name = scanner.next();
		try {
			System.out.print("\nEnter your Account Balance euros: ");
			float balance = scanner.nextFloat();
			boolean rtnValue = checkIban(account_id, iban, balance, bank_name);
			BudgetPortal.printSeparator(55);
			if (rtnValue) {
				System.out.print("\nSuccesfully added account!\nIBAN: "+ iban + "\nBalance: EUROS "+ balance +"\n");
				BudgetPortal.viewDashboard(account_id, username);
			} else {
				System.out.print("Please Try again with a different IBAN.\n");
				scanner.reset();
				getUserBankDetails(account_id, username);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.print("******* Invalid input *******\n");
			getUserBankDetails(account_id, username);
		}
	}
	
	public static void getUserSubscriptionDetails(int account_id, String username) {
		
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.print("\nEnter the name of the subscription (ie Netflix): ");
			String subName = scanner.next();
			
			System.out.print("\nEnter start date in format [DD-MM-YYYY]: ");	
			String startDateString = scanner.next();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date1 = sdf1.parse(startDateString);
			java.sql.Date startDateSQL = new java.sql.Date(date1.getTime());
			
			System.out.print("\nEnter start end in format [DD-MM-YYYY]: ");
			String endDateString = scanner.next();
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date2 = sdf2.parse(endDateString);
			java.sql.Date endDateSQL = new java.sql.Date(date2.getTime());
			
			String valid = "(?:(?:31(-)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(-)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(-)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
			//String year = "(\\d{4})$";
			if(startDateString.matches(valid) == false | endDateString.matches(valid) == false ) {
				System.out.print("******* Invalid input *******\n");
				getUserSubscriptionDetails(account_id, username);
			}

			boolean rtnValue = addSubscription(account_id, subName, startDateSQL, endDateSQL); //This is duplicated in the addbank function
			BudgetPortal.printSeparator(55);
			if (rtnValue) {
				System.out.print("Succesfully added subscription!\nName: "+ subName + "\nFrom: "+ startDateSQL + " to "+ endDateSQL);
				BudgetPortal.viewDashboard(account_id, username);
			} else {
				System.out.print("Error adding account.\n");
			}
			BudgetPortal.printSeparator(55);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("******* Invalid input *******\n");
			getUserSubscriptionDetails(account_id, username);
		}
	}
	
	public static boolean checkIban(int account_id, String iban, float balance, String bank_name) {
		try {
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery(db.bankSel);
			boolean temp = rs.next();
			
			if (temp == false) {
				return addBank(account_id, iban, balance, bank_name);
			} else {
				rs.beforeFirst();
				while (rs.next()) {
					String ibanCheck = rs.getString("iban_num");
					if (ibanCheck.equals(iban)) {
						System.out.println("** ERROR: Can't add same acount multiple times **");
						return false;
					}
				}
				return addBank(account_id, iban, balance, bank_name);	
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean addBank(int account_id, String iban, float balance, String bank_name) {
		try {
			PreparedStatement pStmnt = con.prepareStatement(db.bankAdd);
			pStmnt.setString(1, iban);
			pStmnt.setFloat(2, balance);
			pStmnt.setInt(3, account_id);
			pStmnt.setString(4, bank_name);
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
		
		System.out.println("Select which account you would like to delete: \n ");
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
			System.out.print("******* Invalid input *******\n");
			removeBank(account_id, username);
		}
		return false;
	}
	
	public static boolean removeSubscription(int account_id, String username) {
		
		System.out.println("Select which subscription you would like to delete: \n ");
		try {
			Statement qStmt = con.createStatement();
			ResultSet rs = qStmt.executeQuery(db.subCheck + account_id);
			boolean deleted = listGenerator(rs,false,account_id,username);
			BudgetPortal.printSeparator(55);
			if (deleted == true) {
				System.out.print("Successfully removed subscription!\n");
				BudgetPortal.viewDashboard(account_id, username);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean listGenerator(ResultSet rs, boolean bank, int account_id, String username) {
		try {
			List<String> tempList = new ArrayList<String>();
			List<String> bankNameList = new ArrayList<String>();
			while (rs.next()) {
				if (bank == true) {
					tempList.add(rs.getString("iban_num"));
					bankNameList.add(rs.getString("bank_name"));
				} else {
					tempList.add(rs.getString("subscription_name"));
				}
			}
			int counter = 0;
			for (int i=0; i<  tempList.size(); i++ ) {
				counter++;
				System.out.print(counter+") " + tempList.get(i)+" - ");
				if (bank == true) {
					System.out.print(bankNameList.get(i)+"\n");
				}
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
	
	public static boolean deleter(boolean bank, int account_id, String username, List<String> list) {
		try{
			Scanner scanner = new Scanner(System.in);
			System.out.print("\nSelection : ");
			int choice = scanner.nextInt();
			if (choice == list.size()+1) {
				BudgetPortal.viewDashboard(account_id, username);
			}
			String toBeDeleted = list.get(choice-1);
			System.out.print("\n Are you sure you want to delete the account: " + toBeDeleted +"\n (Y or N) : ");
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
				System.out.print("******* Invalid input *******\n");
				removeSubscription(account_id, username);
			}
		return false;
		}

}//Class