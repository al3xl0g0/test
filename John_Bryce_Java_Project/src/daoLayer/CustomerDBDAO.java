package daoLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import connection.ConnectionPool;
import javaBeans.Coupon;
import javaBeans.CouponType;
import javaBeans.Customer;
import projectExceptions.DuplicateDataException;
import projectExceptions.LogInFailureException;
import projectExceptions.ShutDownException;
import utils.CustomerExceptionConstants;
import projectExceptions.DataNotExistException;

/**
 * This class is a part of the DAO layer. The class which communicates between
 * the customer's object related methods and the Mysql data base through sql
 * queries.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CustomerDBDAO implements CustomerDAO {

	// pool - the connection pool.
	private ConnectionPool pool;

	/**
	 * creates and initializes the variable for saving the logged in customer's
	 * ID.
	 */
	protected static long loggedInCustomerID = 0;

	/**
	 * Constructs the class that communicates with the db and initializes the
	 * pool variable with the Connection pool's instance.
	 * 
	 */
	public CustomerDBDAO() {

		pool = ConnectionPool.getInstance();

	}

	/**
	 * This method creates the customer's record into the Mysql data base, using
	 * the received customer parameter.
	 * 
	 * @param customer
	 *            - the customer object that is filled with the fields of the
	 *            customer to add to the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DuplicateDataException
	 *             - in cases which the customer to add to the data base exists.
	 *             already.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void createCustomer(Customer customer) throws SQLException, DuplicateDataException, ShutDownException {

		if (!checkIfCustomerExists(customer.getCustName())) {

			/*
			 * String sql - query for the data base. PreparedStatement
			 * preparedStatment - the statment that through the connection uses
			 * the sql object and communicating with the data base and returning
			 * ResultSet generated keys to the object generateKeys - holds the
			 * generated keys from the data base. long id - the variable to hold
			 * the customer's id.
			 */
			Connection conn = pool.getConnection();

			String sql = "INSERT INTO Customer (CUST_NAME, PASSWORD) VALUES (?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, customer.getCustName());
			preparedStatement.setString(2, customer.getPassword());
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			long id = 0;
			if (generatedKeys.next()) {
				id = generatedKeys.getLong(1);
				customer.setId(id);
			}

			preparedStatement.close();
			generatedKeys.close();
			pool.returnConnection(conn);
			System.out.println("Customer created successfully.");
		} else {
			throw new DuplicateDataException(CustomerExceptionConstants.CUSTOMER_ALREADY_EXISTS);
		}
	}

	/**
	 * This method removes the customer's record from the Mysql data base, using
	 * the received customer parameter.
	 * 
	 * @param customer
	 *            - the customer object that is filled with the fields of the
	 *            customer to remove from the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the customer to remove from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void removeCustomer(Customer customer) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement stmt - statment
		 * that through the connection communicates with the data base.
		 * ResultSet rs - hold the result of the sql query. int custId - the
		 * customer's id.
		 * 
		 */
		if (checkIfCustomerExists(customer.getCustName())) {

			Connection conn = pool.getConnection();

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("Select ID from Customer where CUST_NAME = '" + customer.getCustName() + "'");
			int custId = 0;
			if (rs.next()) {
				custId = rs.getInt("ID");
			}
			stmt.execute("DELETE Customer_Coupon FROM Customer_Coupon WHERE CUST_ID=" + custId + ";");
			stmt.execute("DELETE Customer FROM Customer WHERE ID=" + custId + ";");

			stmt.close();
			pool.returnConnection(conn);
			System.out.println("Customer removed successfully.");
		} else {
			throw new DataNotExistException(CustomerExceptionConstants.CUSTOMER_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method updates some columns in the customer's record in the Mysql
	 * data base, using the received customer parameter.
	 * 
	 * @param customer
	 *            - the customer object that is filled with the fields of the
	 *            customer to update into the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the customer to update into the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void updateCustomer(Customer customer) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement stmt - statment
		 *
		 * that through the connection communicates with the data base.
		 */
		if (checkIfCustomerExists(customer.getCustName())) {

			Connection conn = pool.getConnection();
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE Customer SET PASSWORD='" + customer.getPassword() + "' WHERE CUST_NAME='"
					+ customer.getCustName() + "'");
			stmt.close();
			pool.returnConnection(conn);
			System.out.println("The customer was updated successfully.");
		} else {
			throw new DataNotExistException(CustomerExceptionConstants.CUSTOMER_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives the customer's record from the Mysql data base,
	 * using the received customer's id parameter.
	 * 
	 * @param id
	 *            - the customer's id of the customer we wish to get from the
	 *            data base.
	 * @return Customer - the customer object that we receive from the record of
	 *         the customer in the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the customer to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Customer getCustomer(long id) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * customer customerRecieved - the customer that will be received from
		 * the data base. Connection conn - connection from the pool. Statement
		 * - stmt - statment that through the connection communicates with the
		 * data base. ResultSet rs - hold the result of the sql query.
		 * ArrayList<Coupon> customerCoupons - all the coupons of the specific
		 * customer.
		 */
		Customer customerRecieved = new Customer();
		Connection conn = pool.getConnection();

		Statement stmt = conn.createStatement();
		ArrayList<Coupon> customerCoupons = new ArrayList<>();

		ResultSet rSet = stmt.executeQuery("SELECT * FROM Customer WHERE ID=" + id);
		if (rSet.next()) {
			customerRecieved.setId(rSet.getLong("ID"));
			customerRecieved.setCustName(rSet.getString("CUST_NAME"));
			customerRecieved.setPassword(rSet.getString("PASSWORD"));

			rSet = stmt.executeQuery(
					"select * from Coupon where ID IN (SELECT COUPON_ID FROM Customer_Coupon WHERE CUST_ID = " + id
							+ ")");

			while (rSet.next()) {
				Coupon customerCoupon = new Coupon();
				String stringType;
				customerCoupon.setId(rSet.getLong("ID"));
				customerCoupon.setTitle(rSet.getString("TITLE"));
				customerCoupon.setStartDate(rSet.getDate("START_DATE"));
				customerCoupon.setEndDate(rSet.getDate("END_DATE"));
				customerCoupon.setAmount(rSet.getInt("AMOUNT"));
				stringType = rSet.getString("TYPE");
				CouponType type = CouponType.valueOf(stringType);
				customerCoupon.setType(type);
				customerCoupon.setMessage(rSet.getString("MESSAGE"));
				customerCoupon.setPrice(rSet.getDouble("PRICE"));
				customerCoupon.setImage(rSet.getString("IMAGE"));

				customerCoupons.add(customerCoupon);
			}
			customerRecieved.setCoupons(customerCoupons);
			closeResources(stmt, rSet, conn);
			return customerRecieved;
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CustomerExceptionConstants.CUSTOMER_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives all the customer's records of all the customers from
	 * the Mysql data base.
	 * 
	 * @return Collection of Customer - collection of all the customers in the
	 *         system in Array list of customer objects that we receive from the
	 *         records of the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the customer to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Customer> getAllCustomers() throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rs - hold the result of the sql query. ArrayList<Customer>
		 * allCustomerList - all the customer of the system.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rSet = stmt.executeQuery("SELECT * FROM Customer");

		Collection<Customer> allCustomerList = new ArrayList<>();

		if (rSet.next()) {
			rSet.absolute(0);

			while (rSet.next()) {
				Customer customer = new Customer();
				customer = getCustomer(rSet.getLong("ID"));
				allCustomerList.add(customer);
			}
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CustomerExceptionConstants.NO_CUSTOMERS_IN_THE_SYSTEM);
		}
		closeResources(stmt, rSet, conn);

		return allCustomerList;
	}

	/**
	 * This method receives all the logged customer's coupons from the records
	 * of the Mysql data base.
	 * 
	 * @return Collection of Coupons- collection of all the logged customer's
	 *         coupons in the system in Array list with customer objects that we
	 *         receive from the records of the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the customer to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Coupon> getCoupons() throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query. Collection<Coupon>
		 * allCouponList - array list of all the coupons of the customer.
		 */
		CouponDBDAO couponDBDAO = new CouponDBDAO();

		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rSet;

		rSet = stmt.executeQuery("SELECT * FROM Customer WHERE ID= " + loggedInCustomerID);

		Collection<Coupon> allCouponList = new ArrayList<>();

		if (rSet.next()) {
			rSet = stmt.executeQuery("SELECT COUPON_ID FROM Customer_Coupon WHERE CUST_ID=" + loggedInCustomerID);

			Collection<Integer> couponsId = new ArrayList<>();

			while (rSet.next()) {
				Integer couponIdInteger = rSet.getInt("COUPON_ID");
				couponsId.add(couponIdInteger);
			}

			for (Integer integer : couponsId) {
				Coupon couponFromId = couponDBDAO.getCoupon(integer);
				allCouponList.add(couponFromId);
			}
			if (allCouponList.isEmpty()) {
				closeResources(stmt, rSet, conn);
				throw new DataNotExistException(CustomerExceptionConstants.CUSTOMER_DOES_NOT_HAVE_COUPONS);
			}
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CustomerExceptionConstants.CUSTOMER_DOES_NOT_EXISTS);
		}
		closeResources(stmt, rSet, conn);

		return allCouponList;
	}

	/**
	 * This method is for closing all resources that are still open in the
	 * method.
	 * 
	 * @param st
	 *            - the statement object that was created for the of the sql
	 *            query.
	 * @param rs
	 *            - the result set that was created for the sql query's answer.
	 * @param connection
	 *            - the connection object for the data base.
	 */
	private void closeResources(Statement st, ResultSet rs, Connection connection) {
		try {
			st.close();
			rs.close();
			pool.returnConnection(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * a method that checks if the customer exists in the data base or not.
	 * 
	 * @param name
	 *            - the name of the customer to check.
	 * @return boolean - a boolean value of whether the customer exists or not.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	private boolean checkIfCustomerExists(String name) throws SQLException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rSet = stmt.executeQuery("SELECT CUST_NAME FROM Customer WHERE CUST_NAME='" + name + "'");

		boolean flag = false;

		if (rSet.next()) {
			flag = true;
		}
		closeResources(stmt, rSet, conn);

		return flag;
	}

	/**
	 * A method for login into the system through passing user name and
	 * password.
	 * 
	 * @param custName
	 *            - the user name of the customer that wants to log in.
	 * @param password
	 *            - the password of the customer that wants to log in.
	 * @return login - boolean weather the credentials are correct or not.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws LogInFailureException
	 *             - if the credentials are wrong.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public boolean login(String custName, String password)
			throws SQLException, LogInFailureException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT ID, CUST_NAME, PASSWORD FROM Customer WHERE CUST_NAME = '" + custName
				+ "' AND PASSWORD = '" + password + "'");

		if (rs.next()) {
			loggedInCustomerID = rs.getLong("ID");
			System.out.println("Customer LogIn success");
		} else {
			closeResources(stmt, rs, conn);
			throw new LogInFailureException(CustomerExceptionConstants.CUSTOMER_LOGIN_FAILED);
		}
		closeResources(stmt, rs, conn);

		return true;
	}

}
