package system.management.budget.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;
import org.apache.log4j.Logger;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class GenarateCustomReportDAOImpl {

	Connection con = null;

	DatabaseConnect jdbcObj = new DatabaseConnect();

	DataSource dataSource = null;
	
	final static Logger logger = Logger.getLogger(GenarateCustomReportDAOImpl.class);

	public void generateReport(java.sql.Date customDateReportTo, java.sql.Date customDateReportFrom, int currentAccountId) {
		try {
			dataSource = jdbcObj.setUpPool();

			con = dataSource.getConnection();
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM TRANSACTIONS WHERE TRANSACTION_DATE BETWEEN  '"+ customDateReportTo +"' and '"+ customDateReportFrom +"' and account_id ='"+ currentAccountId +"'ORDER BY transaction_date DESC, transaction_time DESC" );
			
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
			JasperViewer.viewReport(jp,false);
			stmt.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}	finally {
			try {
				if(con!=null) {
					con.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}