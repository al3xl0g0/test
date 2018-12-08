package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import projectExceptions.ShutDownException;
import utils.ConnectionData;
import utils.GeneralExceptionConstants;

/**
 * This class is the connection pool, that holds all of the connections for
 * communicating with the data base. There are 5 max connection in the pool, the
 * class is singleton for multi thread use.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class ConnectionPool {

	// conn - empty connection object . stmt - empty statement object.

	Connection conn = null;
	Statement stmt = null;

	// key object to lock and release thread's access for a multi threaded
	// program support.
	public Object key = new Object();
	// arConnection - array list for the connections of the pool.
	private ArrayList<Connection> arConnection = new ArrayList<>();
	// INSTANCE - the connection pool instance.
	private static ConnectionPool INSTANCE = null;
	// shutDownActivated - turns true when shut down is activated to prevent
	// further use of connections.
	private boolean shutDownActivated = false;

	/**
	 * Constructs the class of the connection pool, while using the jdbc driver,
	 * and creating all of the connection in the pool.
	 * 
	 * @throws ClassNotFoundException
	 *             - when an application tries to load in a class through its
	 *             string name, but no definition for the class with the
	 *             specified name could be found.
	 */
	private ConnectionPool() throws ClassNotFoundException {
		// The driver for the JDBC connection to the data base.
		Class.forName(ConnectionData.JDBC_DRIVER);

		for (int i = 0; i < ConnectionData.maxConnections; i++) {
			try {
				// Driver and connection related member of url of the data base
				// and the user and password to it.
				Connection newConnection = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER,
						ConnectionData.PASS);
				arConnection.add(newConnection);
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Creating the pool instance or getting the pool instance (while it is
	 * already created).
	 * 
	 * @return INSTANCE - the connection pool instance.
	 */
	public static ConnectionPool getInstance() {
		synchronized (ConnectionPool.class) {
			if (INSTANCE == null) {
				try {
					// instance - the created connection pool instance.
					INSTANCE = new ConnectionPool();
				} catch (ClassNotFoundException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * Getting a connection (to communicate later with the data base) from the
	 * connection pool. Only while there is a connection left in the pool and
	 * shutdown has not been executed.
	 * 
	 * @return resultConnection - connection that the method got from the pool.
	 * @throws ShutDownException
	 *             - in cases which the system is shutting down while there is
	 *             attempt to do use of the program.
	 */
	public Connection getConnection() throws ShutDownException {
		// resultConnection - the connection the will be returned for use with
		// the data base.
		Connection resultConnection = null;
		if (!shutDownActivated) {
			try {
				synchronized (key) {
					while (arConnection.isEmpty()) {
						key.wait();
					}
					if (!shutDownActivated) {
						resultConnection = arConnection.get(0);
						arConnection.remove(0);
					} else {
						throw new ShutDownException(GeneralExceptionConstants.SYSTEM_SHUTTING_DOWN);
					}
				}
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		} else {
			throw new ShutDownException(GeneralExceptionConstants.SYSTEM_SHUTTING_DOWN);
		}
		return resultConnection;
	}

	/**
	 * Returning a connection that the use of it is over, back to the pool.
	 * 
	 * @param conn
	 *            - the connection to return to the pool.
	 */
	public void returnConnection(Connection conn) {
		synchronized (key) {
			arConnection.add(conn);
			key.notify();
		}
	}

	/**
	 * Closing all of the connections in the pool. First not allowing others to
	 * get new connections any more. Second before closing making sure all of
	 * the connections are in the pool.
	 * 
	 * @throws SQLException
	 *             - When database access error or other errors occur.
	 */
	public void closeAllConnections() throws SQLException {
		// shutDownActivated - boolean that indicates if shut down was
		// activated.
		shutDownActivated = true;
		if (arConnection.size() != ConnectionData.maxConnections) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}

		for (Connection connection : arConnection) {
			connection.close();
		}
	}

}
