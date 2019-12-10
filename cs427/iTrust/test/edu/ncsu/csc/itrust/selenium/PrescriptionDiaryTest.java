package edu.ncsu.csc.itrust.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import edu.ncsu.csc.itrust.action.AddSleepEntryAction;
import edu.ncsu.csc.itrust.action.DeleteSleepEntryAction;
import edu.ncsu.csc.itrust.action.ViewSleepEntryAction;
import edu.ncsu.csc.itrust.beans.SleepEntryBean;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * Tests the functionality of the new Sleep Diary portion of iTrust. Tests
 * that you can add new entries, you can view entries, HCPs with the role of
 * trainer can view entries, and error checks that the user enters the
 * appropriate data in the appropriate format.
 */
public class PrescriptionDiaryTest extends iTrustSeleniumTest {

	/**
	 * Sets up the standard testing data.
	 */
	public void setUp() throws Exception {
		super.setUp();
		gen.standardData();
	}

	/**
	 * Sanity check.
	 */
	public void tearDown() throws Exception {
		gen.clearAllTables();
	}

	/**
	 * Tests that a patient can click "add" to empty Prescription Diary.
	 * 
	 * @throws Exception
	 */
	public void testAddPrescriptionEntryToEmptyPrescriptionDiary() throws Exception {

        // login
		WebDriver driver = new HtmlUnitDriver(true);
		driver = login("1", "pw"); 

		// View my prescription diary so we can add a new entry
		driver.findElement(By.linkText("My Prescription Diary")).click();
		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());
		driver.findElement(By.linkText("Add an entry to your Prescription Diary."))
				.click();

        // check the page name 
		assertEquals("Add a Prescription Entry", driver.getTitle());

