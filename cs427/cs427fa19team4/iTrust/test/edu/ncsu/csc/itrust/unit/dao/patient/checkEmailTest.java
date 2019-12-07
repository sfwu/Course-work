package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class checkEmailTest extends TestCase {
    PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
    TestDataGenerator gen = new TestDataGenerator();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.patient2(); // if it passes, need to test if empty
    }

    public void testGetPatient2() throws Exception {
        PatientBean p = patientDAO.getPatient(2);
        String email = p.getEmail();

        assertEquals(false,patientDAO.checkEmail(email));
        assertTrue(patientDAO.checkEmail("randomemail@yahoo.com"));
    }
}
