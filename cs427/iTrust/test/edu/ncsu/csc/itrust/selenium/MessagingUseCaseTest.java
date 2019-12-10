package edu.ncsu.csc.itrust.selenium;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.meterware.httpunit.HttpUnitOptions;

import edu.ncsu.csc.itrust.enums.TransactionType;

import javax.swing.*;

public class MessagingUseCaseTest extends iTrustSeleniumTest {

	/*
	 * The URL for iTrust, change as needed
	 */
	/**ADDRESS*/
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	private WebDriver driver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		HttpUnitOptions.setScriptingEnabled(false);
		// turn off htmlunit warnings
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	}

	//The following two for doctors
	public void testHCPMessageFilter_1() throws Exception {

		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Andy Programmer");
		driver.findElement(By.name("subject")).sendKeys("Prescription");
		//We intendedly made it capital
		driver.findElement(By.name("hasWords")).sendKeys("PRESCRIPTION");

		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/30/2010");
		driver.findElement(By.name("endDate")).sendKeys("02/01/2010");
		String stamp = "2010-01-31 12:12";
		//check the result
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains(stamp));

	}

	public void testHCPMessageFilter_2() throws Exception {

		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Andy Programmer");
		driver.findElement(By.name("subject")).sendKeys("Scratchy Throat");
		driver.findElement(By.name("notWords")).sendKeys("abcde");

		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("02/01/2010");
		driver.findElement(By.name("endDate")).sendKeys("02/03/2010");
		String stamp = "2010-02-02 13:03";
		//check the result
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains(stamp));
		 
	}

	//The following two are for patients
	public void testPatientMessageFilter_1() throws Exception {

		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Kelly Doctor");
		driver.findElement(By.name("subject")).sendKeys("Office Visit Updated");
		//we intendedly made it capital
		driver.findElement(By.name("hasWords")).sendKeys("VISIT");

		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/20/2010");
		driver.findElement(By.name("endDate")).sendKeys("01/22/2010");
		String stamp = "2010-01-21 11:32";
		//check the result
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains(stamp));

	}

	public void testPatientMessageFilter_2() throws Exception {

		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 1L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Kelly Doctor");
		driver.findElement(By.name("subject")).sendKeys("Lab Procedure");
		//we intendedly made it capital
		driver.findElement(By.name("notWords")).sendKeys("ABCDE");

		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/27/2010");
		driver.findElement(By.name("endDate")).sendKeys("01/30/2010");
		String stamp = "2010-01-28 17:58";
		//check the result
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains(stamp));
	}


	//The following two test for save and cancel
	// ***********************
	public void testHCPMessageFilter_SaveCancel() throws Exception {

		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Andy Programmer");
		driver.findElement(By.name("subject")).sendKeys("Prescription");
		driver.findElement(By.name("hasWords")).sendKeys("PRESCRIPTION");
		driver.findElement(By.name("notWords")).sendKeys("ABCDE");
		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/30/2010");
		driver.findElement(By.name("endDate")).sendKeys("02/01/2010");
		//click save then feed boxes with different inputs
		driver.findElement(By.name("save")).click();


		//send corresponding keys
		driver.findElement(By.name("sender")).clear();
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("hasWords")).clear();
		driver.findElement(By.name("notWords")).clear();
		driver.findElement(By.name("sender")).sendKeys("abcde");
		driver.findElement(By.name("subject")).sendKeys("efghi");
		driver.findElement(By.name("hasWords")).sendKeys("xyzw");
		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/01/2019");
		driver.findElement(By.name("endDate")).clear();
		driver.findElement(By.name("endDate")).sendKeys("02/01/2019");



		//click cancel then see whether we have the previously saved content back.
		driver.findElement(By.name("cancel")).click();
		//check the result
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("Prescription"));
		assertTrue(driver.getPageSource().contains("PRESCRIPTION"));
		assertTrue(driver.getPageSource().contains("01/30/2010"));
		assertTrue(driver.getPageSource().contains("02/01/2010"));
		assertTrue(driver.getPageSource().contains("ABCDE"));

	}

	// ***********************
	public void testPatientMessageFilter_SaveCancel() throws Exception {

		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 1L, 0L, "");

		//send corresponding keys
		driver.findElement(By.name("sender")).sendKeys("Kelly Doctor");
		driver.findElement(By.name("subject")).sendKeys("Office Visit Updated");
		driver.findElement(By.name("hasWords")).sendKeys("VISIT");
		driver.findElement(By.name("notWords")).sendKeys("ABCDE");
		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("startDate")).sendKeys("01/27/2010");
		driver.findElement(By.name("endDate")).sendKeys("01/30/2010");
		//click save then feed boxes with different inputs
		driver.findElement(By.name("save")).click();
		
		
		//send corresponding keys
		driver.findElement(By.name("sender")).clear();
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("hasWords")).clear();
		driver.findElement(By.name("notWords")).clear();
		//feed it with date
		driver.findElement(By.name("startDate")).clear();
		driver.findElement(By.name("endDate")).clear();


		//click cancel then see whether we have the previously saved content back.
		driver.findElement(By.name("cancel")).click();
		//check the result
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Office Visit Updated"));
		assertTrue(driver.getPageSource().contains("VISIT"));
		assertTrue(driver.getPageSource().contains("01/27/2010"));
		assertTrue(driver.getPageSource().contains("01/30/2010"));
		assertTrue(driver.getPageSource().contains("ABCDE"));

	}


	//
	public void testHCPSendMessage() throws Exception {
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Compose a Message")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("2");
		driver.findElement(By.xpath("//input[@value='2']")).submit();
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("subject")).sendKeys("Visit Request");
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("We really need to have a visit.");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 2L, "");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		assertTrue(driver.getPageSource().contains("My Sent Messages"));
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("Visit Request"));
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		
		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Visit Request"));
		assertTrue(driver.getPageSource().contains(stamp));
	}


	public void testPatientSendReply() throws Exception {
		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Read")).click();
		assertLogged(TransactionType.MESSAGE_VIEW, 2L, 9000000000L, "");
		driver.findElement(By.linkText("Reply")).click();
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("Which office visit did you update?");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertLogged(TransactionType.MESSAGE_SEND, 2L, 9000000000L, "");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 2L, 0L, "");
		
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains(stamp));
	}
	
	public void testPatientSendMessageMultiRecipients() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Compose a Message")).click();
		final Select selectBox = new Select(driver.findElement(By.name("dlhcp")));
		selectBox.selectByValue("9000000003");
		//selectComboValue("dlhcp", "9000000003", driver);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.name("cc")).clear();
		driver.findElement(By.name("cc")).sendKeys("9000000000");
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("subject")).sendKeys("This is a message to multiple recipients");
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("We really need to have a visit!");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertTrue(driver.getPageSource().contains("Gandalf Stormcrow"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("This is a message to multiple recipients"));
	}
	
	public void testPatientSendReplyMultipleRecipients() throws Exception {
		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Read")).click();
		assertLogged(TransactionType.MESSAGE_VIEW, 2L, 9000000000L, "");
		driver.findElement(By.linkText("Reply")).click();
		driver.findElement(By.name("cc")).click();
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("Which office visit did you update?");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Gandalf Stormcrow"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 2L, 0L, "");
		
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains(stamp));
		
		driver = login("9000000003", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000003L, 0L, "");
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains(stamp));
	}
	
	public void testHCPSendReplySingleCCRecipient() throws Exception {
		gen.clearMessages();
		gen.messages6();
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Read")).click();
		assertLogged(TransactionType.MESSAGE_VIEW, 9000000000L, 22L, "Viewed Message: 3");
		driver.findElement(By.linkText("Reply")).click();
		driver.findElement(By.name("cc")).click();
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("I will not be able to make my next schedulded appointment.  Is there anyone who can book another time?");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 22L, "9000000007");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("RE: Appointment rescheduling"));
		assertTrue(driver.getPageSource().contains("Fozzie Bear"));
		assertTrue(driver.getPageSource().contains("Beaker Beaker"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		
		driver = login("22", "pw");
		assertLogged(TransactionType.HOME_VIEW, 22L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 22L, 0L, "");
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("RE: Appointment rescheduling"));
		assertTrue(driver.getPageSource().contains(stamp));
		
		driver = login("9000000007", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000007L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000007L, 0L, "");
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("RE: Appointment rescheduling"));
		assertTrue(driver.getPageSource().contains(stamp));
	}

	public void testPatientSortOutboxMessageByNameAscending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("name");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> names = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			names.add(TotalColumnCount.get(0).getText().split(" ")[1]);
		}

		List<String> sortedList = new ArrayList<>(names);
		Collections.sort(sortedList);
		assertEquals(sortedList, names);
	}

	public void testPatientSortOutboxMessageByNameDescending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("name");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("desc");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> names = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			names.add(TotalColumnCount.get(0).getText().split(" ")[1]);
		}

		List<String> sortedList = new ArrayList<>(names);
		Collections.sort(sortedList, Collections.reverseOrder());
		assertEquals(sortedList, names);
	}

	public void testPatientSortOutboxMessageByTimeDescending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("desc");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList, Collections.reverseOrder());
		assertEquals(sortedList, times);
	}

	public void testPatientSortOutboxMessageByTimeAscending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList);
		assertEquals(sortedList, times);
	}

	public void testHCPSortOutboxMessageByTimeAscending() throws Exception {
		//gen.messages();
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList);
		assertEquals(sortedList, times);
	}



	public void testPatientSortInboxMessageByNameAscending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("name");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> names = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			names.add(TotalColumnCount.get(0).getText().split(" ")[1]);
		}

		List<String> sortedList = new ArrayList<>(names);
		Collections.sort(sortedList);
		assertEquals(sortedList, names);
	}

	public void testPatientSortInboxMessageByNameDescending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("name");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("desc");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> names = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			names.add(TotalColumnCount.get(0).getText().split(" ")[1]);
		}

		List<String> sortedList = new ArrayList<>(names);
		Collections.sort(sortedList, Collections.reverseOrder());
		assertEquals(sortedList, names);
	}

	public void testPatientSortInboxMessageByTimeDescending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("desc");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList, Collections.reverseOrder());
		assertEquals(sortedList, times);
	}

	public void testPatientSortInboxMessageByTimeAscending() throws Exception {
		gen.messagingCcs();
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList);
		assertEquals(sortedList, times);
	}

	public void testHCPSortInboxMessageByTimeAscending() throws Exception {
		//gen.messages();
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		final Select selectBox = new Select(driver.findElement(By.name("sortby")));
		selectBox.selectByValue("time");
		final Select selectBox0 = new Select(driver.findElement(By.name("sorthow")));
		selectBox0.selectByValue("asce");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		WebElement webTable = driver.findElement(By.id("mailbox"));
		List<WebElement> TotalRowCount = webTable.findElements(By.xpath("//*[@id='mailbox']/tbody/tr"));
		List<String> times = new ArrayList<>();
		for(WebElement rowElement:TotalRowCount) {
			List<WebElement> TotalColumnCount = rowElement.findElements(By.xpath("td"));
			times.add(TotalColumnCount.get(2).getText());
		}
		List<String> sortedList = new ArrayList<>(times);
		Collections.sort(sortedList);
		assertEquals(sortedList, times);
	}


}