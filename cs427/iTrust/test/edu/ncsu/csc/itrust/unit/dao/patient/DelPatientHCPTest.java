package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.action.ViewPreregisteredAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DelPatientHCPTest {

    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TestDataGenerator gen = new TestDataGenerator();
    private PatientDAO patientDAO = new PatientDAO(factory);

    @Before
    public void setUp() throws Exception {
        gen.clearAllTables();
        gen.preregisteredStandard(); //Adds standard & preregistered data
    }

    @Test
    public void delPatientHCPTest() throws DBException{
        patientDAO.setPatientHCP(1234L, 900000000);
        try {
            patientDAO.delPatientHCP(1234L);
        } catch (ITrustException e) {
            fail();
        }

        Connection conn = null;
        PreparedStatement ps = null;
        long hcp = -1L;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("SELECT HCPID FROM declaredhcp "
                    + "WHERE PatientID = 1234");
            ResultSet results = ps.executeQuery();
            assertEquals(false, results.next());
            results.close();
            ps.close();
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }
}
