package system.management.budget.valueObjects;

public class BankVO {
	private int bank_id;
	private String iban_num;
	private float balance;
	private int account_id;
	private String bank_name;
	
	
	public BankVO(String bank_name,String iban_num, Float balance) {
		this.bank_name = bank_name;
		this.iban_num = iban_num;
		this.balance = balance;
	}
	
	public BankVO(int bank_id, String iban_num, float balance) {
		this.bank_id=bank_id;
		this.iban_num=iban_num;
		this.balance=balance;
	}
	
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public int getBank_id() {
		return bank_id;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public String getIban_num() {
		return iban_num;
	}
	public void setIban_num(String iban_num) {
		this.iban_num = iban_num;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	
	
	
}
