package utils;

/**
 * This class is for creating customized exception constants for the use in the
 * code instead of rewriting the same strings code a lot of times and for easier
 * maintenance. It's for the company related exceptions.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class CompanyExceptionConstants {

	
	public static final String COMPANY_LOGIN_FAILED = "Company Login Faliure.";
	public static final String COMPANY_DOES_NOT_EXISTS = "The company does not exists in the system.";
	public static final String COMPANY_DOES_NOT_HAVE_COUPONS = "This company does not have coupons in the system.";
	public static final String NO_COMPANIES_IN_THE_SYSTEM = "There are no companies in the system";
	public static final String COMPANY_ALREADY_EXISTS = "The company already exists in the system.";
}
