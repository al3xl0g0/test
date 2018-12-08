package daoLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import connection.ConnectionPool;
import javaBeans.Coupon;
import javaBeans.CouponType;
import projectExceptions.DataNotExistException;
import projectExceptions.DuplicateDataException;
import projectExceptions.GeneralErrorException;
import projectExceptions.ShutDownException;
import utils.CouponExceptionConstants;
import utils.GeneralExceptionConstants;

/**
 * This class is a part of the DAO layer. The class which communicates between
 * the coupon's object related methods and the Mysql data base through sql
 * queries.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CouponDBDAO implements CouponDAO {

	// pool - the connection pool instance.
	private ConnectionPool pool;
	// sdf - the date format.
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Constructs the class that communicates with the db and initializes the
	 * pool variable with the Connection pool's instance.
	 */
	public CouponDBDAO() {
		// pool - the connection pool.
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This method creates the coupon's record into the Mysql data base, using
	 * the received coupon parameter.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to add to the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DuplicateDataException
	 *             - in cases which the coupon to add to the data base exists.
	 *             already.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void createCoupon(Coupon coupon) throws SQLException, DuplicateDataException, ShutDownException {

		if (!checkIfCouponExists(coupon.getTitle())) {

			// conn - the connection object from the pool.
			Connection conn = pool.getConnection();

			/*
			 * String sql - query for the data base. PreparedStatement
			 * preparedStatment - the statment that through the connection uses
			 * the sql object and communicating with the data base and returning
			 * ResultSet generated keys to the object generateKeys - holds the
			 * generated keys from the data base. long id - the variable to hold
			 * the coupons's id.
			 */
			String sql = "INSERT INTO Coupon (TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, java.sql.Date.valueOf(sdf.format(coupon.getStartDate())));
			preparedStatement.setDate(3, java.sql.Date.valueOf(sdf.format(coupon.getEndDate())));
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().toString());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());

			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			long id = 0;
			if (generatedKeys.next()) {
				id = generatedKeys.getLong(1);
				coupon.setId(id);
			}

			// stmt - the statment object for the connection.
			Statement stmt = conn.createStatement();

			stmt.execute("INSERT INTO Company_Coupon VALUES (" + CompanyDBDAO.loggedInCompanyID + "," + coupon.getId()
					+ ");");
			preparedStatement.close();
			generatedKeys.close();
			stmt.close();
			pool.returnConnection(conn);

			System.out.println("Coupon created successfully.");
		} else {
			throw new DuplicateDataException(CouponExceptionConstants.COUPON_ALREADY_EXISTS);
		}
	}

	/**
	 * This method removes the coupon's record from the Mysql data base, using
	 * the received coupon parameter.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to remove from the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupon to remove from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws SQLException, DataNotExistException, ShutDownException {

		if (checkIfCouponExists(coupon.getTitle())) {

			/*
			 * Connection conn - connection from the pool. Statement stmt -
			 * statment that through the connection communicates with the data
			 * base. ResultSet rs - hold the result of the sql query. int coupId
			 * - the coupon's id.
			 * 
			 */
			Connection conn = pool.getConnection();

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("Select ID from Coupon where TITLE = '" + coupon.getTitle() + "'");
			int coupId = 0;
			if (rs.next()) {
				coupId = rs.getInt("ID");
			}

			stmt.execute("DELETE Customer_Coupon FROM Customer_Coupon WHERE COUPON_ID= " + coupId);
			stmt.execute("DELETE Company_Coupon FROM Company_Coupon WHERE COUPON_ID= " + coupId);
			stmt.execute("DELETE Coupon FROM Coupon WHERE ID=" + coupId + ";");

			stmt.close();
			pool.returnConnection(conn);
			System.out.println("Coupon removed successfully.");
		} else {
			throw new DataNotExistException(CouponExceptionConstants.COUPON_DOES_NOT_EXISTS);
		}

	}

	/**
	 * This method updates some columns in the coupon's record in the Mysql data
	 * base, using the received company parameter.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to update into the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupon to update into the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws SQLException, DataNotExistException, ShutDownException {

		if (checkIfCouponExists(coupon.getTitle())) {

			/*
			 * Connection conn - connection from the pool. Statement stmt -
			 * statment
			 *
			 * that through the connection communicates with the data base.
			 */
			Connection conn = pool.getConnection();

			Statement stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE Coupon SET END_DATE='" + sdf.format(coupon.getEndDate()) + "', PRICE="
					+ coupon.getPrice() + "WHERE TITLE='" + coupon.getTitle() + "'");
			stmt.close();
			pool.returnConnection(conn);
			System.out.println("The coupon was updated successfully.");
		} else {
			throw new DataNotExistException(CouponExceptionConstants.COUPON_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives the coupon's record from the Mysql data base, using
	 * the received coupon's id parameter.
	 * 
	 * @param id
	 *            - the coupon's id of the coupon we wish to get from the data
	 *            base.
	 * @return Coupon - the coupon object that we receive from the record of the
	 *         coupon in the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupon to get from the data base does'nt
	 *             exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Coupon getCoupon(long id) throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Coupon coupon - the coupon that will be received from the data base.
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 */
		Connection conn = pool.getConnection();

		Statement stmt = conn.createStatement();

		ResultSet rSet = stmt.executeQuery("SELECT * FROM Coupon WHERE " + id + "=ID;");

		Coupon coupon = null;

		if (rSet.next()) {

			coupon = new Coupon();

			coupon.setId(id);
			coupon.setTitle(rSet.getString("TITLE"));
			coupon.setStartDate(rSet.getDate("START_DATE"));
			coupon.setEndDate(rSet.getDate("END_DATE"));
			coupon.setAmount(rSet.getInt("AMOUNT"));
			String typestring = rSet.getString("TYPE");
			CouponType type = CouponType.valueOf(typestring);
			coupon.setType(type);
			coupon.setMessage(rSet.getString("MESSAGE"));
			coupon.setPrice(rSet.getDouble("PRICE"));
			coupon.setImage(rSet.getString("IMAGE"));

			closeResources(stmt, rSet, conn);

			return coupon;

		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CouponExceptionConstants.COUPON_DOES_NOT_EXISTS);
		}
	}

	/**
	 * This method receives all the coupon's records of all the coupons from the
	 * Mysql data base.
	 * 
	 * @return Collection of Coupon - collection of all the coupons in the
	 *         system in Array list of coupon objects that we receive from the
	 *         records of the data base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupons to get from the data base
	 *             does'nt exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Coupon> getAllCoupon() throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query. ArrayList<Coupon>
		 * allCouponList - list of all the coupons objects.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		ResultSet rSet = stmt.executeQuery("SELECT * FROM Coupon");

		Collection<Coupon> allCouponList = new ArrayList<>();

		if (rSet.next()) {
			rSet.absolute(0);

			while (rSet.next()) {
				// coupon - new coupon object for the list.
				Coupon coupon = new Coupon();

				coupon = getCoupon(rSet.getLong("ID"));

				allCouponList.add(coupon);
			}

		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CouponExceptionConstants.NO_COUPONS_IN_THE_SYSTEM);
		}
		closeResources(stmt, rSet, conn);

		return allCouponList;

	}

	/**
	 * This method receives all the records of coupons that are in the wished
	 * category from all of the coupons from the Mysql data base.
	 * 
	 * @param couponType
	 *            - the specific type of coupons that the method will get from
	 *            the coupons in the db
	 * @return Collection of Coupon - collection of all the coupons in the
	 *         system only from the type of the entered parameter in Array list
	 *         of coupon objects that we receive from the records of the data
	 *         base.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupon to get from the data base does'nt
	 *             exist in it.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	@Override
	public Collection<Coupon> getCouponByType(CouponType couponType)
			throws SQLException, DataNotExistException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query. ArrayList<Coupon>
		 * couponsByType - list of all the coupons objects by type.
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		ResultSet rSet = stmt.executeQuery("SELECT * FROM Coupon WHERE TYPE='" + couponType + "'");

		Collection<Coupon> couponsByType = new ArrayList<>();

		if (rSet.next()) {
			rSet.absolute(0);

			while (rSet.next()) {
				// coupon new coupon object.
				Coupon coupon = new Coupon();
				coupon = getCoupon(rSet.getLong("ID"));
				couponsByType.add(coupon);
			}
		} else {
			closeResources(stmt, rSet, conn);
			throw new DataNotExistException(CouponExceptionConstants.NO_COUPONS_OF_THIS_TYPE);
		}
		closeResources(stmt, rSet, conn);

		return couponsByType;
	}

	/**
	 * This method purchases a specific coupon for the customer after checking
	 * if it can be done. purchasing the coupon is done by checking the
	 * expiration date, amount left and if the customer hasn't already purchase
	 * that coupon. purchasing includes updating the new amount and that the
	 * customer purchased the coupon in the data base .
	 * 
	 * @param coupon
	 *            - the coupon object that is purchased by the customer with the
	 *            fields of the coupon to purchase.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws DataNotExistException
	 *             - in cases which the coupon to get from the data base does'nt
	 *             exist in it.
	 * @throws DuplicateDataException
	 *             - when data already exists.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 * @throws GeneralErrorException
	 *             - when cannot purchase coupon.
	 */
	public void purchaseCoupon(Coupon coupon) throws SQLException, DataNotExistException, DuplicateDataException,
			GeneralErrorException, ShutDownException {

		if (checkIfCouponExists(coupon.getTitle())) {

			/*
			 * Connection conn - connection from the pool. Statement - stmt -
			 * statment that through the connection communicates with the data
			 * base. ResultSet rSet - hold the result of the sql query. rSetId -
			 * second rs for the id.
			 *
			 */
			Connection conn = pool.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// cal - calender object.
			Calendar cal = Calendar.getInstance();
			// currentDate- object for the today current date.
			Date currentDate = null;
			currentDate = (Date) cal.getTime();
			ResultSet rSetId = null;
			ResultSet rSet = stmt
					.executeQuery("SELECT ID, END_DATE, AMOUNT FROM Coupon WHERE TITLE = '" + coupon.getTitle() + "'");
			if (rSet.next()) {
				// id - of the coupon.
				long id = rSet.getLong("ID");
				// amount - of the coupon.
				int amount = rSet.getInt("AMOUNT");
				// date - end date of the coupon,
				Date date = rSet.getDate("END_DATE");

				rSetId = stmt.executeQuery("SELECT * FROM Customer_Coupon WHERE COUPON_ID = " + id + " AND CUST_ID = "
						+ CustomerDBDAO.loggedInCustomerID);

				if (!rSetId.next()) {
					if (amount > 0 && date.after(currentDate)) {
						coupon.setAmount(amount - 1);
						stmt.execute("UPDATE Coupon SET AMOUNT = " + coupon.getAmount() + " WHERE ID = " + id);
						stmt.execute("INSERT INTO Customer_Coupon VALUES (" + CustomerDBDAO.loggedInCustomerID + ","
								+ id + ")");
					} else {
						closeResources(stmt, rSet, conn);
						rSetId.close();
						throw new GeneralErrorException(GeneralExceptionConstants.CANNOT_PURCHASE_COUPON);
					}
				} else {
					closeResources(stmt, rSet, conn);
					rSetId.close();
					throw new DuplicateDataException(CouponExceptionConstants.COUPON_ALREADY_PURCHASED);
				}
			}
			closeResources(stmt, rSet, conn);
			rSetId.close();
			System.out.println("The coupon: " + coupon.getTitle() + "has been purchased.");
		} else {
			throw new DataNotExistException(CouponExceptionConstants.COUPON_DOES_NOT_EXISTS);
		}
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
	 * a method that checks if the coupon exists in the data base or not.
	 * 
	 * @param title
	 *            - the name of the coupon to check.
	 * @return boolean - a boolean value of whether the coupon exists or not.
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	private boolean checkIfCouponExists(String title) throws SQLException, ShutDownException {

		/*
		 * Connection conn - connection from the pool. Statement - stmt -
		 * statment that through the connection communicates with the data base.
		 * ResultSet rSet - hold the result of the sql query.
		 *
		 */
		Connection conn = pool.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rSet = stmt.executeQuery("SELECT TITLE FROM Coupon WHERE TITLE='" + title + "'");

		// flag - boolean indicates if the coupon exists or not.
		boolean flag = false;
		if (rSet.next()) {
			flag = true;
		}
		closeResources(stmt, rSet, conn);

		return flag;

	}

}
