package utils;

/**
 * This class is for creating customized exception constants for the use in the
 * code instead of rewriting the same strings code a lot of times and for easier
 * maintenance. It's for the customer related exceptions
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CustomerExceptionConstants {

	public static final String CUSTOMER_LOGIN_FAILED = "Customer Login Faliure.";
	public static final String CUSTOMER_DOES_NOT_EXISTS = "The customer does not exists in the system.";
	public static final String CUSTOMER_DOES_NOT_HAVE_COUPONS = "This customer does not have coupons in the system.";
	public static final String NO_CUSTOMERS_IN_THE_SYSTEM = "There are no customers in the system";
	public static final String CUSTOMER_ALREADY_EXISTS = "The customer already exists in the system.";
}
