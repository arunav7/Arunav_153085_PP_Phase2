package com.cg.mypaymentapp.pl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client {

	static WalletService walletService = new WalletServiceImpl();

	void menu() throws IOException {
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("1.Create an account");
		System.out.println("2.View Balance");
		System.out.println("3.Send Money");
		System.out.println("4.Deposit Amount "); 	
		System.out.println("5.Withdraw Amount ");
		System.out.println("6.Exit ");
		System.out.println();
		System.out.println("Please enter your choice");
	
		int option = Integer.parseInt(consoleInput.readLine());
		
		switch(option){
		
		case 1:
	       
			System.out.print("Please enter your Name: ");
			String name = consoleInput.readLine();
		
			System.out.print("Please enter your Mobile no: ");
			String mob = consoleInput.readLine();
		
			System.out.print("Please enter Amount: ");
			BigDecimal bigdecimal = new BigDecimal(consoleInput.readLine());

			Customer cust = walletService.createAccount(name, mob, bigdecimal);
			
			if(cust==null)
				System.out.println("Mobile number already registered");

			else
				System.out.println("Mobile number SuccessFully registered");

			break;
		
		case 2:
			
			System.out.print("please enter your mobile no");
			String mob1 = consoleInput.readLine();
		
			Customer cust1 = walletService.showBalance(mob1);
			System.out.print("ur balance in account is:"+cust1.getWallet().getBalance());
		
			break;
	
		case 3:
	
			System.out.print("please enter your source mobile no");
			String sourceMobile = consoleInput.readLine();
	
			System.out.print("please enter your target mobile no");
			String targetMobile = consoleInput.readLine();
	
			System.out.print("please enter amount to be transferred");
			BigDecimal amount = new BigDecimal(consoleInput.readLine());
	
			walletService.fundTransfer(sourceMobile, targetMobile, amount);
		
			break;
	
		case 4:
			
			System.out.print("please enter your mobile no");
			String dmob = consoleInput.readLine();
	
			System.out.print("please enter amount to be deposited");
	
			BigDecimal depositAmount = new BigDecimal(consoleInput.readLine());
	
			walletService.depositAmount(dmob, depositAmount);
	
			break;
	
		case 5:
			
			System.out.print("please enter your mobile no");
			String wmob = consoleInput.readLine();
	
			System.out.print("please enter amount to be withdrawm");
			BigDecimal withdrawAmount = new BigDecimal(consoleInput.readLine());
	
			walletService.withdrawAmount(wmob, withdrawAmount);
	
			break;
			
		case 6:
			System.out.println("GoodBye!!!!!!");
			System.exit(0);
			break;
	
		default:
			System.out.println("wrong choice");
			break;
	
		}
	}
	
	public static void main(String[] args) throws IOException {

		Client cli = new Client();

		while(true)
			cli.menu();
	}	
	
}
