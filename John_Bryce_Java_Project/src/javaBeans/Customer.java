package javaBeans;

import java.util.Collection;

/**
 * This class is for creating the customer object to use with the different
 * customer related methods.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class Customer {

	// customer id.
	private long id;
	// customer name.
	private String custName;
	// customer password.
	private String password;
	// customer coupons.
	private Collection<Coupon> coupons;

	/**
	 * constructs the Customer object.
	 */
	public Customer() {

	}

	/**
	 * Getter method for the customer's id.
	 * 
	 * @return - id of the customer.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method for the customer's id.
	 * 
	 * @param id
	 *            - id of the customer.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter method for the customer's name.
	 *
	 * @return custName - the customer's name.
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * Setter method for the customer's name.
	 * 
	 * @param custName
	 *            - the customer's name
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * Getter method for the customer's password.
	 *
	 * @return password - the customer's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for the customer's password.
	 * 
	 * @param password
	 *            - the customer's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Receives a collection of the coupons of the customer.
	 * 
	 * @return coupons - all the coupons of the customer in a collection of
	 *         coupon objects.
	 */
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * sets the coupons list as the coupons of the customer.
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
		return "Customer [id=" + id + ", custName=" + custName + ", password=" + password + ", coupons=" + coupons
				+ "]";
	}
}
