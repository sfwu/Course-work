package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.DBUtil;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SetPreregisteredActivatedTest {

        private DAOFactory factory = TestDAOFactory.getTestInstance();
        private TestDataGenerator gen = new TestDataGenerator();
        private PatientDAO patientDAO = new PatientDAO(factory);

        @Before
        public void setUp() throws Exception {
            gen.clearAllTables();
            gen.preregisteredStandard(); //Adds standard & preregistered data
        }

        @Test
        public void setPreregisteredActivatedTest() throws DBException {
            try {
                patientDAO.setPreregisteredActivated(1234L);
            } catch (ITrustException e) {
                fail();
            }

            Connection conn = null;
            PreparedStatement ps = null;
            int activated = -1;
            int preregistered = -1;
            try {
                conn = factory.getConnection();
                ps = conn.prepareStatement("SELECT preregistered, activated FROM patients "
                        + "WHERE MID = 1234");
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    preregistered = results.getInt(1);
                    activated = results.getInt(2);
                }

                results.close();
                ps.close();
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                DBUtil.closeConnection(conn, ps);
            }
            assertEquals(1, activated);
            assertEquals(0, preregistered);

        }
    }