package system.management.budget.valueObjects;

import java.util.Date;

public class SubscriptionVO {
	private int subscription_id;
	private String subscription_name;
	private java.util.Date subscription_start_date;
	private java.util.Date subscription_end_date;
	
	public SubscriptionVO(int subscription_id, String subscription_name, Date subscription_start_date,
			Date subscription_end_date) {
		super();
		this.subscription_id = subscription_id;
		this.subscription_name = subscription_name;
		this.subscription_start_date = subscription_start_date;
		this.subscription_end_date = subscription_end_date;
	}

	public int getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(int subscription_id) {
		this.subscription_id = subscription_id;
	}

	public String getSubscription_name() {
		return subscription_name;
	}

	public void setSubscription_name(String subscription_name) {
		this.subscription_name = subscription_name;
	}

	public java.util.Date getSubscription_start_date() {
		return subscription_start_date;
	}

	public void setSubscription_start_date(java.util.Date subscription_start_date) {
		this.subscription_start_date = subscription_start_date;
	}

	public java.util.Date getSubscription_end_date() {
		return subscription_end_date;
	}

	public void setSubscription_end_date(java.util.Date subscription_end_date) {
		this.subscription_end_date = subscription_end_date;
	}	
	
}