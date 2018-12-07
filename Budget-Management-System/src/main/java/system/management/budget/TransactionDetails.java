package system.management.budget;

import java.util.List;

import system.management.budget.valueObjects.DashboardVO;

public interface TransactionDetails {
	public boolean getTransactions(int currentAccountId);
}
