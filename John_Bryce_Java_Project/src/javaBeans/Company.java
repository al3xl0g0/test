package javaBeans;

import java.util.Collection;

/**
 * This class is for creating the company object to use with the different
 * company related methods.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class Company {

	// id of company.
	private long id;
	// Company name.
	private String compName;
	// company password.
	private String password;
	// company email.
	private String email;
	// company coupons.
	private Collection<Coupon> coupons;

	/**
	 * constructs the company object.
	 */
	public Company() {

	}

	/**
	 * Getter method for the company's id.
	 * 
	 * @return - id of the company.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method for the company's id.
	 * 
	 * @param id
	 *            - id of the company.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter method for the company's name.
	 *
	 * @return compName - the company's name.
	 */
	public String getCompName() {
		return compName;
	}

	/**
	 * Setter method for the company's name.
	 * 
	 * @param compName
	 *            - the company's name
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}

	/**
	 * Getter method for the company's password.
	 *
	 * @return password - the company's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for the company's password.
	 * 
	 * @param password
	 *            - the company's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter method for the company's email.
	 *
	 * @return email - the company's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter method for the company's email.
	 * 
	 * @param email
	 *            - the company's email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Receives a collection of the coupons of the company.
	 * 
	 * @return coupons - all the coupons of the company in a collection of
	 *         coupon objects.
	 */
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * sets the coupons list as the coupons of the company.
	 * 
	 * @param coupons
	 *            - collection of coupon objects to set as the company's
	 *            coupons.
	 */
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * method for printing to get the object's description.
	 * 
	 * @return string - a long string of all the fields names and their values.
	 */
	@Override
	public String toString() {
		return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email
				+ ", coupons=" + coupons + "]";
	}
}
