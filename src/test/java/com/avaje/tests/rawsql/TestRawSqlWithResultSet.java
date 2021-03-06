package com.avaje.tests.rawsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.Transaction;
import com.avaje.tests.model.basic.Customer;
import com.avaje.tests.model.basic.ResetBasicData;

public class TestRawSqlWithResultSet extends BaseTestCase {

  @Test
  public void test() throws SQLException {
    
    ResetBasicData.reset();
    
    // Transaction supplies our jdbc Connection
    Transaction txn = Ebean.beginTransaction();
    
    PreparedStatement pstmt = null;
    
    try {
      pstmt = txn.getConnection().prepareStatement("select id, name, billing_address_id from o_customer");
      
      // ResultSet will be closed by Ebean
      ResultSet resultSet = pstmt.executeQuery();
      
      RawSql rawSql = new RawSql(resultSet, "id", "name", "billingAddress.id");
      
      List<Customer> list = Ebean.find(Customer.class)
        .setRawSql(rawSql)
        // also test a secondary query join 
        .fetch("billingAddress", new FetchConfig().query())
        .findList();

      for (Customer customer : list) {
        System.out.println("id:"+customer.getId()+" name:"+customer.getName()+" billingAddress:"+customer.getBillingAddress());
      }
      
    } finally {
      close(pstmt);
      txn.end();
    }
    
  }
  
  private static void close(Statement stmt) {

    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
}
