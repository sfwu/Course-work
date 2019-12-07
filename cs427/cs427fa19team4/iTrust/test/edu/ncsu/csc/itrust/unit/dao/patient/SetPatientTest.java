package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PatientHistoryBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.enums.Ethnicity;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class SetPatientTest extends TestCase {
    PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
    TestDataGenerator gen = new TestDataGenerator();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.patient2(); // if it passes, need to test if empty
    }

    public void testGetPatient2() throws Exception {
        PatientBean p = new PatientBean();
        p.setFirstName("Person2");
        p.setLastName("Test");
        p.setEmail("another email");
        p.setStreetAddress1("addr1");
        p.setStreetAddress2("addr2");
        p.setCity("City");
        p.setState("AL");
        p.setZip("12345");
        p.setPhone("123");
        p.setIcName("Name");
        p.setIcAddress1("icaddr1");
        p.setIcAddress2("icaddr2");
        p.setIcCity("iccity");
        p.setIcState("IL");
        p.setIcZip("098");
        p.setIcPhone("5678");
        long mid = patientDAO.setPatient(p);

        p = patientDAO.getPatient(mid);
        assertEquals("Person2", p.getFirstName());
        assertEquals("Test", p.getLastName());
        assertEquals("another email", p.getEmail());

        assertEquals("addr1", p.getStreetAddress1());
        assertEquals("addr2", p.getStreetAddress2());
        assertEquals("City", p.getCity());
        assertEquals("AL", p.getState());
        assertEquals("12345", p.getZip());
        assertEquals("123", p.getPhone());
        assertEquals("Name", p.getIcName());
        assertEquals("icaddr1", p.getIcAddress1());
        assertEquals("icaddr2", p.getIcAddress2());
        assertEquals("iccity", p.getIcCity());
        assertEquals("IL", p.getIcState());
        assertEquals("098", p.getIcZip());
        assertEquals("5678", p.getIcPhone());
        assertEquals(true, p.getPreregistered());
        assertEquals("", p.getCauseOfDeath());
        assertNotNull(p);
    }
}
