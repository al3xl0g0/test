package utils;

/**
 * This class is for the use of the connection pool. It is meant to make the
 * pool class more readable and sorted. It's visible to the pool by it's static
 * nature. It contains essential members for the pool.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class ConnectionData {

	// the driver of jdbc.
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	// the data base url.
	public static final String DB_URL = "jdbc:mysql://localhost/coupon_project?autoReconnect=true&useSSL=false";
	// - max number of connections .
	public static final int maxConnections = 10;

	// Database credentials
	public static final String USER = "dbuser";
	public static final String PASS = "dbpassword";

}
