package edu.ncsu.csc.itrust.dao.mysql;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean;
import edu.ncsu.csc.itrust.beans.loaders.DiagnosisBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Used for managing diagnoses given during a particular office visit.
 *
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 *
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 *
 *
 *
 *
 */

public class DiagnosesDAO {
	private DAOFactory factory;
	private DiagnosisBeanLoader loader = new DiagnosisBeanLoader(true);

	/**
	 * @param factory
	 */
	public DiagnosesDAO(DAOFactory factory) {
		this.factory = factory;
	}


	/**
	 * @param visitID
	 * @return
	 * @throws DBException
	 */
	public List<DiagnosisBean> getList(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From ovdiagnosis,icdcodes Where ovdiagnosis.VisitID = ? "
					+ "AND icdcodes.Code=ovdiagnosis.ICDCode");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			List<DiagnosisBean> loadlist = loader.loadList(rs);
			rs.close();
			ps.close();
			return loadlist;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Gets a local zip code count and regional count of a specified diagnosis code
	 *
	 * @param icdCode The diagnosis code
	 * @param zipCode The zip code to evaluate
	 * @param lower The starting date
	 * @param upper The ending date
	 * @return A bean containing the local and regional counts
	 * @throws DBException
	 */
	public DiagnosisStatisticsBean getDiagnosisCounts(String icdCode, String zipCode, java.util.Date lower, java.util.Date upper) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		DiagnosisStatisticsBean dsBean = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ovdiagnosis INNER JOIN officevisits ON ovdiagnosis.VisitID=officevisits.ID INNER JOIN patients ON officevisits.PatientID=patients.MID WHERE ICDCode=? AND zip=? AND visitDate >= ? AND visitDate <= ? ");
			ps.setString(1, icdCode);
			ps.setString(2, zipCode);
			ps.setTimestamp(3, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(4, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));

			ResultSet rs = ps.executeQuery();
			rs.last();
			int local = rs.getRow();
			ps.close();
			ps = conn.prepareStatement("SELECT * FROM ovdiagnosis INNER JOIN officevisits ON ovdiagnosis.VisitID=officevisits.ID INNER JOIN patients ON officevisits.PatientID=patients.MID WHERE ICDCode=? AND zip LIKE ? AND visitDate >= ? AND visitDate <= ? ");
			ps.setString(1, icdCode);
			ps.setString(2, zipCode.substring(0, 3) + "%");
			ps.setTimestamp(3, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(4, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));

			rs = ps.executeQuery();
			rs.last();
			int region = rs.getRow();

