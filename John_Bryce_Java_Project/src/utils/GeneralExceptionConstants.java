package utils;

public class GeneralExceptionConstants {
	/**
	 * This class is for creating customized exception constants for the use in
	 * the code instead of rewriting the same strings code a lot of times and
	 * for easier maintenance. It's for the customer related exceptions. It's
	 * for general related exceptions.
	 * 
	 * @author Ehud Perlman
	 * @version 1.0 February 20, 2017.
	 */
	public static final String ADMIN_LOGIN_FAILED = "Admin Login Faliure.";
	public static final String CANNOT_PURCHASE_COUPON = "cannot purchase coupon, no coupons left or coupon date expired";
	public static final String SYSTEM_SHUTTING_DOWN = "Service is not avaliable, system shutting down.";
	public static final String NO_COUPONS_BY_PRICE = "There are no coupons in this price or under this price.";
	public static final String NO_COUPONS_INSIDE_DATE = "There are no coupons inside this date timeline.";
}
