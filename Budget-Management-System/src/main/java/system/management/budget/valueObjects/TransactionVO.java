package system.management.budget.valueObjects;

public class TransactionVO {
	private String transactionName;
	private String transactionType;
	private float transactionAmount;
	private String transactionTime;
	private String merchantName;
	private java.sql.Date transactionDate;
	private float updatedAccountBalance;
	
	public float getUpdatedAccountBalance() {
		return updatedAccountBalance;
	}
	public void setUpdatedAccountBalance(float updatedAccountBalance) {
		this.updatedAccountBalance = updatedAccountBalance;
	}
	public float getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public java.sql.Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(java.sql.Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
}
