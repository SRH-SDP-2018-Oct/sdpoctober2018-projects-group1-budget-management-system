package system.management.budget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import system.management.budget.connections.DatabaseConnect;
import system.management.budget.valueObjects.BankVO;

public class CurrentBalance implements TransactionDetails {

	final static Logger logger = Logger.getLogger(CurrentBalance.class);
	
	Connection con = null;

	DatabaseConnect jdbcObj = new DatabaseConnect();

	DataSource dataSource = null;

	public boolean getTransactions(int currentAccountId) {
		BankVO balance_row;
		List<BankVO> foundBalances = new ArrayList<BankVO>();
		try {
			dataSource = jdbcObj.setUpPool();

			con = dataSource.getConnection();
			Statement qStmt = con.createStatement();
			qStmt.execute("Select bank_name, iban_num, balance FROM Bank WHERE account_id='" + currentAccountId + "'");
			ResultSet rs = qStmt.getResultSet();

			while (rs.next()) {
				balance_row = new BankVO(rs.getString("bank_name"), rs.getString("iban_num"), rs.getFloat("balance"));
				foundBalances.add(balance_row);
			}

			return showCurrentBalance(foundBalances);

		} catch (Exception e) {
			logger.error("Exception : "+e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error("Exception : "+e);
			}
		}

		return false;
	}

	public boolean showCurrentBalance(List<BankVO> foundBalances) {
		try {
			System.out.print("\n");
			System.out.print(" DASHBOARD \n");
			BudgetPortal.printSeparator(55);
			if (foundBalances.isEmpty())
			{
		    	System.out.println("No Bank Account added yet ");
			}
			else {
				System.out.print("\n");
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
				logger.error("Exception : "+e);	
			}
		return false;	
	}

}
