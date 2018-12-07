package system.management.budget.connections;


import system.management.budget.valueObjects.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;


public class DashboardDaoImpl {
	
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public boolean getTransaction(int currentAccountId ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
		try {
				Statement qStmt = con.createStatement();
				qStmt.execute("Select DISTINCT b.bank_name, b.iban_num, t.* FROM Transactions t INNER JOIN ( SELECT bank.* FROM Bank, Transactions WHERE bank.account_id ='"+ currentAccountId +"' )b ON t.bank_id = b.bank_id ORDER BY t.transaction_date DESC, t.transaction_time DESC");
				ResultSet rs = qStmt.getResultSet();
		
				while(rs.next()) 
				{
					trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("bank_name"),rs.getString("iban_num"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"),rs.getString("currency"));
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
			
			if (foundTransactions.isEmpty())
			{
				System.out.println("No Transaction added yet ");
			}	
			else {
					System.out.print("TRANSACTIONS : \n \n");
			
					System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.printf("%10s %15s %20s %30s %20s %20s %10s %10s %10s", "TIME", "DATE", "NAME", "MERCHANT","BANK NAME", "IBAN", "TYPE","AMOUNT","CURRENCY");
					System.out.println();	
					System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					for (int i = 0; i < foundTransactions.size() ; i++ ) {
						System.out.format("%10s %15s %20s %30s %20s %20s %10s %10s %10s",
						foundTransactions.get(i).getTransaction_time(), foundTransactions.get(i).getTransaction_date(), foundTransactions.get(i).getTransaction_name(), foundTransactions.get(i).getMerchant_name(), foundTransactions.get(i).getBank_name(), foundTransactions.get(i).getIban_num(), foundTransactions.get(i).getTransaction_type(),foundTransactions.get(i).getTransaction_amount(),foundTransactions.get(i).getCurrency()  );
						System.out.println();
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					}
					System.out.println("\n");

				}
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
					qStmt.execute("Select bank_name, iban_num, balance FROM Bank WHERE account_id='"+ currentAccountId +"'");
					ResultSet rs = qStmt.getResultSet();
		
					while(rs.next()) 
					{
						balance_row = new BankVO(rs.getString("bank_name"),rs.getString("iban_num"),rs.getFloat("balance"));
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
			
			if (foundBalances.isEmpty())
			{
		    	System.out.println("No Bank Account added yet ");
			}
			else {
				System.out.print("CURRENT ACCOUNT BALANCES : \n \n");
				System.out.println("------------------------------------------------------------------------");
				System.out.printf("%20s %20s %20s", "NAME", "IBAN", "BALANCE");
				System.out.println();
				System.out.println("------------------------------------------------------------------------");
		   
				for (int i = 0; i < foundBalances.size() ; i++ ) {
					System.out.format("%20s %20s %20s",foundBalances.get(i).getBank_name(), foundBalances.get(i).getIban_num(), foundBalances.get(i).getBalance() );
					System.out.println();
				    System.out.println("------------------------------------------------------------------------");
				}
				System.out.println("\n");
			}
			}
			catch(Exception e) {
				System.out.println("Error" + e);	
			}
		return false;	
	}
	
	public boolean getTransactionByDate(int currentAccountId, int MonthSelected , int YearSelected ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
			try {
					Statement qStmt = con.createStatement();
					qStmt.execute("Select * FROM Transactions WHERE account_id='"+ currentAccountId +"'AND Month(transaction_date) ='" + MonthSelected + "'AND Year(transaction_date) ='" + YearSelected + "'");
					ResultSet rs = qStmt.getResultSet();
		
					while(rs.next()) 
					{
						trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("bank_name"),rs.getString("iban_num"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"),rs.getString("currency"));
						foundTransactions.add(trans_row);	
					}
		
				return showTransactions(foundTransactions);
		
			}
			catch(Exception e) {
				System.out.println("Error" + e);
			}
	
			return false;
	
		}
	
	public boolean getSubscritpions(int account_id ) {
		SubscriptionVO subscription_row;
		List <SubscriptionVO> foundSubscriptions = new ArrayList<SubscriptionVO>();
			try {
				Statement qStmt = con.createStatement();
				ResultSet rs = qStmt.executeQuery(db.subCheck + account_id);
		
					while(rs.next()) 
					{
						subscription_row = new SubscriptionVO(rs.getInt("subscription_id"),rs.getString("subscription_name"),rs.getDate("subscription_start_date"),rs.getDate("subscription_end_date"));
						foundSubscriptions.add(subscription_row);	
					}
		
				return showSubscriptions(foundSubscriptions);
		
			}
			catch(Exception e) {
				System.out.println("Error" + e);
			}
	
			return false;
	
		}
	public boolean showSubscriptions(List <SubscriptionVO> showSubscriptions) {
		try {
				System.out.println("\n  SUBSCRIPTIONS :\n ");
				if (showSubscriptions.isEmpty())
				{
					System.out.println("No Subscriptions added yet ");
				}
				else {
						System.out.println("--------------------------------------------------------------");
						System.out.printf("%20s %20s %20s", "SUBSCRIPTION NAME", "START DATE", "EXPIRES ON");
						System.out.println();	
						System.out.println("--------------------------------------------------------------");
		
						for (int i = 0; i < showSubscriptions.size() ; i++ ) {
							System.out.format("%20s %20s %20s", showSubscriptions.get(i).getSubscription_name(), showSubscriptions.get(i).getSubscription_start_date(), showSubscriptions.get(i).getSubscription_end_date()  );
							System.out.println();
							System.out.println("--------------------------------------------------------------");
						}
						System.out.println("\n");
				}
			}
			catch(Exception e) {
				System.out.println("Error" + e);	
			}
		return false;	
	}
	
	
	public JFreeChart createMonthlySpendingBarChart (int currentAccountId){
		DefaultCategoryDataset empty_dataset = new DefaultCategoryDataset();
		
		JFreeChart empty= ChartFactory.createBarChart("empty","empty","empty", empty_dataset,PlotOrientation.VERTICAL, false, true, false);
		try {
			
//			Statement qStmt = con.createStatement();
//			ResultSet rs = qStmt.executeQuery("SELECT SUM(transaction_amount) AS Amount, month(transaction_date) FROM Transactions WHERE account_id = '" + currentAccountId + "' GROUP BY month(transaction_date)");
//			 final CategoryDataset barDataset =
//			            new JDBCCategoryDataset( databaseAccess.getOracleDbConnection(),
//			                                     QUERY_SALARY_PER_FINANCE_EMPLOYEE );
//			
//			barChart = ChartFactory.createBarChart (Monthly Spending,
//													Months,
//													Total Amount,
//													barDataset,
//													aOrientation,
//													true,
//													true,
//													false);
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        Statement qStmt = con.createStatement();
	        ResultSet rs = qStmt.executeQuery("SELECT SUM(transaction_amount) AS Amount, monthname(transaction_date) AS Months FROM Transactions WHERE account_id = '" + currentAccountId + "' GROUP BY Months");
	        
	        while (rs.next()) {
	        	Float amount = rs.getFloat("Amount");
	        	String months = rs.getString("Months");	        
	            dataset.addValue(amount, "Amount", months);
	        }
	        JFreeChart chart = ChartFactory.createBarChart("Monthly Spending", "Months", "Amount", dataset, PlotOrientation.VERTICAL, false, true, false);
	        return chart;
	        
//			Save Chart in a specific Folder :	        
//	        int width =640;
//	        int height =480;
//	        File BarChart = new File("/Users/hamzasaib/desktop/output_chart_png");
//	        ChartUtilities.saveChartAsPNG(BarChart,chart,width,height); 
	        
	     
		}
		catch (Exception e) {
			System.out.println("Error" + e);
		}
		
		return empty;
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
}

