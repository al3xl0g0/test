package daoLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import connection.ConnectionPool;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;
import projectExceptions.DuplicateDataException;
import projectExceptions.LogInFailureException;
import projectExceptions.ShutDownException;
import utils.CompanyExceptionConstants;
import projectExceptions.DataNotExistException;

/**
 * This class is a part of the DAO layer. The class which communicates between
 * the company's object related methods and the Mysql data base through sql
 * queries.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CompanyDBDAO implements CompanyDAO {

	// - pool the connection pool object.
	private ConnectionPool pool;

	/**
	 * creates and initializes the variable for saving the logged in company's
	 * ID.
	 */
	protected static long loggedInCompanyID = 0;

	/**
	 * Constructs the class that communicates with the db and initializes the
	 * pool variable with the Connection pool's instance.
	 * 
	 */
	public CompanyDBDAO() {

		// pool is an instance of the connection pool object that hold the
		// conenction in it.

		pool = ConnectionPool.getInstance();

	}

	/**
	 * This method creates the company's record into the Mysql data base, using
	 * the received company parameter.
	 * 
	 * @param company
	 *            - the company object that is filled with the fields of the
	 *            company to add to the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DuplicateDataException
	 *             - in cases which the company to add to the data base exists.
	 *             already.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void createCompany(Company company) throws SQLException, DuplicateDataException, ShutDownException {

		if (!checkIfCompanyExists(company.getCompName())) {

			// conn - connection received from the pool.
			Connection conn = pool.getConnection();

			/*
			 * String sql - query for the data base. PreparedStatement
			 * preparedStatment - the statment that through the connection uses
			 * the sql object and communicating with the data base and returning
			 * ResultSet generated keys to the object generateKeys - holds the
			 * generated keys from the data base. long id - the variable to hold
			 * the company's id.
			 */

			String sql = "INSERT INTO Company (COMP_NAME, PASSWORD, EMAIL) VALUES (?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, company.getCompName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			long id = 0;
			if (generatedKeys.next()) {
				id = generatedKeys.getLong(1);
				company.setId(id);
			}
			preparedStatement.close();
			generatedKeys.close();

			pool.returnConnection(conn);
			System.out.println("Company created successfully.");
		} else {
			throw new DuplicateDataException(CompanyExceptionConstants.COMPANY_ALREADY_EXISTS);
		}
	}

	/**
	 * This method removes the company's record from the Mysql data base, using
	 * the received company parameter.
	 * 
	 * @param company
	 *            - the company object that is filled with the fields of the
	 *            company to remove from the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the company to remove from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void removeCompany(Company company) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement stmt - statment
		 * that through the connection communicates with the data base.
		 * ResultSet rs - hold the result of the sql query. int compId - the
		 * company's id.
		 * 
		 */
		if (checkIfCompanyExists(company.getCompName())) {
			Connection conn = pool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("Select ID from Company where COMP_NAME = '" + company.getCompName() + "'");
			int compId = 0;
			if (rs.next()) {
				compId = rs.getInt("ID");
			}
			stmt.execute("DELETE Coupon FROM Coupon WHERE ID IN (select COUPON_ID FROM Company_Coupon WHERE COMP_ID="
					+ compId + ");");
			stmt.execute(
					"DELETE Customer_Coupon FROM Customer_Coupon WHERE COUPON_ID IN(select COUPON_ID FROM Company_Coupon WHERE COMP_ID="
							+ compId + ");");
			stmt.execute("DELETE Company_Coupon FROM Company_Coupon WHERE COMP_ID=" + compId + ";");
			stmt.execute("DELETE Company FROM Company WHERE ID=" + compId + ";");

			stmt.close();
			pool.returnConnection(conn);
			System.out.println("Company removed successfully.");
		} else {
			throw new DataNotExistException(CompanyExceptionConstants.COMPANY_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method updates some columns in the company's record in the Mysql
	 * data base, using the received company parameter.
	 * 
	 * @param company
	 *            - the company object that is filled with the fields of the
	 *            company to update into the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the company to update into the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void updateCompany(Company company) throws SQLException, DataNotExistException, ShutDownException {

		if (checkIfCompanyExists(company.getCompName())) {

			/*
			 * Connection conn - connection from the pool. Statement stmt -
			 * statment
			 *
			 * that through the connection communicates with the data base.
			 */
			Connection conn = pool.getConnection();
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE Company SET PASSWORD='" + company.getPassword() + "', EMAIL='"
					+ company.getEmail() + "' WHERE COMP_NAME='" + company.getCompName() + "'");
			stmt.close();
			pool.returnConnection(conn);
			System.out.println("The company was updated successfully.");
		} else {
			throw new DataNotExistException(CompanyExceptionConstants.COMPANY_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives the company's record from the Mysql data base, using
	 * the received company's id parameter.
	 * 
	 * @param id
	 *            - the company's id of the company we wish to get from the data
	 *            base.
	 * @return Company - the company object that we receive from the record of
	 *         the company in the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the company to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Company getCompany(long id) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Company companyRecieved - the company that will be received from the
		 * data base. Connection conn - connection from the pool. Statement -
		 * stmt - statment that through the connection communicates with the
		 * data base. ResultSet rs - hold the result of the sql query. int
		 * compId - the company's id. ArrayList<Coupon> companyCoupons - all the
		 * coupons of the specific company.
		 */
		Company companyRecieved = new Company();
		Connection conn = pool.getConnection();

		Statement stmt = conn.createStatement();
		ArrayList<Coupon> companyCoupons = new ArrayList<>();

		ResultSet rSet = stmt.executeQuery("SELECT * FROM Company WHERE ID=" + id);
		if (rSet.next()) {

			companyRecieved.setId(rSet.getLong("ID"));
			companyRecieved.setCompName(rSet.getString("COMP_NAME"));
			companyRecieved.setPassword(rSet.getString("PASSWORD"));
			companyRecieved.setEmail(rSet.getString("EMAIL"));
			rSet = stmt.executeQuery(
					"select * from Coupon where ID IN (SELECT COUPON_ID FROM Company_Coupon WHERE COMP_ID = " + id
							+ ")");

			while (rSet.next()) {
				/*
				 * Coupon companyCoupon - a company's coupon to be created.
				 * String stringType - holds the type of the coupon in string
				 * object. CouponType type - the type of the coupon in enum.
				 */
				Coupon companyCoupon = new Coupon();
				String stringType;
				companyCoupon.setId(rSet.getLong("ID"));
				companyCoupon.setTitle(rSet.getString("TITLE"));
				companyCoupon.setStartDate(rSet.getDate("START_DATE"));
				companyCoupon.setEndDate(rSet.getDate("END_DATE"));
				companyCoupon.setAmount(rSet.getInt("AMOUNT"));
				stringType = rSet.getString("TYPE");
				CouponType type = CouponType.valueOf(stringType);
				companyCoupon.setType(type);
				companyCoupon.setMessage(rSet.getString("MESSAGE"));
				companyCoupon.setPrice(rSet.getDouble("PRICE"));
				companyCoupon.setImage(rSet.getString("IMAGE"));

				companyCoupons.add(companyCoupon);
			}
			companyRecieved.setCoupons(companyCoupons);
			closeResources(stmt, rSet, conn);
			return companyRecieved;
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CompanyExceptionConstants.COMPANY_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives all the company's records of all the companies from
	 * the Mysql data base.
	 * 
	 * @return Collection of Company - collection of all the companies in the
	 *         system in Array list of company objects that we receive from the
	 *         records of the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the company to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Company> getAllCompanies() throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rs - hold the result of the sql query. int compId - the
		 * company's id. ArrayList<Coupon> companyCoupons - all the coupons of
		 * the specific company. Collection<Company> allCompanyList - a list of
		 * all the company objects.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rSet = stmt.executeQuery("SELECT * FROM Company");

		Collection<Company> allCompanyList = new ArrayList<>();

		if (rSet.next()) {
			rSet.absolute(0);

			while (rSet.next()) {
				// company - the created company.
				Company company = new Company();
				company = getCompany(rSet.getLong("ID"));
				allCompanyList.add(company);
			}
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CompanyExceptionConstants.NO_COMPANIES_IN_THE_SYSTEM);
		}
		closeResources(stmt, rSet, conn);

		return allCompanyList;
	}

	/**
	 * This method receives all the logged company's coupons from the records of
	 * the Mysql data base.
	 * 
	 * @return Collection of Coupons- collection of all the logged company's
	 *         coupons in the system in Array list with company objects that we
	 *         receive from the records of the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the company to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Coupon> getCoupons() throws SQLException, DataNotExistException, ShutDownException {

		// CouponDBDAO couponDBDAO - the class that communicate with the data
		// base for related coupon actions.

		CouponDBDAO couponDBDAO = new CouponDBDAO();

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query. Collection<Coupon>
		 * allCouponList - array list of all the coupons of the company.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rSet;

		rSet = stmt.executeQuery("SELECT * FROM Company WHERE ID= " + loggedInCompanyID);

		Collection<Coupon> allCouponList = new ArrayList<>();

		if (rSet.next()) {
			rSet = stmt.executeQuery("SELECT COUPON_ID FROM Company_Coupon WHERE COMP_ID=" + loggedInCompanyID);

			// Collection<Integer> couponsId - list of all the company coupon's
			// id integer objects.
			Collection<Integer> couponsId = new ArrayList<>();

			while (rSet.next()) {

				// Integer couponIdInteger - coupon id integer of a coupon of
				// the company.
				Integer couponIdInteger = rSet.getInt("COUPON_ID");
				couponsId.add(couponIdInteger);
			}

			for (Integer integer : couponsId) {

				// Coupon couponFromId - a full coupon object by the coupon id.
				Coupon couponFromId = couponDBDAO.getCoupon(integer);
				allCouponList.add(couponFromId);
			}
			if (allCouponList.isEmpty()) {
				closeResources(stmt, rSet, conn);
				throw new DataNotExistException(CompanyExceptionConstants.COMPANY_DOES_NOT_HAVE_COUPONS);
			}
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CompanyExceptionConstants.COMPANY_DOES_NOT_EXISTS);
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
	 * a method that checks if the company exists in the data base or not.
	 * 
	 * @param name
	 *            - the name of the compay to check.
	 * @return boolean - a boolean value of whether the company exists or not.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	private boolean checkIfCompanyExists(String name) throws SQLException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rSet = stmt.executeQuery("SELECT COMP_NAME FROM Company WHERE COMP_NAME='" + name + "'");

		// boolean flag - indicates if the company exists or not.
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
	 * @param compName
	 *            - the user name of the company that wants to log in.
	 * @param password
	 *            - the password of the company that wants to log in.
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
	public boolean login(String compName, String password)
			throws SQLException, LogInFailureException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT ID, COMP_NAME, PASSWORD FROM Company WHERE COMP_NAME = '" + compName
				+ "' AND PASSWORD = '" + password + "'");

		if (rs.next()) {
			loggedInCompanyID = rs.getLong("ID");
		} else {
			closeResources(stmt, rs, conn);
			throw new LogInFailureException(CompanyExceptionConstants.COMPANY_LOGIN_FAILED);
		}
		closeResources(stmt, rs, conn);

		return true;
	}
}
