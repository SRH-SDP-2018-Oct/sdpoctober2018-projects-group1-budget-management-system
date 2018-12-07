package system.management.budget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import system.management.budget.connections.DatabaseConnect;
import system.management.budget.valueObjects.SubscriptionVO;

public class SubscriptionDetails implements TransactionDetails {

	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	public boolean getTransactions(int account_id ) {
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
		
				return showTransactions(foundSubscriptions);
		
			}
			catch(Exception e) {
				System.out.println("Error" + e);
			}
	
			return false;
	
		}
	public boolean showTransactions(List <SubscriptionVO> showSubscriptions) {
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
	}}
