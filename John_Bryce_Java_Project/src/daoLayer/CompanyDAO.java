package daoLayer;

import java.sql.SQLException;
import java.util.Collection;

import javaBeans.Company;
import javaBeans.Coupon;
import projectExceptions.DataNotExistException;
import projectExceptions.DuplicateDataException;
import projectExceptions.LogInFailureException;
import projectExceptions.ShutDownException;

/**
 * The interface that CompanyDBDAO implements from. it has all the crucial
 * company methods for communicating with the data base.
 *
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public interface CompanyDAO {

	// - Abstract methods to create, romove and etc company related actions.

	public void createCompany(Company company) throws SQLException, DuplicateDataException, ShutDownException;

	public void removeCompany(Company Company) throws SQLException, DataNotExistException, ShutDownException;

	public void updateCompany(Company Company) throws SQLException, DataNotExistException, ShutDownException;

	// - Abstract methods to get company related information such as getting
	// company, all companies or all company coupons.

	public Company getCompany(long id) throws SQLException, DataNotExistException, ShutDownException;

	public Collection<Company> getAllCompanies() throws SQLException, DataNotExistException, ShutDownException;

	public Collection<Coupon> getCoupons() throws SQLException, DataNotExistException, ShutDownException;

	// Abstarct login method, for login later use.

	public boolean login(String compName, String password)
			throws SQLException, LogInFailureException, ShutDownException;
}