			dsBean = new DiagnosisStatisticsBean(zipCode, local, region, lower, upper);
			rs.close();
			ps.close();
			return dsBean;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}

	}

	public DiagnosisStatisticsBean getDeathStatistics(java.util.Date lower, java.util.Date upper, String gender) {
		Connection conn = null;
		PreparedStatement ps = null;
		DiagnosisStatisticsBean dsBean = null;

		try {
			conn = factory.getConnection();
			String statement = "Select * From patients where patients.CauseOfDeath AND patients.DateOfDeath " +
					"AND patients.DateOfDeath >= ? AND patients.DateOfDeath <= ? ";
			if (gender.equals("Male")) {
				statement += "AND patients.Gender = \"Male\"";
			} else if (gender.equals("Female")) {
				statement += "AND patients.Gender = \"Female\"";
			}
			ps = conn.prepareStatement(statement);

			ps.setTimestamp(1, new Timestamp(lower.getTime()));
			ps.setTimestamp(2, new Timestamp(upper.getTime()));

			ResultSet rs = ps.executeQuery();

			HashMap<String, Integer> NumOfDeath = new HashMap<String, Integer>();

			while (rs.next()) {

				String result = (rs.getObject("CauseOfDeath")).toString();

				if (NumOfDeath.containsKey(result) == false ){
					NumOfDeath.put(result,1);
				}
				else if (NumOfDeath.containsKey(result) == true){
					NumOfDeath.put(result,NumOfDeath.get(result) + 1);
				}
			}

			int max1 = 0 ;
			int max2 = 0;

			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();

			for (String key: NumOfDeath.keySet()){
				int value = NumOfDeath.get(key);
				if( value > max1 ){
					max2 = max1;
					max1 = value;
				}
				else if ( value > max2){
					max2 = value;
				}
			}

			for (String key: NumOfDeath.keySet()){
				int value = NumOfDeath.get(key);
				if( value == max1 && sb1.length() == 0 ){
					sb1.append(key);
				}
				else if ( value == max2 && sb2.length() == 0 ){
					sb2.append(key);
				}
			}

			String top1 = (sb1.length() != 0) ? sb1.toString() + " ("+ Integer.toString(max1) + ")" : "NONE";
			String top2 = (sb2.length() != 0) ? sb2.toString() + " ("+ Integer.toString(max2) + ")" : "NONE";

			dsBean = new DiagnosisStatisticsBean(top1, top2, lower, upper);

			rs.close();
			ps.close();

			return dsBean;
		} catch (SQLException e) {

//			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}

		dsBean = new DiagnosisStatisticsBean("cause? @@@ cause?", (long)0.0, (long)0.0, lower, upper);
		return dsBean;
	}

	/**
	 * Gets a weekly local zip code count and regional count of a specified diagnosis code over a time period
	 *
	 * @param icdCode The diagnosis code
	 * @param zipCode The zip code to evaluate
	 * @param lower The starting date
	 * @param upper The ending date
	 * @return A list of beans containing the local and regional count for each week in the time period
	 * @throws DBException
	 */
	public ArrayList<DiagnosisStatisticsBean> getWeeklyCounts(String icdCode, String zipCode, java.util.Date lower, java.util.Date upper) throws DBException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(lower);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date lowerDate = cal.getTime();
		cal.add(Calendar.HOUR, 24*6);
		Date upperDate = cal.getTime();

		ArrayList<DiagnosisStatisticsBean> weekStats = new ArrayList<DiagnosisStatisticsBean>();

		do {
			DiagnosisStatisticsBean db = getDiagnosisCounts(icdCode, zipCode, lowerDate, upperDate);
			weekStats.add(db);

			cal.setTime(upperDate);
			cal.add(Calendar.HOUR, 24);
			lowerDate = cal.getTime();
			cal.add(Calendar.HOUR, 24*6);
			upperDate = cal.getTime();
		} while (lowerDate.before(upper));

		return weekStats;
	}

	public DiagnosisStatisticsBean getCountForWeekOf(String icdCode, String zipCode, java.util.Date lower) throws DBException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(lower);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date lowerDate = cal.getTime();
		cal.add(Calendar.HOUR, 24*6);
		Date upperDate = cal.getTime();

		return  getDiagnosisCounts(icdCode, zipCode, lowerDate, upperDate);
	}

	public Date findEarliestIncident(String icdCode) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT MIN(visitDate) AS visitDate FROM ovdiagnosis d INNER JOIN officevisits o " +
					" ON d.VisitID=o.ID " +
					" WHERE ICDCode LIKE ?");
			ps.setString(1, icdCode + "%");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Date date = rs.getDate("visitDate");
				rs.close();
				ps.close();
				return date;
			} else{
				rs.close();
				ps.close();
				return null;
			}
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}

	}

	/**
	 * Adds a diagnosis bean to the database.
	 * @param pres The prescription bean to be added.
	 * @return The unique ID of the newly added bean.
	 * @throws DBException
	 */
	public long add(DiagnosisBean bean) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			String statement = "INSERT INTO ovdiagnosis " +
					"(VisitID,ICDCode) VALUES (?,?)";
			ps = conn.prepareStatement(statement);
			ps.setLong(1, bean.getVisitID());
			ps.setString(2, bean.getICDCode());
			ps.executeUpdate();
			ps.close();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}


	/**
	 * Edits an existing prescription bean.
	 *
	 * @param pres The newly updated prescription bean.
	 * @return A long indicating the ID of the newly updated prescription bean.
	 * @throws DBException
	 */
	public long edit(DiagnosisBean bean) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			String statement = "UPDATE ovdiagnosis " +
					"SET VisitID=?, ICDCode=? " +
					"WHERE ID = ?";
			ps = conn.prepareStatement(statement);
			ps.setLong(1, bean.getVisitID());
			ps.setString(2, bean.getICDCode());
			ps.setLong(3, bean.getOvDiagnosisID());
			ps.executeUpdate();
			ps.close();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Removes the given diagnosis from its office visit
	 *
	 * @param ovMedicationID The unique ID of the medication to be removed.
	 * @throws DBException
	 */
	public void remove(long ovDiagnosisID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM ovdiagnosis WHERE ID=? ");
			ps.setLong(1, ovDiagnosisID);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}