		// Fill in all of the info and submit
		WebElement strDate = driver.findElement(By.name("strDate"));
		strDate.clear();
		strDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement strPillName = driver.findElement(By.name("strPillName"));
		strPillName.clear();
		strPillName.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement strNum = driver.findElement(By.name("strNum"));
		strNum.clear();
		strNum.sendKeys("5");
		driver.findElement(By.tagName("form")).submit();

		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());

        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("givenDate:0"));
        assertEquals("08/08/2010", valDate.getAttribute(VALUE));

        WebElement valName = driver.findElement(By.name("givenName:0"));
        assertEquals("poinson", valName.getAttribute(VALUE));

        WebElement valNum = driver.findElement(By.name("givenNum:0"));
        assertEquals("5", valNum.getAttribute(VALUE));
	}


	/**
	 * Tests that a patient can click "add" to nonempty Prescription Diary.
	 * 
	 * @throws Exception
	 */
	public void testAddPrescriptionEntryToNonEmptyPrescriptionDiary() throws Exception {

        // login
		WebDriver driver = new HtmlUnitDriver(true);
		driver = login("1", "pw"); 

		// View my prescription diary so we can add a new entry
		driver.findElement(By.linkText("My Prescription Diary")).click();
		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());
		driver.findElement(By.linkText("Add an entry to your Prescription Diary."))
				.click();

        // check the page name 
		assertEquals("Add a Prescription Entry", driver.getTitle());

		// Fill in all of the info and submit
		WebElement strDate = driver.findElement(By.name("strDate"));
		strDate.clear();
		strDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement strPillName = driver.findElement(By.name("strPillName"));
		strPillName.clear();
		strPillName.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement strNum = driver.findElement(By.name("strNum"));
		strNum.clear();
		strNum.sendKeys("5");
        driver.findElement(By.tagName("form")).submit();

        // Now we add onemore
        driver.findElement(By.linkText("Add an entry to your Prescription Diary."))
				.click();
        // check the page name 
		assertEquals("Add a Prescription Entry", driver.getTitle());

		// Fill in all of the info and submit
		strDate = driver.findElement(By.name("strDate"));
		strDate.clear();
		strDate.sendKeys("08/20/2010");
        
        // enter the type of the pill
        strPillName = driver.findElement(By.name("strPillName"));
		strPillName.clear();
		strPillName.sendKeys("poinson1");

        //enter the number that the patient has to take
		strNum = driver.findElement(By.name("strNum"));
		strNum.clear();
		strNum.sendKeys("8");
		driver.findElement(By.tagName("form")).submit();

		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());

        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("givenDate:0"));
        assertEquals("08/08/2010", valDate.getAttribute(VALUE));

        WebElement valName = driver.findElement(By.name("givenName:0"));
        assertEquals("poinson", valName.getAttribute(VALUE));

        WebElement valNum = driver.findElement(By.name("givenNum:0"));
        assertEquals("5", valNum.getAttribute(VALUE));

        // check the later added one 
        valDate = driver.findElement(By.name("givenDate:1"));
        assertEquals("08/20/2010", valDate.getAttribute(VALUE));

        valName = driver.findElement(By.name("givenName:1"));
        assertEquals("poinson1", valName.getAttribute(VALUE));

        valNum = driver.findElement(By.name("givenNum:1"));
        assertEquals("8", valNum.getAttribute(VALUE));

	}

    /**
	 * Tests that a patient can click "add" to add one to the num of taken pull of the Prescription Diary.
	 * 
	 * @throws Exception
	 */
	public void testAddTakenNumOfPill() throws Exception {

        // login
		WebDriver driver = new HtmlUnitDriver(true);
		driver = login("1", "pw"); 

		// View my prescription diary so we can add a new entry
		driver.findElement(By.linkText("My Prescription Diary")).click();
        assertEquals("iTrust - View My Prescription Diary", driver.getTitle());
        
        driver.findElement(By.linkText("Add an entry to your Prescription Diary."))
				.click();

        // check the page name 
		assertEquals("Add a Prescription Entry", driver.getTitle());

		// Fill in all of the info and submit
		WebElement strDate = driver.findElement(By.name("strDate"));
		strDate.clear();
		strDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement strPillName = driver.findElement(By.name("strPillName"));
		strPillName.clear();
		strPillName.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement strNum = driver.findElement(By.name("strNum"));
		strNum.clear();
		strNum.sendKeys("5");
        driver.findElement(By.tagName("form")).submit();


        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("CurDate:0"));
        assertEquals("08/08/2010", valDate.getAttribute(VALUE));

        WebElement valName = driver.findElement(By.name("CurName:0"));
        assertEquals("poinson", valName.getAttribute(VALUE));

        WebElement valNum = driver.findElement(By.name("curNum:0"));
        assertEquals("0", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("1", valNum.getAttribute(VALUE));
        
        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("2", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("3", valNum.getAttribute(VALUE));
        

    }
    
    /**
	 * Tests that a patient can click "minus" to subtract one to the num of taken pull of the Prescription Diary.
	 * 
	 * @throws Exception
	 */
	public void testMinusTakenNumOfPill() throws Exception {

        // login
		WebDriver driver = new HtmlUnitDriver(true);
		driver = login("1", "pw"); 

		// View my prescription diary so we can add a new entry
		driver.findElement(By.linkText("My Prescription Diary")).click();
        assertEquals("iTrust - View My Prescription Diary", driver.getTitle());
        
        driver.findElement(By.linkText("Add an entry to your Prescription Diary."))
				.click();

        // check the page name 
		assertEquals("Add a Prescription Entry", driver.getTitle());

		// Fill in all of the info and submit
		WebElement strDate = driver.findElement(By.name("strDate"));
		strDate.clear();
		strDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement strPillName = driver.findElement(By.name("strPillName"));
		strPillName.clear();
		strPillName.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement strNum = driver.findElement(By.name("strNum"));
		strNum.clear();
		strNum.sendKeys("5");
        driver.findElement(By.tagName("form")).submit();


        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("CurDate:0"));
        assertEquals("08/08/2010", valDate.getAttribute(VALUE));

        WebElement valName = driver.findElement(By.name("CurName:0"));
        assertEquals("poinson", valName.getAttribute(VALUE));

        WebElement valNum = driver.findElement(By.name("curNum:0"));
        assertEquals("0", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("1", valNum.getAttribute(VALUE));
        
        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("2", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("addNum"))
                .click();
        assertEquals("3", valNum.getAttribute(VALUE));
        
        driver.findElement(By.linkText("minusNum"))
                .click();
        assertEquals("2", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("minusNum"))
                .click();
        assertEquals("1", valNum.getAttribute(VALUE));

        driver.findElement(By.linkText("minusNum"))
                .click();
        assertEquals("0", valNum.getAttribute(VALUE));
        

	}


	
}