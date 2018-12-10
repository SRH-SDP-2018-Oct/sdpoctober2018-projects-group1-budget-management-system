package system.management.budget;

import java.io.Console;
import java.util.Scanner;


import system.management.budget.valueObjects.*;
import system.management.budget.connections.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class BudgetPortal 
{
	public static void main(String[] args) {
		clearScreen();
		proceedToPortal();
	}
	
	public static void proceedToPortal() {
		System.out.println(" Welcome to the BUDGET MANAGEMENT SYSTEM Portal ");
		printSeparator(55);
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("\n Select your Option ");
		System.out.println("\n1 : Login");
		System.out.println("\n2 : Register ");
		System.out.println("\n3 : Forgot your Password");
		printSeparator(55);
		System.out.println("Go For :");
		
		String loginRegistrationChoice=scanner.nextLine();
		LoginRegistrationDAOImpl con = new LoginRegistrationDAOImpl();
		
		if (loginRegistrationChoice.equals("1")) {
			userLogin(con);
		}
		else if(loginRegistrationChoice.equals("2")){
			userRegistration(con);
		}
		else if(loginRegistrationChoice.equals("3"))
			userForgotPassword(con);
		else {
			System.out.println("\nPlease enter a valid number.");
		}
		scanner.close();
	}

	public static void userRegistration(LoginRegistrationDAOImpl con) {
		Scanner scanner = new Scanner(System.in);
		UserRegistrationVO userDetails = new UserRegistrationVO();
		printSeparator(55);
		System.out.println("\n REGISTER");
		printSeparator(55);
		System.out.println("\nPlease provide the below metioned details to register. \n\nFIRST NAME:");
		userDetails.setFirstName(scanner.nextLine());
		
		System.out.println("\nLAST NAME:");
		userDetails.setLastName(scanner.nextLine());
		
		do{
			System.out.println("\nEMAIL ADDRESS:");
			userDetails.setEmailID(scanner.nextLine());
			
				if(!userDetails.getEmailID().contains("@") && !userDetails.getEmailID().contains("."))
					System.out.println("Please enter a valid email address.");
		}while(!userDetails.getEmailID().contains("@") && !userDetails.getEmailID().contains("."));
		
		
		System.out.println("\nGENDER");
		userDetails.setGender(scanner.nextLine());
		
		System.out.println("\nPASSWORD");
		userDetails.setPassword(scanner.nextLine());
		
		System.out.println("\nTO RETRIVE YOU ACCOUNT INCASE YOU FORGET YOUR PASSWORD, PLEASE ANSWER THE SECURITY QUESTION BELOW."
				+ "\n\nWhat is the last name of the teacher who failed you?:");
		userDetails.setRecoveryAnswer(scanner.nextLine());
	
		con.registrationDbConnection(userDetails);
		printSeparator(55);
		clearScreen();
		System.out.println("Registration successful !!! ... You may proceed to login.\n");
		proceedToPortal();
	}

	public static void userLogin(LoginRegistrationDAOImpl con) {
		Scanner scanner = new Scanner(System.in);
		printSeparator(55);
		System.out.println("\n LOGIN");
		printSeparator(55);
		System.out.println("\nEnter your Email-ID: ");
		String username = scanner.nextLine();

		Console c = System.console();
		String pass;
		System.out.println("\nEnter password: ");
		
		if(c!=null) {
			
			char[] ch = c.readPassword();
			pass = String.valueOf(ch);// converting char array into string
		}
		else {
			//Password input for eclipse console
			pass = scanner.nextLine();
		}

		int currentAccountId = con.createConnection(username, pass);
	
		if(currentAccountId != 0)
			viewDashboard(currentAccountId,username);
		else {
			System.out.println("Login failed.\n\n Either the Email-ID or password is incorrect. \n\nPlease enter the details again.");
			userLogin(con); //Recursive function.
		}
		//scanner.close();
	}
	
	public static void userForgotPassword(LoginRegistrationDAOImpl con) {

		Scanner scanner = new Scanner(System.in);
		printSeparator(55);
		System.out.println("\n FORGOT YOUR PASSWORD");
		printSeparator(55);
		System.out.println("Please enter your email address:");
		String email=scanner.nextLine();
		System.out.println("\n");
		System.out.println("What is the last name of the teacher who failed you?:");
		String answer=scanner.nextLine();
		
		con.forgotPasswordDbConnection(email, answer);
		proceedToPortal();	
	
	
	}
	
	private static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}
	
	public static void printSeparator(int count) {
		System.out.print("\n");
		for (int i=0; i<count; i++){
				System.out.print("*");
		}
		System.out.print("\n");
	}
	
	public static void viewDashboard(int currentAccountId, String username) {
		
		TransactionDAOImpl transactions = new TransactionDAOImpl();
		
		printSeparator(55);
		System.out.println("Welcome User :  " + username + "\n" );
		
		System.out.println("*******************  Menu Options  *******************");
		System.out.println("1 : Dashboard");
		System.out.println("2 : Add Or Delete - Bank Account/ Subscription");
		System.out.println("3 : Adding a Transaction");
		System.out.println("4 : Expense Tracker");
		System.out.println("5 : Notifications");
		System.out.println("6 : Reports");
		System.out.println("7 : Logout");
		printSeparator(55);
		System.out.println("Go For :");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		printSeparator(55);
		
		switch (choice) {
		case 1:
				System.out.println("Please Select the Type of Display" );
				System.out.println("1 : Display All");
				System.out.println("2 : Display by Month ");
                System.out.println("3 : Charts ");
            	printSeparator(55);
				System.out.println("Go For :");
				Scanner scan_1 = new Scanner(System.in);
				int display_type = scan_1.nextInt();
				printSeparator(55);
				switch (display_type) {
				
				case 1 : 
						DashboardDaoImpl dashboardViewForCurrentBalance  = new DashboardDaoImpl(new CurrentBalance());
						dashboardViewForCurrentBalance.budgetTransactionType(currentAccountId);
						
						DashboardDaoImpl dashboardViewForBank = new DashboardDaoImpl(new BankDetails());
						dashboardViewForBank.budgetTransactionType(currentAccountId);
						
						DashboardDaoImpl dashboardViewForSubscription = new DashboardDaoImpl(new SubscriptionDetails());
						dashboardViewForSubscription.budgetTransactionType(currentAccountId);
				
						break;
				 
				case 2 :  
						System.out.println("Please Select a Month (1 - 12) :");
				 		Scanner scan_month = new Scanner(System.in);
				 		int MonthSelected = scan_month.nextInt();
				 		
				 		System.out.println("Please Select a Year ( YYYY ) :");
				 		Scanner scan_year = new Scanner (System.in);
				 		int YearSelected = scan_year.nextInt();
				 		
				 		DashboardDaoImpl dashboardViewForCurrentBalance2  = new DashboardDaoImpl(new CurrentBalance());
				 		dashboardViewForCurrentBalance2.budgetTransactionType(currentAccountId);
				 		
				 		BankDetails transactionDetailsByDate = new BankDetails();
				 		transactionDetailsByDate.getTransactionByDate(currentAccountId, MonthSelected, YearSelected);
	 
						break;
						
                case 3 : 
                		System.out.println("Choose the Chart you want to display : ");
                		System.out.println("1 : Monthly Spending \n");
                		System.out.println("Go For :");
                		printSeparator(55);
                		Scanner scan_chart = new Scanner(System.in);        
                		int chart_selected = scan_chart.nextInt();
                		switch (chart_selected)  {
                
                        case 1 : JFreeChart chart = DashboardDaoImpl.createMonthlySpendingBarChart(currentAccountId);
                                 ChartPanel cp = new ChartPanel(chart);
                                 JFrame frame = new JFrame("Bar Chart");

                                 frame.setSize(600, 400);
                                 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                 frame.setVisible(true);
                                 frame.getContentPane().add(cp);
                                 BudgetPortal.viewDashboard(currentAccountId, username);
                                 break;
                                 
                        default : System.out.println("Invalid Input");
                		}

				default : System.out.println("Invalid Input");
			
				}
				BudgetPortal.viewDashboard(currentAccountId, username);
				scan_1.close();
				break;
				
		case 2: AddRemoveBankOrSubscription.menu(currentAccountId,username);
				break;
		
		case 3:	transactions.transactionsInitialized(currentAccountId,username);
			 	break;
			 	
		case 5: System.out.println("Notifications \n");
				NotificationsDAOImpl.getNotifications(currentAccountId);
				BudgetPortal.viewDashboard(currentAccountId, username);
				break;

		case 6: System.out.println("\nFrom date (yyyy:mm:dd)");
				String customDateReportGenerationTo = scanner.nextLine();
				System.out.println("\nFrom date (yyyy:mm:dd)");
				String customDateReportGenerationFrom = scanner.nextLine();
				GenarateCustomReportDAOImpl report = new GenarateCustomReportDAOImpl();
				report.generateReport(customDateReportGenerationTo, customDateReportGenerationFrom);
				break; // Is this redundant?
				
		case 7: 
				clearScreen();
				proceedToPortal();
				break; 
				
		default: System.out.println("Invalid Input");
		
		}
		
		scanner.close();
		
	}	

}



