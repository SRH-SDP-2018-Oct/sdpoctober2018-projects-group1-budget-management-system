package system.management.budget.connections;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DatabaseConnect {
		
	
	static Properties prop = new Properties();
    static InputStream input = null;
	// JDBC Driver Name & Database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/BMS_Schema";

	// JDBC Database Credentials

	static final String JDBC_USER = "root";

	static final String JDBC_PASS = "root";
	
	private static GenericObjectPool gPool = null;


	@SuppressWarnings("unused")

	public DataSource setUpPool() throws Exception {

		Class.forName(JDBC_DRIVER);

		// Creates an Instance of GenericObjectPool That Holds Our Pool of Connections
		// Object!

		gPool = new GenericObjectPool();

		gPool.setMaxActive(10);

		// Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create
		// the Connection Object!

		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

		// Creates a PoolableConnectionFactory That Will Wraps the Connection Object
		// Created by the ConnectionFactory to Add Object Pooling Functionality!

		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);

		return new PoolingDataSource(gPool);

	}

	public GenericObjectPool getConnectionPool() {

		return gPool;

	}
	 
	
	    // This Method Is Used To Print The Connection Pool Status
	
	    /*public void printDbStatus() {
	
	        System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: " + getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	
	    }*/
		//Select queries
		public String accSel = "SELECT * FROM Account"; 
		public String bankSel = "SELECT * FROM Bank";
		public String bankCheck = "SELECT * FROM Bank WHERE account_id = ";
		public String subCheck = "SELECT * FROM Subscriptions WHERE account_id = ";
		public String forgotPasswordCheck = "SELECT email,recovery_answer FROM Account";
		//Insert queries
		public String bankAdd = "INSERT INTO Bank (iban_num,balance,account_id,bank_name) VALUES (?,?,?,?)";
		public String subAdd = "INSERT INTO Subscriptions (subscription_name,subscription_start_date,subscription_end_date,account_id) VALUES (?,?,?,?)";
		public String accountAdd = "INSERT INTO Account (email,first_name,last_name,sex,password,recovery_answer) VALUES (?,?,?,?,?,?)";
		public String transactionsAdd = "INSERT INTO transactions (account_id,bank_id,transaction_name,transaction_type,transaction_amount,transaction_date,Transaction_time,merchant_name) VALUES (?,?,?,?,?,?,?,?)";
		
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
