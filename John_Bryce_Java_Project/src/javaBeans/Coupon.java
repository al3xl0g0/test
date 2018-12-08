package javaBeans;

import java.util.Date;

/**
 * This class is for creating the coupon object to use with the different
 * company and customer related methods.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class Coupon {

	// coupon id.
	private long id;
	// coupon title.
	private String title;
	// coupon start date.
	private Date startDate;
	// coupon end date.
	private Date endDate;
	// coupon amount.
	private int amount;
	// coupon type.

	private CouponType type;
	// coupon message.
	private String message;
	// coupon price.
	private double price;
	// coupon image.
	private String image;

	/**
	 * constructs the coupon object.
	 */
	public Coupon() {

	}

	/**
	 * Getter method for the coupon's id.
	 * 
	 * @return - id of the coupon.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method for the coupon's id.
	 * 
	 * @param id
	 *            - id of the coupon.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter method for the coupon's title.
	 *
	 * @return title - the coupon's title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the coupon's title.
	 * 
	 * @param title
	 *            - the coupon's title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter method for the coupon's start date.
	 *
	 * @return startDate - the coupon's start date.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter method for the coupon's start date.
	 *
	 * @param startDate
	 *            - the coupon's start date.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter method for the coupon's end date.
	 *
	 * @return endDate - the coupon's end date.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter method for the coupon's end date.
	 *
	 * @param endDate
	 *            - the coupon's end date.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter method for the coupon's amount left.
	 *
	 * @return amount - the coupon's amount left.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Setter method for the coupon's amount left.
	 *
	 * @param amount
	 *            - the coupon's left.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Getter method for the coupon's type.
	 *
	 * @return type - the coupon's type.
	 */
	public CouponType getType() {
		return type;
	}

	/**
	 * Setter method for the coupon's type.
	 *
	 * @param type
	 *            - the coupon's type.
	 */
	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * Getter method for the coupon's message.
	 *
	 * @return message - the coupon's message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter method for the coupon's message.
	 *
	 * @param message
	 *            - the coupon's message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter method for the coupon's price.
	 *
	 * @return price - the coupon's price.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Setter method for the coupon's price.
	 *
	 * @param price
	 *            - the coupon's price.
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Getter method for the coupon's image.
	 *
	 * @return image - the coupon's image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Setter method for the coupon's image.
	 *
	 * @param image
	 *            - the coupon's image.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * method for printing to get the object's description.
	 * 
	 * @return string - a long string of all the fields names and their values.
	 */
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image="
				+ image + "]";
	}
}
