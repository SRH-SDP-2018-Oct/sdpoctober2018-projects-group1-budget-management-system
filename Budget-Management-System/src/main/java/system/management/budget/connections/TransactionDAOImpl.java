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

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import system.management.budget.BudgetPortal;
import system.management.budget.valueObjects.BankVO;
import system.management.budget.valueObjects.CategoryVO;
import system.management.budget.valueObjects.TransactionVO;

public class TransactionDAOImpl {

	Connection con = null;
	
	DatabaseConnect jdbcObj = new DatabaseConnect();
	
	DataSource dataSource = null;
	
	final static Logger logger = Logger.getLogger(TransactionDAOImpl.class);
	
	public void transactionsInitialized(int currentAccountId, String username) {
		
		List<BankVO> bankDetails = getUserBankId(currentAccountId);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nGo For :");
		int option=scanner.nextInt();	
		BudgetPortal.printSeparator(55);
		scanner.nextLine();
		if(option == 0) {
			BudgetPortal.viewDashboard(currentAccountId,username);
		} else {
		
		int bank_id= bankDetails.get(option-1).getBank_id();
		float balance = bankDetails.get(option-1).getBalance();
		
		int userCategoryID = getUserCategory();
		BudgetPortal.printSeparator(55);

		
		TransactionVO transactionDetails = new TransactionVO();
		BudgetPortal.printSeparator(55);
		System.out.println("\nPlease enter your transaction details :");
		System.out.println("\nTransaction Name - (ex: Shopping/Food/Banking/Party) :");
		transactionDetails.setTransactionName(scanner.nextLine());
		
		String transactionTypeCreditDebit;
		try {
			System.out.println("\nTransaction Amount : ");
			transactionDetails.setTransactionAmount(scanner.nextFloat());
			scanner.nextLine();
		}
		catch(Exception e) {
			logger.error("Exception :"+e);
			System.out.println("Please enter a valid amount.");
			transactionsInitialized(currentAccountId, username);
		}
		System.out.println("\nTransaction Type - (Credit or Debit?) : ");	
		transactionTypeCreditDebit = scanner.nextLine();
		

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
		String transactionDate = scanner.nextLine();
					
		try {
			SimpleDateFormat transactionDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date1 = transactionDateFormat.parse(transactionDate);
			transactionDetails.setTransactionDate(new java.sql.Date(date1.getTime()));
		} catch (ParseException e) {
			logger.error("Exception :"+e);
			System.out.println("\nThe date entered is not in the correct format. Please reinitialize the transaction.");
			transactionsInitialized(currentAccountId, username);			
		}	
		
		System.out.println("\nTransaction Time - (HH:MM) : ");
		transactionDetails.setTransactionTime(scanner.nextLine());
		
		System.out.println("\nMerchant Name : ");
		transactionDetails.setMerchantName(scanner.nextLine());
	
		addTransactionToDb(currentAccountId, bank_id, transactionDetails, userCategoryID);
		System.out.println("\nDo you want to add another transaction? (Y / N) : ");
		if(scanner.next().equals("Y"))
			transactionsInitialized(currentAccountId, username);
		else
			BudgetPortal.viewDashboard(currentAccountId,username);
		}
			
	}
	private int getUserCategory() {
		try {
		dataSource = jdbcObj.setUpPool();
		
		con = dataSource.getConnection();
		
		Statement stmt = con.createStatement();

		ResultSet rsForCatagories = stmt.executeQuery("SELECT * FROM CATEGORY");
		CategoryVO category;
		List<CategoryVO> categoryList = new ArrayList<CategoryVO>();
		int option=0;
		boolean userCategorySelection=false;
			while (rsForCatagories.next()) {
				category = new CategoryVO(rsForCatagories.getInt("category_id"),
						rsForCatagories.getString("category_name"));
				categoryList.add(category);
			}
			System.out.println("Please select the category under which you want this transaction to be mapped.");
			for (int i = 0; i < categoryList.size(); i++) {
				System.out.print("\n" + categoryList.get(i).getCategoryId() + " :   "
						+ categoryList.get(i).getCategoryName() + "\n");
			}
			
			System.out.println("\nGo For :");
			Scanner scanner = new Scanner(System.in);
			
			
			while (!userCategorySelection) {
				option = scanner.nextInt();
				if (0 < option && option < 9) {
					userCategorySelection = true;
					return option;
				} else
					System.out.println("Please enter a valid category number");
			}
		} catch (Exception e) {
			logger.error("Exception :"+e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error("Exception :"+e);
			}
		}
		return 0;
	}
	private List<BankVO> getUserBankId(int currentAccountId) {

		System.out.println("\nPlease enter the IBAN option number under which you want the transaction to be linked : ");
		List<BankVO> bankDetailsList = new ArrayList<BankVO>();
		try {
			dataSource = jdbcObj.setUpPool();
    		
    		con = dataSource.getConnection();
    		
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Bank WHERE account_id='"+ currentAccountId +"'");

			
			BankVO bankDetails;
			while(rs.next()) {
				if(rs.getInt("account_id")==currentAccountId) {
					bankDetails = new BankVO(rs.getInt("bank_id"),rs.getString("iban_num"),rs.getFloat("balance"));
					bankDetailsList.add(bankDetails);
				}
			}
			
			int counter = 0;
			System.out.println("\n0 :   Go Back to Dashboard ");
			for (int i=0; i<  bankDetailsList.size(); i++ ) {
				counter++;
				System.out.print("\n"+counter+" :   IBAN : " + bankDetailsList.get(i).getIban_num()+"   Balance : "+bankDetailsList.get(i).getBalance()+" EUR\n");
			}
			BudgetPortal.printSeparator(55);
			return bankDetailsList;
		} catch (Exception e) {
			logger.error("Exception :"+e);
		}finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error("Exception :"+e);
			}
		}
		return bankDetailsList;
	}
	public boolean addTransactionToDb(int currentAccountID, int bank_id, TransactionVO transactionDetails, int userCategoryID) {
		
		try {
			dataSource = jdbcObj.setUpPool();
    		
    		con = dataSource.getConnection();
    		
			PreparedStatement stmt = con.prepareStatement(jdbcObj.transactionsAdd);
			stmt.setInt(1, currentAccountID);
			stmt.setInt(2, bank_id);
			stmt.setInt(3, userCategoryID);
			stmt.setString(4, transactionDetails.getTransactionName());
			stmt.setString(5, transactionDetails.getTransactionType());
			stmt.setFloat(6, transactionDetails.getTransactionAmount());
			stmt.setDate(7, transactionDetails.getTransactionDate());
			stmt.setString(8, transactionDetails.getTransactionTime());
			stmt.setString(9, transactionDetails.getMerchantName());
			
			stmt.execute();
			stmt.close();
			
			Statement stmtBalanceUpdate = con.createStatement();
			stmtBalanceUpdate.execute("UPDATE Bank SET balance ='"+ transactionDetails.getUpdatedAccountBalance() +"' WHERE bank_id='"+ bank_id +"'");
			stmtBalanceUpdate.close();
			
			return true;
			
		} catch (Exception e) {

			logger.error("Exception :"+e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error("Exception :"+e);
			}
		}
		return false;
		
	}
}
