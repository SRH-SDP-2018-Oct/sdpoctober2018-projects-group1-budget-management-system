package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.*;


import org.apache.log4j.Logger;

import system.management.budget.valueObjects.DashboardVO;

public class DashboardDaoImpl {

	public boolean getTransaction(String currentAccountId) {
		DashboardVO trans_row;
		List <DashboardVO> foundTransactions = new ArrayList<DashboardVO>();
	try {
		
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMS_schema", "root","root1234");
		Statement s = con.createStatement();
		s.execute("Select * FROM Transactions WHERE account_id='"+ currentAccountId +"'");
		ResultSet rs = s.getResultSet();
		
		
			while(rs.next()) 
			{
				trans_row = new DashboardVO(rs.getString("transaction_time"),rs.getDate("transaction_date"),rs.getString("transaction_name"),rs.getString("merchant_name"),rs.getString("transaction_type"),rs.getFloat("transaction_amount"),rs.getString("currency"));
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
			for (int i = 0; i < foundTransactions.size() ; i++ ) {
				System.out.println("-------------------------------------------------------");
		    	System.out.println("Row " + i +" : " + foundTransactions.get(i).getTransaction_time() + "/" + foundTransactions.get(i).getTransaction_date() + "/" + foundTransactions.get(i).getTransaction_name() + "/" + foundTransactions.get(i).getMerchant_name() + "/" + foundTransactions.get(i).getTransaction_type() + "/" + foundTransactions.get(i).getTransaction_amount() + "/" + foundTransactions.get(i).getCurrency());
			}
		}
		catch(Exception e) {
			
			System.out.println("Error" + e);
			
		}
	return false;
		
	}
	
}
