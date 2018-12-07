package system.management.budget.connections;


import system.management.budget.TransactionDetails;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;


public class DashboardDaoImpl {
	
	static DatabaseConnect db = new DatabaseConnect();
	static Connection con = db.dbConnect();
	
	private TransactionDetails transactionDetails;
	
	 public DashboardDaoImpl(TransactionDetails transactionDetails) {
	        this.transactionDetails = transactionDetails;
	    }

	public void budgetTransactionType(int currentAccountId) {
	        this.transactionDetails.getTransactions(currentAccountId);
	    }	
	
	public static JFreeChart createMonthlySpendingBarChart (int currentAccountId){
		DefaultCategoryDataset empty_dataset = new DefaultCategoryDataset();
		
		JFreeChart empty= ChartFactory.createBarChart("empty","empty","empty", empty_dataset,PlotOrientation.VERTICAL, false, true, false);
		try {
			
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
	        
//          Save Chart in a specific Folder :            
//	        int width =640;
//	        int height =480;
//	        
//	        File BarChart = new File("/Users/hamzasaib/desktop/output_chart_png");
//	        ChartUtilities.saveChartAsPNG(BarChart,chart,width,height); 
	        
	     
		}
		catch (Exception e) {
			System.out.println("Error" + e);
		}
		
		return empty;	
		
	}
	
}

