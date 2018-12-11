package system.management.budget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import system.management.budget.connections.DatabaseConnect;
import system.management.budget.valueObjects.*;

public class BankDetails implements TransactionDetails {

	final static Logger logger = Logger.getLogger(BankDetails.class);
	
	Connection con = null;
	
	DatabaseConnect jdbcObj = new DatabaseConnect();
	
	DataSource dataSource = null;
	
	public boolean getTransactions(int currentAccountId ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
		try {
				dataSource = jdbcObj.setUpPool();
    		
				con = dataSource.getConnection();
				
				Statement qStmt = con.createStatement();
				qStmt.execute("Select DISTINCT b.bank_name, b.iban_num, t.* FROM Transactions t INNER JOIN ( SELECT bank.* FROM Bank, Transactions WHERE bank.account_id ='"+ currentAccountId +"' )b ON t.bank_id = b.bank_id ORDER BY t.transaction_date DESC, t.transaction_time DESC");
				ResultSet rs = qStmt.getResultSet();
		
				while(rs.next()) 
				{
					trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("bank_name"),rs.getString("iban_num"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"));
					foundTransactions.add(trans_row);	
				}
		
			return showTransactions(foundTransactions);
		
		}catch(Exception e) {
			logger.error("Exception : "+e);
		}finally {
			try {
				if(con!=null) {
					con.close();
				}
			}catch(Exception e) {
				logger.error("Exception : "+e);
			}
		}
		
		return false;
	}
	
	public boolean getTransactionByDate(int currentAccountId, int MonthSelected , String YearSelected ) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
			try {
					dataSource = jdbcObj.setUpPool();
	    		
	    			con = dataSource.getConnection();
	    			
					Statement qStmt = con.createStatement();
					qStmt.execute("Select DISTINCT b.bank_name, b.iban_num, t.* FROM Transactions t INNER JOIN (SELECT bank.* FROM BANK,Transactions WHERE transactions.account_id= bank.account_id = '"+ currentAccountId +"' )b ON t.bank_id = b.bank_id WHERE Month(t.transaction_date) ='" + MonthSelected + "'AND Year(t.transaction_date) ='" + YearSelected + "'");
					ResultSet rs = qStmt.getResultSet();
		
					while(rs.next()) 
					{
						trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("bank_name"),rs.getString("iban_num"),rs.getString("transaction_type"),rs.getDouble("transaction_amount"));
						foundTransactions.add(trans_row);	
					}
		
				return showTransactions(foundTransactions);
		
			}
			catch(Exception e) {
				logger.error("Exception : "+e);
			}finally {
				try {
					if(con!=null) {
						con.close();
					}
				}catch(Exception e) {
					logger.error("Exception : "+e);
				}
			}
	
			return false;
	
		}
	
	
	public boolean showTransactions(List <DashboardVO> foundTransactions) {
		try {
			System.out.print("TRANSACTIONS : \n \n");
			if (foundTransactions.isEmpty())
			{
				System.out.println("Transactions not available ");
			}	
			else {
					System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.printf("%10s %15s %20s %30s %20s %20s %10s %10s %10s", "TIME", "DATE", "NAME", "MERCHANT","BANK NAME", "IBAN", "TYPE","AMOUNT","CURRENCY");
					System.out.println();	
					System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					for (int i = 0; i < foundTransactions.size() ; i++ ) {
						System.out.format("%10s %15s %20s %30s %20s %20s %10s %10s %10s",
						foundTransactions.get(i).getTransaction_time(), foundTransactions.get(i).getTransaction_date(), foundTransactions.get(i).getTransaction_name(), foundTransactions.get(i).getMerchant_name(), foundTransactions.get(i).getBank_name(), foundTransactions.get(i).getIban_num(), foundTransactions.get(i).getTransaction_type(),foundTransactions.get(i).getTransaction_amount(),"EUR" );
						System.out.println();
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
					}
					System.out.println("\n");

				}
			}
			catch(Exception e) {
				logger.error("Exception : "+e);	
			}
		return false;	
	}

}
