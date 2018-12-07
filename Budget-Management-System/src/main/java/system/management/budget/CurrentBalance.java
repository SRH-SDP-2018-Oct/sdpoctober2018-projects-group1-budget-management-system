package system.management.budget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import system.management.budget.connections.DatabaseConnect;
import system.management.budget.valueObjects.BankVO;

public class CurrentBalance implements TransactionDetails {
	
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public boolean getTransactions(int currentAccountId) {
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

}
