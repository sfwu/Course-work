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


		// Fill in all of the info and submit
		WebElement newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("5");
		driver.findElement(By.name("submitNew")).submit();

		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());

        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("row1C1"));
        assertEquals("08/08/2010", valDate.getText());

        WebElement valName = driver.findElement(By.name("row1C2"));
        assertEquals("poinson", valName.getText());

        WebElement valNum = driver.findElement(By.name("row1C3"));
        assertEquals("5", valNum.getText());
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


		// Fill in all of the info and submit
		WebElement newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("5");
        driver.findElement(By.name("submitNew")).submit();

		//add one more

		// Fill in all of the info and submit
		newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/20/2010");
        
        // enter the type of the pill
        newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson1");

        //enter the number that the patient has to take
		newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("8");
		driver.findElement(By.name("submitNew")).submit();

		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());

        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("row1C1"));
        assertEquals("08/08/2010", valDate.getText());

        WebElement valName = driver.findElement(By.name("row1C2"));
        assertEquals("poinson", valName.getText());

        WebElement valNum = driver.findElement(By.name("row1C3"));
        assertEquals("5", valNum.getText());

        // check the later added one 
        valDate = driver.findElement(By.name("row2C1"));
        assertEquals("08/20/2010", valDate.getText());

        valName = driver.findElement(By.name("row2C2"));
        assertEquals("poinson1", valName.getText());

        valNum = driver.findElement(By.name("row2C3"));
        assertEquals("8", valNum.getText());

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
        

		// Fill in all of the info and submit
		WebElement newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("5");
        driver.findElement(By.name("submitNew")).submit();


        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("curRow1C1"));
        assertEquals("08/08/2010", valDate.getText());

        WebElement valName = driver.findElement(By.name("curRow1C2"));
        assertEquals("poinson", valName.getText());

        WebElement valNum = driver.findElement(By.name("curRow1C3"));
        assertEquals("0", valNum.getText());

        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("1", valNum.getText());
        
        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("2", valNum.getText());

        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("3", valNum.getText());
        

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


		// Fill in all of the info and submit
		WebElement newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson");

        //enter the number that the patient has to take
		WebElement newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("5");
        driver.findElement(By.name("submitNew")).submit();


        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		WebElement valDate = driver.findElement(By.name("CurDate:0"));
        assertEquals("08/08/2010", valDate.getText());

        WebElement valName = driver.findElement(By.name("CurName:0"));
        assertEquals("poinson", valName.getText());

        WebElement valNum = driver.findElement(By.name("curNum:0"));
        assertEquals("0", valNum.getText());

        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("1", valNum.getText());
        
        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("2", valNum.getText());

        driver.findElement(By.linkText("addNum1"))
                .click();
        assertEquals("3", valNum.getText());
        
        driver.findElement(By.linkText("minusNum1"))
                .click();
        assertEquals("2", valNum.getText());

        driver.findElement(By.linkText("minusNum1"))
                .click();
        assertEquals("1", valNum.getText());

        driver.findElement(By.linkText("minusNum1"))
                .click();
        assertEquals("0", valNum.getText());
        

	}

	/**
	 * Tests that a patient can click "add" to empty Prescription Diary.
	 * 
	 * @throws Exception
	 */
	public void testDeletePrescriptionEntryFromPrescriptionDiary() throws Exception {

        // login
		WebDriver driver = new HtmlUnitDriver(true);
		driver = login("1", "pw"); 

		// View my prescription diary so we can add a new entry
		driver.findElement(By.linkText("My Prescription Diary")).click();
		assertEquals("iTrust - View My Prescription Diary", driver.getTitle());


		// Fill in all of the info and submit
		WebElement newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("08/08/2010");
        
        // enter the type of the pill
        WebElement newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson1");

        //enter the number that the patient has to take
		WebElement newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("5");
		driver.findElement(By.name("submitNew")).submit();

		//add second column

		// Fill in all of the info and submit
		newDate = driver.findElement(By.name("newDate"));
		newDate.clear();
		newDate.sendKeys("10/10/2010");
        
        // enter the type of the pill
        newPillType = driver.findElement(By.name("newPillType"));
		newPillType.clear();
		newPillType.sendKeys("poinson2");

        //enter the number that the patient has to take
		newTakenNum = driver.findElement(By.name("newTakenNum"));
		newTakenNum.clear();
		newTakenNum.sendKeys("10");

		driver.findElement(By.name("submitNew")).submit();

		// now delete the first row, the second row will then become the first row

		driver.findElement(By.name("delete1")).submit();

		WebElement valDate = driver.findElement(By.name("curRow1C1"));
        assertEquals("10/10/2010", valDate.getText());

        WebElement valName = driver.findElement(By.name("curRow1C2"));
        assertEquals("poinson2", valName.getText());

        WebElement valNum = driver.findElement(By.name("curRow1C3"));
        assertEquals("0", valNum.getText());

        // Ensure the entry was added.
        // :0 mean the 0 row of the table
		valDate = driver.findElement(By.name("row1C1"));
        assertEquals("10/10/2010", valDate.getText());

        valName = driver.findElement(By.name("row1C2"));
        assertEquals("poinson2", valName.getText());

        valNum = driver.findElement(By.name("row1C3"));
        assertEquals("10", valNum.getText());
	}
	
}