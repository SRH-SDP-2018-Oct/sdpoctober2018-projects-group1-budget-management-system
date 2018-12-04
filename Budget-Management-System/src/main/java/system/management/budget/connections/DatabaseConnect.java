package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnect {
	
		//Select queries
		public String accSel = "SELECT * FROM Account"; 
		public String bankSel = "SELECT * FROM Bank";
		public String bankCheck = "SELECT * FROM Bank WHERE account_id = ";
		public String subCheck = "SELECT * FROM Subscriptions WHERE account_id = ";
		
		//Insert queries
		public String bankAdd = "INSERT INTO Bank (iban_num,balance,account_id) VALUES (?,?,?)";
		public String subAdd = "INSERT INTO Subscriptions (subscription_name,subscription_start_date,subscription_end_date,account_id) VALUES (?,?,?,?)";
		
		//Delete queries
		public String bankDel = "DELETE FROM BMS_Schema.Bank WHERE account_id = (?) AND iban_num = (?)";
		public String subDel = "DELETE FROM BMS_Schema.Subscriptions WHERE account_id = (?) AND subscription_name = (?)";

	
	public static Connection dbConnect(){
		Connection conFail = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMS_Schema", "root","root");
			return con;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conFail;
	}
}
