package system.management.budget.valueObjects;
import java.util.*;


public class DashboardVO {
	private int account_id;
	private int bank_id;
	private int category_id;
	private int transaction_id;
	private String transaction_name;
	private double transaction_amount;
	private String merchant_name;
	private java.util.Date transaction_date;
	private String transaction_time;
	private String transaction_type;
	private String currency;
	private String iban;
	
	public DashboardVO(String transaction_time,Date transaction_date, String transaction_name,String merchant_name,String transaction_type,Double transaction_amount,String currency )
	{
	    this.transaction_time = transaction_time;
	    this.transaction_date = transaction_date;
	    this.transaction_name = transaction_name;
	    this.merchant_name = merchant_name;
	    this.transaction_type = transaction_type;
	    this.transaction_amount = transaction_amount;
	    this.currency = currency;
	}
	
	public DashboardVO(Date transaction_date, String transaction_name, double transaction_amount, String currency)
	{
		this.transaction_date = transaction_date;
		this.transaction_name = transaction_name;
		this.transaction_amount = transaction_amount;
	    this.currency = currency;
		
	}
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getBank_id() {
		return bank_id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getTransaction_name() {
		return transaction_name;
	}
	public void setTransaction_name(String transaction_name) {
		this.transaction_name = transaction_name;
	}
	public double getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(float transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public java.util.Date getTransaction_date() {
		return transaction_date;
	}
	public void setTransaction_date(java.util.Date transaction_date) {
		this.transaction_date = transaction_date;
	}
	public String getTransaction_time() {
		return transaction_time;
	}
	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	
	
}