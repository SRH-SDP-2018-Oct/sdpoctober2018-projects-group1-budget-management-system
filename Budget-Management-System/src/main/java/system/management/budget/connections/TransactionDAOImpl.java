package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import system.management.budget.BudgetPortal;
import system.management.budget.valueObjects.BankVO;
import system.management.budget.valueObjects.TransactionVO;

public class TransactionDAOImpl {
	
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	
	public void transactionsInitialized(int currentAccountId, String username) {
		
		List<BankVO> bankDetails = getUserBankId(currentAccountId);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nGo For :");
		int option=scanner.nextInt();		
		
		int bank_id= bankDetails.get(option-1).getBank_id();
		float balance = bankDetails.get(option-1).getBalance();
		
		TransactionVO transactionDetails = new TransactionVO();
		//Scanner scanner = new Scanner(System.in);
		BudgetPortal.printSeparator(55);
		System.out.println("\nPlease enter your transaction details :");
		System.out.println("\nTransaction Name - (ex: Shopping/Food/Banking/Party) :");
		transactionDetails.setTransactionName(scanner.next());
		
		String transactionTypeCreditDebit;
		try {
			System.out.println("\nTransaction Amount : ");
			transactionDetails.setTransactionAmount(scanner.nextFloat());
		}
		catch(Exception e) {
			System.out.println("Please enter a valid amount.");
			transactionsInitialized(currentAccountId, username);
		}
		System.out.println("\nTransaction Type - (Credit or Debit?) : ");	
		transactionTypeCreditDebit = scanner.next();
		

		if(transactionTypeCreditDebit.startsWith("D") || transactionTypeCreditDebit.startsWith("d"))
			transactionDetails.setUpdatedAccountBalance(balance-transactionDetails.getTransactionAmount());
		else if(transactionTypeCreditDebit.startsWith("C") || transactionTypeCreditDebit.startsWith("c")) {
			transactionDetails.setUpdatedAccountBalance(balance+transactionDetails.getTransactionAmount());
			System.out.println("\n Previous Balance   :   "+balance+" \n * Updated Balance :   "+transactionDetails.getUpdatedAccountBalance());
			}
		else
			System.out.println("\nUnable to recognize the transaction type. This transaction amount will not be reflected in your accounts total balance.");
		
		transactionDetails.setTransactionType(transactionTypeCreditDebit);
		System.out.println("\nTransaction Date - ([DD-MM-YYYY]) :");
		String transactionDate = scanner.next();
					
		try {
			SimpleDateFormat transactionDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date1 = transactionDateFormat.parse(transactionDate);
			transactionDetails.setTransactionDate(new java.sql.Date(date1.getTime()));
		} catch (ParseException e) {
			System.out.println("\nThe date entered is not in the correct format. Please reinitialize the transaction.");
			transactionsInitialized(currentAccountId, username);
			
		}	
		
		System.out.println("\nTransaction Time - (HH:MM) : ");
		transactionDetails.setTransactionTime(scanner.next());
		
		System.out.println("\nMerchant Name : ");
		transactionDetails.setMerchantName(scanner.next());
	
		addTransactionToDb(currentAccountId, bank_id, transactionDetails);
		System.out.println("\nDo you want to add another transaction? (Y / N) : ");
		if(scanner.next().equals("Y"))
			transactionsInitialized(currentAccountId, username);
		else
			BudgetPortal.viewDashboard(currentAccountId,username);
			
	}
	private List<BankVO> getUserBankId(int currentAccountId) {

		System.out.println("\nPlease enter the IBAN option number under which you want the transaction to be linked : ");
		List<BankVO> bankDetailsList = new ArrayList<BankVO>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Bank WHERE account_id='"+ currentAccountId +"'");
			
			//HashMap<Integer, String> bankDetails = new HashMap<Integer, String>();
			
			BankVO bankDetails;
			while(rs.next()) {
				if(rs.getInt("account_id")==currentAccountId) {
					bankDetails = new BankVO(rs.getInt("bank_id"),rs.getString("iban_num"),rs.getFloat("balance"));
					bankDetailsList.add(bankDetails);
				}
			}
			
			int counter = 0;
			for (int i=0; i<  bankDetailsList.size(); i++ ) {
				counter++;
				System.out.print("\n"+counter+":   IBAN : " + bankDetailsList.get(i).getIban_num()+"   Balance : "+bankDetailsList.get(i).getBalance()+"\n");
			}
			
		return bankDetailsList;
		}	
		 catch (Exception e) {
			
			e.printStackTrace();
		}
		return bankDetailsList;
	}
	public boolean addTransactionToDb(int currentAccountID, int bank_id, TransactionVO transactionDetails) {
		
		try {
			
			PreparedStatement stmt = con.prepareStatement(db.transactionsAdd);
			stmt.setInt(1, currentAccountID);
			stmt.setInt(2, bank_id);
			stmt.setString(3, transactionDetails.getTransactionName());
			stmt.setString(4, transactionDetails.getTransactionType());
			stmt.setFloat(5, transactionDetails.getTransactionAmount());
			stmt.setDate(6, transactionDetails.getTransactionDate());
			stmt.setString(7, transactionDetails.getTransactionTime());
			stmt.setString(8, transactionDetails.getMerchantName());
			
			stmt.execute();
			stmt.close();
			
			Statement stmtBalanceUpdate = con.createStatement();
			stmtBalanceUpdate.execute("UPDATE Bank SET balance ='"+ transactionDetails.getUpdatedAccountBalance() +"' WHERE bank_id='"+ bank_id +"'");
			stmtBalanceUpdate.close();
			
			return true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
}
