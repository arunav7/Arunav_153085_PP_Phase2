package com.cg.mypaymentapp.repo;

import java.sql.SQLException;

import com.cg.mypaymentapp.beans.Customer;

public interface WalletRepo {

	public boolean save(Customer customer) throws SQLException, ClassNotFoundException ;
	
	public Customer findOne(String mobileNo) throws ClassNotFoundException, SQLException;
}
