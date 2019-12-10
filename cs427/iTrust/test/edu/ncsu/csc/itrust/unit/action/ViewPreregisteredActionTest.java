package edu.ncsu.csc.itrust.unit.action;

import edu.ncsu.csc.itrust.action.ViewPatientAction;
import edu.ncsu.csc.itrust.action.ViewPreregisteredAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ViewPreregisteredActionTest {

        private DAOFactory factory = TestDAOFactory.getTestInstance();
        private TestDataGenerator gen = new TestDataGenerator();
        private ViewPreregisteredAction action;

        @Before
        public void setUp() throws Exception {
            gen.clearAllTables();
            gen.preregisteredStandard(); //Adds standard & preregistered data
            action = new ViewPreregisteredAction(factory); //HCP1
        }

        @Test
        public void testGetViewablePatients() {
            List<PatientBean> list = null;
            try {
                list = action.getPreregisteredPatients();
            } catch (ITrustException e) {
                fail();
            }
            if(list == null){
                fail();
            } else {
                assertEquals(2, list.size());
            }
        }
    }
