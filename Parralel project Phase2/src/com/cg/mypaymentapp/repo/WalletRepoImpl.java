package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.dbutil.Db_Util;

public class WalletRepoImpl implements WalletRepo {
	
	Db_Util dbutil=new Db_Util ();

	public WalletRepoImpl(){
		
	}
	
	@Override
	public boolean save(Customer customer) throws SQLException, ClassNotFoundException {
		Connection con=dbutil.getConnection();
	
		Customer customer1=findOne(customer.getMobileNo());
	
		if(customer1 == null) {
	
			PreparedStatement pst=con.prepareStatement("insert into wallet values(?,?,?)");
			
			pst.setString(1, customer.getName());
			pst.setString(2, customer.getMobileNo());
			pst.setBigDecimal(3, customer.getWallet().getBalance());
	
			pst.executeUpdate();
	
			return true;	
		}
		else if(customer1.getMobileNo().equals(customer.getMobileNo())) {
			
			PreparedStatement pst=con.prepareStatement("update wallet set balance=? where mobNo=?");
		
			pst.setBigDecimal(1, customer.getWallet().getBalance());
			pst.setString(2, customer.getMobileNo());
		
			pst.executeUpdate();

			return true;	
		}
		else
			return false;
	}
	
	@Override
	public Customer findOne(String mobileNo) throws ClassNotFoundException, SQLException {
		
		String name = null;
		String mobile = null;
		BigDecimal amount=new BigDecimal(0.0);
		
		Connection con=dbutil.getConnection();
	
		Statement st=con.createStatement();
	
		String query=String.format("select * from wallet where '%s'= mobNo", mobileNo);
	
		ResultSet rs=st.executeQuery(query);
	
		while(rs.next()) {
			
			name=rs.getString(1);	
			mobile=rs.getString(2);
			amount=rs.getBigDecimal(3);
		}
	
		if(mobile==null)
			return null;
	
		else if(mobile.equals(mobileNo)) {
			
			Wallet wallet=new Wallet(amount);
			Customer customer=new Customer(name,mobile,wallet);
			
			return customer;
		}
		else
			return null;
	}
}
