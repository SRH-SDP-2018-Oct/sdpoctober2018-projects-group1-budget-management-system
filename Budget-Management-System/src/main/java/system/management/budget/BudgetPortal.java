package system.management.budget;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import system.management.budget.connections.GenarateCustomReportDAOImpl;
import system.management.budget.connections.LoginRegistrationDAOImpl;
import system.management.budget.valueObjects.UserRegistrationVO;


public class BudgetPortal 
{
	public static void main(String[] args) {
		
		clearScreen();
		System.out.println("Welcome to the BUDGET MANAGEMENT SYSTEM Portal...");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println(" Select your Option ");
		System.out.println("1 : Login");
		System.out.println("2 : Register ");
		System.out.println("3 : Forgot your Password");
		System.out.println(" ******************** ");
		
		String loginRegistrationChoice=scanner.nextLine();
		LoginRegistrationDAOImpl con = new LoginRegistrationDAOImpl();
		if (loginRegistrationChoice.equals("1")) {
			userLogin(scanner, con);
		}
		//Registration form:
		else if(loginRegistrationChoice.equals("2")){
			userRegistration(scanner, con);
		}
		else {
			System.out.println("\nPlease enter a valid number.");
		}
		scanner.close();
	}
	public static void userRegistration(Scanner scanner, LoginRegistrationDAOImpl con) {
		UserRegistrationVO reg = new UserRegistrationVO();
		System.out.println("\nPlease provide the below metioned details to register. \n\nFIRST NAME:");
		//String newLine = System.getProperty("line.separator");
		reg.setFirstName(scanner.nextLine());
		System.out.println("\nLAST NAME:");
		reg.setLastName(scanner.nextLine());
		do{
			System.out.println("\nEMAIL ADDRESS:");
			reg.setEmailID(scanner.nextLine());
				if(!reg.getEmailID().contains("@") && !reg.getEmailID().contains("."))
					System.out.println("Please enter a valid email address.");
		}while(!reg.getEmailID().contains("@") && !reg.getEmailID().contains("."));
		
		
		System.out.println("\nGENDER");
		reg.setGender(scanner.nextLine());
		System.out.println("\nPASSWORD");
		reg.setPassword(scanner.nextLine());
		System.out.println("\nTO RETRIVE YOU ACCOUNT INCASE YOU FORGET YOUR PASSWORD, PLEASE ANSWER THE SECURITY QUESTION BELOW."
				+ "\n\nWHO WAS YOUR FIRST EMPLOYER.");
		reg.setRecoveryAnswer(scanner.nextLine());
		System.out.println(reg.getFirstName()+" "+reg.getEmailID()+" "+reg.getLastName()+" "+reg.getGender());
	
		con.registrationDbConnection(reg);
		
		System.out.println("Registration successful... You may proceed to login.");
	}

	public static void userLogin(Scanner scanner, LoginRegistrationDAOImpl con) {
		System.out.println("\nEnter your Email-ID: ");

		String username = scanner.nextLine();
		// scanner.close();

		Console c = System.console();
		String pass;
		System.out.println("\nEnter password: ");
		if(c!=null) {
			
			char[] ch = c.readPassword();
			pass = String.valueOf(ch);// converting char array into string
		}
		else {
			System.out.println("Hello");
			pass = scanner.nextLine();
		}
		System.out.println("Password is: " + pass);
		System.out.println("Your username is " + username);

		String currentAccountId = con.createConnection(username, pass);
		System.out.println("User logged in :: " + currentAccountId);
		viewDashboard(currentAccountId,username);//why is this username?
		
	}
	
	public static void userForgotPassword(Scanner scanner, LoginRegistrationDAOImpl con) {
		System.out.println("Please enter your email address:");
		String email=scanner.nextLine();
		System.out.println("What is the last name of the teacher who failed you?:");
		String answer=scanner.nextLine();
		
		con.forgotPasswordDbConnection(email, answer);
		}
	
		private static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}
	
	public static void viewDashboard(String currentAccountId, String username) {

		DashboardDaoImpl dashboardView = new DashboardDaoImpl();
	
		System.out.println("Welcome User :  " + username + "\n" );
		
		System.out.println(" **********  Menu Options  ********** ");
		
		System.out.println("1 : Dashboard");
		System.out.println("2 : Add Or Delete - Bank Account/ Subscription");
		System.out.println("3 : Expense Tracker");
		System.out.println("4 : Notifications");
		System.out.println("5 : Reports");
		System.out.println("6 : Logout");
		
		System.out.println(" ******************** ");
		Scanner scan = new Scanner(System.in);
		int choice = scan.nextInt();
	
		
		switch (choice){
		case 1 :
					dashboardView.getTransaction(currentAccountId);
					break;
		case 5 :
					//Code for generating custom dynamic jasper report.
					System.out.println("\nFrom date (yyyy:mm:dd)");
					String customDateReportGenerationTo = scanner.nextLine();
					System.out.println("\nFrom date (yyyy:mm:dd)");
					String customDateReportGenerationFrom = scanner.nextLine();
					GenarateCustomReportDAOImpl report = new GenarateCustomReportDAOImpl();			
					report.generateReport(customDateReportGenerationTo,customDateReportGenerationFrom);			
					
		default : System.out.println("Invalid Input");
		    

		}
		
		scan.close();
		
	
		
	}
	

}
