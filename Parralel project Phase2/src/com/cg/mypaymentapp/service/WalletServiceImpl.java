package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cg.mypaymentapp.Exception.InsufficientBalanceException;
import com.cg.mypaymentapp.Exception.InvalidInputException;
import com.cg.mypaymentapp.Exception.MobileNumberNotRegistered;
import com.cg.mypaymentapp.Exception.ZeroBalanceException;
import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {
 
	WalletRepo walletRepo = new WalletRepoImpl();
	
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		
		if(amount.compareTo(new BigDecimal("0.00")) <0)
			throw new InvalidInputException("amount caanot be negative");
			
		Pattern p=Pattern.compile("[0-9]{10}");
		Matcher m=p.matcher(mobileNo);
		
		if(m.find()&& m.group().equals(mobileNo)) {		
		
			Customer customer1=null;
		
			try {
				customer1 = walletRepo.findOne(mobileNo);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.getMessage();
			}
		
			if(customer1==null) {
		   
				Wallet wallet = new Wallet(amount);
	     
				Customer customer=new Customer(name,mobileNo,wallet);
				customer1 = customer;
			
				try {
					System.out.println(customer1);
					walletRepo.save(customer);
				} catch (ClassNotFoundException | SQLException e) {
					e.getMessage();
				}
				return customer1;
			}
			else
				throw new MobileNumberNotRegistered("your mobile number already registered");
		}
		else
			throw new InvalidInputException("please correctly enter 10 digit mobile number");	
	}

	public Customer showBalance(String mobileNo) {
		
		Pattern p=Pattern.compile("[0-9]{10}");
		Matcher m=p.matcher(mobileNo);
		
		if(m.find()&& m.group().equals(mobileNo)) {	
		       
			Customer customer = null;
		         
			try {
			     customer = walletRepo.findOne(mobileNo);
		       } catch (ClassNotFoundException | SQLException e) {
			       e.printStackTrace(); 
		       }
		            
			if(customer != null)       
				return customer;
		                    
			else        	
				throw new MobileNumberNotRegistered("your mobile number already registered");
		}
		else
			throw new InvalidInputException("please correctly enter 10 digit mobile number");
	}
	
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount){
	
		if(amount.compareTo(new BigDecimal("0.00")) ==0)
			throw new ZeroBalanceException("You cannot transfer Zero balance");
			
		if(amount.compareTo(new BigDecimal("0.00")) <0)	
			throw new InvalidInputException("amount caanot be negative");
			
		Pattern pattern=Pattern.compile("[0-9]{10}");
		Matcher m=pattern.matcher(sourceMobileNo);
		Matcher m1=pattern.matcher(targetMobileNo);
		
		if(m.find()&& m.group().equals(sourceMobileNo) &&  m1.find()&& m1.group().equals(targetMobileNo)) {
			Customer cust1 = null, cust2 = null;
		
			try {
				cust1 = walletRepo.findOne(sourceMobileNo);
			} catch (ClassNotFoundException | SQLException e) {
				e.getMessage();
			}
		
			try {
				cust2 = walletRepo.findOne(targetMobileNo);
			} catch (ClassNotFoundException | SQLException e) {
				e.getMessage();
			}
	    
			if(cust1==null && cust2==null)
				throw new MobileNumberNotRegistered("Source mobile Number and target mobile number is not registered");
	    
			else if(cust2==null)
				throw new MobileNumberNotRegistered("Target mobile Number is not registered");
	    
			else if(cust1==null)
				throw new MobileNumberNotRegistered("Source mobile Number is not registered");
	    
			else {
	    	
				if(sourceMobileNo.equals(targetMobileNo))
					throw new InvalidInputException("Source mobile number and target mobile number cannot be same");
			
				BigDecimal bigdecimal = cust1.getWallet().getBalance();
				bigdecimal = bigdecimal.subtract(amount);
		
				if(bigdecimal.compareTo(new BigDecimal("0.00")) <0)
					throw new InsufficientBalanceException("Insufficient balance in source registered mobile number");
		
				Wallet wallet = new Wallet(bigdecimal);
				cust1.setWallet(wallet);
		
				BigDecimal newbigdecimal = cust2.getWallet().getBalance();
		
				newbigdecimal = newbigdecimal.add(amount);
	
				Wallet wallet1 = new Wallet(newbigdecimal);
		
				cust2.setWallet(wallet1);
				System.out.println("finally in wallet"+cust2.getWallet().getBalance());
          
				try {
					walletRepo.save(cust1);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
         
				try {
					walletRepo.save(cust2);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}  
				return cust1;
			}
		}
		else
			throw new InvalidInputException("please correctly enter 10 digit mobile number");
    }
	
	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		
		if(amount.compareTo(new BigDecimal("0.00")) ==0)	
			throw new ZeroBalanceException("You cannot deposit Zero balance in your account");
			
		if(amount.compareTo(new BigDecimal("0.00")) <0)	
			throw new InvalidInputException(" deposited amount caanot be negative or zero");
			
		Pattern pattern=Pattern.compile("[0-9]{10}");
		Matcher m=pattern.matcher(mobileNo);
		
		if(m.find()&& m.group().equals(mobileNo)) {
		
			Customer cust1=null;
		
			try {
				cust1 = walletRepo.findOne(mobileNo);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		 
			if(cust1==null)
		    	throw new MobileNumberNotRegistered(" mobile Number is not registered");
		    
			else {
				BigDecimal amt = cust1.getWallet().getBalance();
		
				amt = amt.add(amount);
		
				Wallet wallet = new Wallet(amt);
		
				cust1.setWallet(wallet);
		
				try {
					walletRepo.save(cust1);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		
				return cust1;
			}
		}
		else
			throw new InvalidInputException("please correctly enter 10 digit mobile number");
	}
	
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		
		if(amount.compareTo(new BigDecimal("0.00")) ==0)	
			throw new ZeroBalanceException("You cannot withdraw Zero balance in your account");
			
		if(amount.compareTo(new BigDecimal("0.00")) <=0)	
			throw new InvalidInputException("withdrawl amount caanot be negative ror zero");
			
		Pattern pattern=Pattern.compile("[0-9]{10}");
		Matcher m=pattern.matcher(mobileNo);
		
		if(m.find()&& m.group().equals(mobileNo)) {
		
			Customer cust1=null;
		
			try {
				cust1 = walletRepo.findOne(mobileNo);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		 
			if(cust1==null)
		    	throw new MobileNumberNotRegistered("mobile Number is not registered");
		    
		 
			else {
		
				BigDecimal amt = cust1.getWallet().getBalance();
		
				amt = amt.subtract(amount);
		
				if(amt.compareTo(new BigDecimal("0.00")) <0)
					throw new InsufficientBalanceException("Insufficient balance in source registered mobile number");
		
				Wallet wallet = new Wallet(amt);
		
				cust1.setWallet(wallet);
		
				try {
					walletRepo.save(cust1);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				return cust1;
			}
		}
		else
			throw new InvalidInputException("please correctly enter 10 digit mobile number");
	}
}