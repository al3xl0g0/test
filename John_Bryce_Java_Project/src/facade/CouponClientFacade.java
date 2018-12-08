package facade;

import java.sql.SQLException;

import javaBeans.ClientType;

/**
 * This interface is used for the different login methods. all of the facades
 * implements it, and there for have to instantiate them.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public interface CouponClientFacade {

	public CouponClientFacade login(String name, String password, ClientType clientType) throws SQLException;
}
