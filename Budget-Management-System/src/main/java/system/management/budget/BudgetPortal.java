package system.management.budget;

import java.io.Console;
import java.util.Scanner;
import system.management.budget.connections.LoginRegistrationDAOImpl;
import system.management.budget.valueObjects.UserRegistrationVO;

/**
 * Hello world!
 *
 */
public class BudgetPortal 
{
	public static void main(String[] args) {
		System.out.println("Welcome to the BUDGET MANAGEMENT SYSTEM Portal...");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("To login press 1. or to register as a new user press 2.");
		
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

		boolean authentication = con.createConnection(username, pass);
		System.out.println("User logged in :: " + authentication);
		viewDashboard();
		
	}
	
	private static void viewDashboard() {
		//ToDo: Add dash board specific code here.
	}
}
