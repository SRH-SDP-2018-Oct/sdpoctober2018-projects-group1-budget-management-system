package system.management.budget;

import java.io.Console;
import java.util.Scanner;
import system.management.budget.connections.*;
import system.management.budget.valueObjects.*;


public class BudgetPortal 
{
	public static void main(String[] args) {
		System.out.println("*****  WELCOME TO THE BUDGET MANAGEMENT SYSTEM PORTAL  ***** \n");
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
			System.out.println("Please enter a valid number.");
		}
		scanner.close();
	}
	private static void userRegistration(Scanner scanner, LoginRegistrationDAOImpl con) {
		UserRegistrationVO reg = new UserRegistrationVO();
		System.out.println("Please provide the below metioned details to register");
		String newLine = System.getProperty("line.separator");
		
		System.out.println(newLine+"FIRST NAME:");
		reg.setFirstName(scanner.nextLine());
		System.out.println(newLine+"LAST NAME:");
		reg.setLastName(scanner.nextLine());
		System.out.println(newLine+"EMAIL ADDDRESS:");
		reg.setEmailID(scanner.nextLine());
		System.out.println(newLine+"MOBILE NUMBER");
		reg.setMobileNumber(scanner.nextLine());
		System.out.println(reg.getFirstName()+" "+reg.getEmailID()+" "+reg.getLastName()+" "+reg.getMobileNumber());
	
		con.registrationDbConnection(reg);
		
		System.out.println("Registration successful... You may proceed to login.");
	}

	private static void userLogin(Scanner scanner, LoginRegistrationDAOImpl con) {
		System.out.println("Enter your username: ");

		String username = scanner.nextLine();
		// scanner.close();

		Console c = System.console();
		System.out.println("Enter password: ");
		char[] ch = c.readPassword();
		String pass = String.valueOf(ch);// converting char array into string
		System.out.println("Password is: " + pass);
		System.out.println("Your username is " + username);

		String currentAccountId = con.createConnection(username, pass);
		System.out.println("User logged in :: " + currentAccountId);
		viewDashboard(currentAccountId,username);
		
	}
	
	private static void userForgotPassword(Scanner scanner, LoginRegistrationDAOImpl con) {
		System.out.println("Please enter your email address:");
		String email=scanner.nextLine();
		System.out.println("What is the last name of the teacher who failed you?:");
		String answer=scanner.nextLine();
		
		con.forgotPasswordDbConnection(email, answer);
		}
	
	private static void viewDashboard(String currentAccountId, String username) {

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
					
		default : System.out.println("Invalid Input");
		    

		}
		
		scan.close();
		
	
		
	}
	

}
