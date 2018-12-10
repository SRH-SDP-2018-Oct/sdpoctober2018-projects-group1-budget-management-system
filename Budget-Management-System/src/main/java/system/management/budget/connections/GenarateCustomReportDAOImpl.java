package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class GenarateCustomReportDAOImpl {
	
	public void generateReport(java.sql.Date customDateReportTo,java.sql.Date customDateReportFrom){
		try {
			ResultSet rs = null;
				
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bms_schema", "root","root");
			
			Statement stmt = con.createStatement();
			
			rs = stmt.executeQuery("SELECT * FROM TRANSACTIONS WHERE TRANSACTION_DATE BETWEEN  '"+ customDateReportFrom +"' and  '"+ customDateReportTo +"'");
			FastReportBuilder drb = new FastReportBuilder();
			DynamicReport dr = drb.addColumn("Merchant Name", "merchant_name", String.class.getName(), 30)
					.addColumn("Transaction Name", "transaction_name", String.class.getName(), 30)
					.addColumn("Type", "transaction_type", String.class.getName(), 30)
					.addColumn("Amount", "transaction_amount", String.class.getName(), 30)
					.addColumn("Name", "transaction_name", String.class.getName(), 30)
					.addColumn("Date", "transaction_date", Date.class.getName(), 30)
					.addColumn("Time", "transaction_time", String.class.getName(), 30)
					.setTitle("TRANSACTION STATEMENT").setSubtitle("This report was generated at " + new Date())
					.setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true).build();
			JRResultSetDataSource resultsetdatasource = new JRResultSetDataSource(rs); 
																						
			JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(),
					resultsetdatasource);
			JasperViewer.viewReport(jp); // finally display the report report
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}