package facade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import daoLayer.CompanyDBDAO;
import daoLayer.CouponDBDAO;
import javaBeans.ClientType;
import javaBeans.Coupon;
import javaBeans.CouponType;
import projectExceptions.DuplicateDataException;
import projectExceptions.GeneralErrorException;
import projectExceptions.LogInFailureException;
import projectExceptions.ShutDownException;
import utils.GeneralExceptionConstants;
import projectExceptions.DataNotExistException;

/**
 * This class is a part of the facade pattern layer. The class which manages the
 * company user's request for using methods with company functionality after a
 * successful login. The class uses the methods of the CompanyDBDAO and the
 * Coupon DBDAO classes - which communicates with the data base. In addition,
 * this class also catches any related exceptions that might occur.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CompanyFacade implements CouponClientFacade {

	// creating the classes for use of their methods.
	private CompanyDBDAO companyDBDAO;
	private CouponDBDAO couponDBDAO;

	/**
	 * Constructs the class of the company facade and creating new relevant
	 * DBDAO objects for the use of the facade with the DBDAO objects.
	 */
	public CompanyFacade() {

		companyDBDAO = new CompanyDBDAO();
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * This method activates the createCoupon method in the couponDBDAO class
	 * while sending the received coupon parameter, eventually creating the
	 * coupon's record into the Mysql data base. The coupon is recorded as the
	 * coupon of the logged in company.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to eventually add to the data base.
	 */
	public void createCoupon(Coupon coupon) {

		try {
			couponDBDAO.createCoupon(coupon);

		} catch (DuplicateDataException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This method activates the removeCoupon method in the couponDBDAO class
	 * while sending the received coupon parameter, eventually removing the
	 * coupon's record from the Mysql data base.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to eventually remove from the data base.
	 */
	public void removeCoupon(Coupon coupon) {

		try {
			couponDBDAO.removeCoupon(coupon);

		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This method activates the updateCoupon method in the couponDBDAO class
	 * while sending the received coupon parameter, eventually updating the
	 * coupon's record in the Mysql data base.
	 * 
	 * @param coupon
	 *            - the coupon object that is filled with the fields of the
	 *            coupon to eventually update in data base.
	 */
	public void updateCoupon(Coupon coupon) {

		try {
			couponDBDAO.updateCoupon(coupon);

		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This method activates the getCoupon method in the couponDBDAO class while
	 * sending the received coupon id parameter, eventually getting and
	 * returning a Coupon object.
	 * 
	 * @param id
	 *            - the id of the coupon, like it's on the data base, so that we
	 *            will receive a coupon object from the record in the data base
	 *            by it's id.
	 * @return couponDBDAO.getCoupon(id) - returning a Coupon object from the
	 *         coupon's record in the Mysql data base.
	 */
	public Coupon getCoupon(long id) {

		try {
			return couponDBDAO.getCoupon(id);

		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	/**
	 * This method activates the getAllCoupons method in the couponDBDAO,
	 * eventually getting and returning an Array List of Coupon objects.
	 * 
	 * @return companyDBDAO.getCoupons() - returning an Array List of all the
	 *         Coupon objects of the logged in company from records in the Mysql
	 *         data base
	 */
	public Collection<Coupon> getAllCoupons() {

		try {
			return companyDBDAO.getCoupons();

		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());

			return null;
		}
	}

	/**
	 * The method uses the getCoupons method in the companyDBDAO, so it will
	 * receive all the coupons of the company, and after that, will make a new
	 * Array list of only the company coupons of the specific entered type.
	 * 
	 * @param ct
	 *            - the coupon type that the method will use, to get only
	 *            company coupons of this type.
	 * @return allCouponsByType - the array list of Coupon of all the companys
	 *         coupons of this type only.
	 */
	public Collection<Coupon> getCouponsByType(CouponType ct) {

		// allCouponsByType - list of all of them by type.
		Collection<Coupon> allCouponsByType = null;
		// allcoupons - list of all of them.
		Collection<Coupon> allCoupons;
		try {
			allCoupons = companyDBDAO.getCoupons();
			allCouponsByType = new ArrayList<>();

			for (Coupon coupon : allCoupons) {
				if (coupon.getType().equals(ct)) {
					allCouponsByType.add(coupon);
				}
			}
		} catch (DataNotExistException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}

		return allCouponsByType;
	}

	/**
	 * The method uses the getCoupons method in the companyDBDAO, so it will
	 * receive all the coupons of the company, and after that, will make a new
	 * Array list of only the company's coupons at the specific price, or below
	 * it.
	 * 
	 * @param price
	 *            - the coupon price that the method will use, to get only
	 *            company coupons at this price, or below it.
	 * @return allCompanyCouponsByPrice - the array list of Coupon of all the
	 *         company's coupons of at this price or below it.
	 * 
	 */
	public Collection<Coupon> getCouponsByPrice(double price) {

		// lists of all the company coupons, and the list of all the coupons by
		// price.
		Collection<Coupon> allCompanyCouponsByPrice = new ArrayList<>();
		Collection<Coupon> allCompanyCoupons;

		try {
			allCompanyCoupons = companyDBDAO.getCoupons();

			for (Coupon coupon : allCompanyCoupons) {
				if (coupon.getPrice() <= price) {
					allCompanyCouponsByPrice.add(coupon);
				}
			}
			if (allCompanyCouponsByPrice.isEmpty()) {
				throw new GeneralErrorException(GeneralExceptionConstants.NO_COUPONS_BY_PRICE);
			}
		} catch (DataNotExistException | SQLException | ShutDownException | GeneralErrorException e) {
			System.err.println(e.getMessage());
		}

		return allCompanyCouponsByPrice;
	}

	/**
	 * The method uses the getCoupons method in the companyDBDAO, so it will
	 * receive all the coupons of the company, and after that, will make a new
	 * Array list of only the company's coupons that their end date is at the
	 * date received or before it.
	 * 
	 * @param date
	 *            - the coupon's date that the method will use, to get only
	 *            company coupons that their end date is at this date, or before
	 *            it.
	 * @return allCompanyCouponsByDate - the array list of Coupon of all the
	 *         company's coupons that that their end date is at this date, or
	 *         before it.
	 * 
	 */
	public Collection<Coupon> getCouponsByDate(Date date) {

		Collection<Coupon> allCompanyCouponsByDate = new ArrayList<>();
		Collection<Coupon> allCompanyCoupons;
		try {
			allCompanyCoupons = companyDBDAO.getCoupons();

			for (Coupon coupon : allCompanyCoupons) {
				if (coupon.getEndDate().before(date) || coupon.getEndDate().equals(date)) {
					allCompanyCouponsByDate.add(coupon);
				}
			}
			if (allCompanyCouponsByDate.isEmpty()) {
				throw new GeneralErrorException(GeneralExceptionConstants.NO_COUPONS_INSIDE_DATE);
			}
		} catch (DataNotExistException | SQLException | ShutDownException | GeneralErrorException e) {
			System.err.println(e.getMessage());
		}

		return allCompanyCouponsByDate;
	}

	/**
	 * The login method for the company client type user. After receiving the
	 * parameters, while the clientType is of company kind, it calls the login
	 * method in companyDBDAO and there checks the credentials and if the login
	 * is successful, it receives true value in the result boolean and then
	 * returns CouponClientFacade of the type CompanyFacade and the user can
	 * start using the company different methods. if the login is unsuccessful,
	 * the facade is not returned.
	 * 
	 * @param compName
	 *            - - the user name of the user company that wants to log in.
	 * @param password
	 *            - the password of the user that wants to log in.
	 * @param clientType
	 *            - the client type of the user that wants to log in.
	 * @return login - returns the CouponClientFacade object of CompanyFacade
	 *         kind, or a null value.
	 */
	@Override
	public CouponClientFacade login(String compName, String password, ClientType clientType) {

		try {
			// result - the facade of company if the login successful.
			boolean result = companyDBDAO.login(compName, password);
			if (result) {
				System.out.println("login success.");

				return this;
			}
		} catch (LogInFailureException | SQLException | ShutDownException e) {
			System.err.println(e.getMessage());
		}

		return null;
	}
}
