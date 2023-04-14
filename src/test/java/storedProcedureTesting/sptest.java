package storedProcedureTesting;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class sptest 
{
	 /*
	  * 1) Create a connection.
	  * 2) Create statement/query.
	  * 3) Execute statement/Query.
	  * 4) close execution.
	  */
	
	// 1) Create a connection.
			Connection con=null;
			Statement stat=null;
			ResultSet rs;
			ResultSet rs1;
			ResultSet rs2;
			CallableStatement cStmt;
			
			@BeforeClass
			void setup() throws SQLException
			{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","root");
			}
			
	// 2) Create statement/query.
			/* Syntax							Store Procedures
			 * {call procedure_name()}			Accept no parameters and return no value
			 * {call procedure_name(?,?)		Accept two parameters and return no value
			 * {?=call procedure_name()}		Accept no parameter and return value
				{?=call procedure_name(?)}		Accept one parameter and return value
			 */
			@Test(priority=1)
			void test_storedProcedures() throws SQLException
			{
				stat=con.createStatement();
				rs=stat.executeQuery("SHOW PROCEDURE STATUS WHERE Name='SelectAllCustomers'");
				rs.next();
				
				Assert.assertEquals(rs.getString("Name"),"SelectAllCustomers");
			}
			
			
			@Test(priority=2)
			void test_SelectAllCustomers() throws SQLException
			{
				cStmt = con.prepareCall("{CALL SelectAllCustomers()}"); 
				rs1=cStmt.executeQuery();
				
				Statement stmt=con.createStatement();
				rs2= stmt.executeQuery("select * from customers;");
				Assert.assertEquals(compareResultSets(rs1,rs2),true);
				
			}
			
			public boolean compareResultSets(ResultSet resultSet1,ResultSet resultSet2) throws SQLException  {
				while (resultSet1.next());
				{
					resultSet2.next();
					int count =resultSet1.getMetaData().getColumnCount();
					for (int i=1;i<=count;i++)
					{
						if(!StringUtils.equals(resultSet1.getString(i),resultSet2.getString(i)))
						{
							return false;
						}
					}
				}
				return true;
				}
			
			@Test
			void test_SelectAllCustomersByCity()
			{
				
				
			}
			
	// 4) close execution.
			
			@AfterClass
			void teardown() throws SQLException
			{
				con.close();
			}
}
