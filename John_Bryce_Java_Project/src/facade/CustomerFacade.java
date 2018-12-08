package facade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import daoLayer.CouponDBDAO;
import daoLayer.CustomerDBDAO;
import javaBeans.ClientType;
import javaBeans.Coupon;
import javaBeans.CouponType;
import projectExceptions.DuplicateDataException;
import projectExceptions.GeneralErrorException;
import projectExceptions.LogInFailureException;
import projectExceptions.ShutDownException;
import projectExceptions.DataNotExistException;

/**
 * This class is a part of the facade pattern layer. The class which manages the
 * customer user's request for using methods with customer functionality after a
 * successful login. The class uses the methods of the CustomerDBDAO and the
 * Coupon DBDAO classes - which communicates with the data base. In addition,
 * this class also catches any related exceptions that might occur.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CustomerFacade implements CouponClientFacade {

	// the DBDAO classes for use of the methods.
	private CustomerDBDAO customerDBDAO;
	private CouponDBDAO couponDBDAO;

	/**
	 * Constructs the class of the customer facade and creating new relevant
	 * DBDAO objects for the use of the facade with the DBDAO objects.
	 */
	public CustomerFacade() {

		customerDBDAO = new CustomerDBDAO();
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * This method is for the customer user to purchase different company
	 * coupons from different companies. it uses the purchaseCoupon method in
	 * the couponDBDAO class the execute the purchase at the system.
	 * 
	 * @param coupon
	 *            - the coupon object representing the coupon that the customer
	 *            wants to purchasse.
	 */
	public void purchaseCoupon(Coupon coupon) {

		try {
			couponDBDAO.purchaseCoupon(coupon);

		} catch (DataNotExistException | DuplicateDataException | SQLException | GeneralErrorException
				| ShutDownException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This method get from the data base a list of all the purchased coupons of
	 * the customer logged in. It uses the getCoupons method from the
	 * customerDBDAO class.
	 * 
	 * @return customerDBDAO.getCoupons() - An array list of Coupon objects of
	 *         all the purchased coupons of that customer.
	 */
	public Collection<Coupon> getAllPurchasedCoupons() {

		try {
			return customerDBDAO.getCoupons();

		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	/**
	 * This method get from the data base a list of all the purchased coupons of
	 * the customer logged in. It uses the getCoupons method from the
	 * customerDBDAO class. After that it creates a new list of only those of
	 * the received specific type of coupons purchased by the customer.
	 * 
	 * @param type
	 *            - the specific coupon type we want for the type of coupons
	 *            that will be in the list.
	 * @return allPurchasedCouponsByType - An array list of Coupon objects of
	 *         all the purchased coupons of that customer that are only of the
	 *         received type.
	 */
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) {

		// lists of all the purchased coupons and list of all of them by type.
		Collection<Coupon> allPurchasedCoupons;
		Collection<Coupon> allPurchasedCouponsByType = new ArrayList<>();

		try {
			allPurchasedCoupons = customerDBDAO.getCoupons();

			for (Coupon coupon : allPurchasedCoupons) {
				if (coupon.getType().equals(type)) {
					allPurchasedCouponsByType.add(coupon);
				}
			}
		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}

		return allPurchasedCouponsByType;
	}

	/**
	 * This method get from the data base a list of all the purchased coupons of
	 * the customer logged in. It uses the getCoupons method from the
	 * customerDBDAO class. After that it creates a new list of only those at
	 * the received specific price or below it, of coupons purchased by the
	 * customer.
	 * 
	 * @param price
	 *            - the specific coupon price received that we want the coupons
	 *            that will be in the list to have. Or a price below it.
	 * @return allPurchasedCouponsByPrice - An array list of Coupon objects of
	 *         all the purchased coupons of that customer that are only at the
	 *         received price or below it.
	 */
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) {

		Collection<Coupon> allPurchasedCouponsByPrice = new ArrayList<>();
		Collection<Coupon> allPurchasedCoupons;
		try {
			allPurchasedCoupons = customerDBDAO.getCoupons();

			for (Coupon coupon : allPurchasedCoupons) {
				if (coupon.getPrice() <= price) {
					allPurchasedCouponsByPrice.add(coupon);
				}
			}
		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}

		return allPurchasedCouponsByPrice;
	}

	/**
	 * The login method for the customer client type user. After receiving the
	 * parameters, while the clientType is of customer kind, it calls the login
	 * method in customerDBDAO and there checks the credentials and if the login
	 * is successful, it receives true value in the result boolean and then
	 * returns CouponClientFacade of the type CustomerFacade and the user can
	 * start using the customer different methods. if the login is unsuccessful,
	 * the facade is not returned.
	 * 
	 * @param custName
	 *            - - the user name of the user customer that wants to log in.
	 * @param password
	 *            - the password of the user that wants to log in.
	 * @param clientType
	 *            - the client type of the user that wants to log in.
	 * @return login - returns the CouponClientFacade object of CustomerFacade
	 *         kind, or a null value.
	 */
	@Override
	public CouponClientFacade login(String custName, String password, ClientType clientType) throws SQLException {

		try {
			// result - the returned customer facade if login is successful.
			boolean result = customerDBDAO.login(custName, password);
			if (result) {
				return this;
			}
		} catch (LogInFailureException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
