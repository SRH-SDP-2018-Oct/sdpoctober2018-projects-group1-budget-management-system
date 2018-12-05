package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.*;


import org.apache.log4j.Logger;

import system.management.budget.valueObjects.*;

public class DashboardDaoImpl {
	
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public boolean getTransaction(int currentAccountId ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
		try {
			Statement qStmt = con.createStatement();
			qStmt.execute("Select * FROM Transactions WHERE account_id='"+ currentAccountId +"'");
			ResultSet rs = qStmt.getResultSet();
		
			while(rs.next()) 
			{
				trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"),rs.getString("currency"));
				foundTransactions.add(trans_row);	
			}
		
			return showTransactions(foundTransactions);
		
		}catch(Exception e) {
			System.out.println("Error" + e);
		}
		
	return false;
	}
	
	
	public boolean showTransactions(List <DashboardVO> foundTransactions) {
		try {
		
			System.out.println("----------------------------------------------------------------------------------------------------------");
		    System.out.printf("%10s %10s %20s %30s %10s %10s %10s", "TIME", "DATE", "NAME", "MERCHANT", "TYPE","AMOUNT","CURRENCY");
		    System.out.println();	
		    System.out.println("----------------------------------------------------------------------------------------------------------");
			for (int i = 0; i < foundTransactions.size() ; i++ ) {
				System.out.format("%10s %10s %20s %30s %10s %10s %10s",
						foundTransactions.get(i).getTransaction_time(), foundTransactions.get(i).getTransaction_date(), foundTransactions.get(i).getTransaction_name(), foundTransactions.get(i).getMerchant_name(), foundTransactions.get(i).getTransaction_type(),foundTransactions.get(i).getTransaction_amount(),foundTransactions.get(i).getCurrency()  );
		        System.out.println();
				System.out.println("----------------------------------------------------------------------------------------------------------");
			}
			System.out.println("----------------------------------------------------------------------------------------------------------");

		}
		catch(Exception e) {
			System.out.println("Error" + e);	
		}
	return false;	
	}
	
	public boolean getCurrentBalance(int currentAccountId) {
		BankVO balance_row;
		List <BankVO> foundBalances = new ArrayList<BankVO>();
			try {
					Statement qStmt = con.createStatement();
					qStmt.execute("Select iban_num, balance FROM Bank WHERE account_id='"+ currentAccountId +"'");
					ResultSet rs = qStmt.getResultSet();
		
					while(rs.next()) 
					{
						balance_row = new BankVO(rs.getString("iban_num"),rs.getFloat("balance"));
						foundBalances.add(balance_row);
					}
					
				return showCurrentBalance(foundBalances);
		
			}catch(Exception e) {
				System.out.println("Error" + e);
			}
			return false;	
	}
	
	public boolean showCurrentBalance(List <BankVO> foundBalances) {
		try {
			for (int i = 0; i < foundBalances.size() ; i++ ) {
					System.out.println("-------------------------------------------------------");
					System.out.println("Bank Account Number " + i +" : " + foundBalances.get(i).getIban_num() + "/" + foundBalances.get(i).getBalance() );
				}
			}
		catch(Exception e) {
			System.out.println("Error" + e);	
		}
	return false;	
	}
	
	public boolean getTransaction(int currentAccountId, int MonthSelected , int YearSelected ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
			try {
					Statement qStmt = con.createStatement();
					qStmt.execute("Select * FROM Transactions WHERE account_id='"+ currentAccountId +"'AND Month(transaction_date) ='" + MonthSelected + "'AND Year(transaction_date) ='" + YearSelected + "'");
					ResultSet rs = qStmt.getResultSet();
		
					while(rs.next()) 
					{
						trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"),rs.getString("currency"));
						foundTransactions.add(trans_row);	
					}
		
				return showTransactions(foundTransactions);
		
			}
			catch(Exception e) {
				System.out.println("Error" + e);
			}
	
			return false;
	
		}
	
}

