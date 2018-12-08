package utils;

/**
 * This class is for creating customized exception constants for the use in the
 * code instead of rewriting the same strings code a lot of times and for easier
 * maintenance. It's for the coupon related exceptions.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CouponExceptionConstants {

	public static final String COUPON_DOES_NOT_EXISTS = "The coupon does not exists in the system.";
	public static final String NO_COUPONS_IN_THE_SYSTEM = "There are no coupons in the system";
	public static final String COUPON_ALREADY_EXISTS = "The coupon already exists in the system.";
	public static final String NO_COUPONS_OF_THIS_TYPE = "There are no coupons of this type in the system";
	public static final String COUPON_ALREADY_PURCHASED = "This coupon has been purchased already.";
}
