package daoLayer;

import java.sql.SQLException;
import java.util.Collection;

import javaBeans.Coupon;
import javaBeans.CouponType;
import projectExceptions.DataNotExistException;
import projectExceptions.DuplicateDataException;
import projectExceptions.ShutDownException;

/**
 * The interface that CouponDBDAO implements from. it has all the crucial coupon
 * related methods for communicating with the data base.
 *
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public interface CouponDAO {

	// - Abstract methods to create, romove and etc coupon related actions.

	public void createCoupon(Coupon coupon) throws SQLException, DuplicateDataException, ShutDownException;

	public void removeCoupon(Coupon coupon) throws SQLException, DataNotExistException, ShutDownException;

	public void updateCoupon(Coupon coupon) throws SQLException, DataNotExistException, ShutDownException;

	// - Abstract methods to get coupon related information such as getting
	// coupon, all coupons or all coupons by type .

	public Coupon getCoupon(long id) throws SQLException, DataNotExistException, ShutDownException;

	public Collection<Coupon> getAllCoupon() throws SQLException, DataNotExistException, ShutDownException;

	public Collection<Coupon> getCouponByType(CouponType couponType)
			throws SQLException, DataNotExistException, ShutDownException;

}
