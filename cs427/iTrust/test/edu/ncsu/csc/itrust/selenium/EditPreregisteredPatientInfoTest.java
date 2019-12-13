package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class EditPreregisteredPatientInfoTest extends iTrustSeleniumTest{

    protected WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();


    @Override
    protected void setUp() throws Exception{
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    public void testMandatoryData() throws Exception{
        driver = new Driver();
        driver.get("http://localhost:8080/iTrust");
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/li/a")).click();
        assertEquals("iTrust - Pre-register", driver.getTitle());
        WebElement firstName = driver.findElement(By.name("firstName"));
        WebElement lastName = driver.findElement(By.name("lastName"));
        WebElement email = driver.findElement(By.name("email"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement confirmpassword = driver.findElement(By.name("confirmpassword"));
        WebElement streetAddress1 = driver.findElement(By.name("streetAddress1"));
        WebElement streetAddress2 = driver.findElement(By.name("streetAddress2"));
        WebElement city = driver.findElement(By.name("city"));
        Select state = new Select(driver.findElement(By.name("state")));
        WebElement zip = driver.findElement(By.name("zip"));
        WebElement height = driver.findElement(By.name("height"));
        WebElement weight = driver.findElement(By.name("weight"));
        WebElement smoker = driver.findElement(By.name("isSmoker"));
        WebElement submit = driver.findElement(By.name("action"));


        firstName.sendKeys("Krastan");
        lastName.sendKeys("Dimitrov");
        email.sendKeys("bleh@gmail.com");
        streetAddress1.sendKeys("123 Street Ave.");
        streetAddress2.sendKeys("Unit 123");
        city.sendKeys("Champaign");
        state.selectByVisibleText("Alabama");
        zip.sendKeys("34567");
        password.sendKeys("12345678");
        confirmpassword.sendKeys("12345678");
        height.sendKeys("72");
        weight.sendKeys("300");
        smoker.click();

        submit.click();

        //log in processes
        driver.findElement(By.id("j_username")).clear();
        driver.findElement(By.id("j_username")).sendKeys("9000000000");
        driver.findElement(By.id("j_password")).clear();
        driver.findElement(By.id("j_password")).sendKeys("pw");
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        //make sure that it correctly log-in as HCP
        try {
            assertEquals("iTrust - HCP Home", driver.getTitle());
        } catch (Error e) {
            verificationErrors.append(e.toString());
            fail();
        }
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        driver.findElement(By.cssSelector("h2.panel-title")).click();

        assertEquals(true,driver.getPageSource().contains("Patient Info"));
        //.click();
        driver.findElement(By.linkText("View Pre-registered")).click();

        assertEquals(true, driver.getPageSource().contains("Krastan Dimitrov"));
        driver.findElement(By.linkText("Krastan Dimitrov")).click();
        assertEquals(true, driver.getPageSource().contains("bleh@gmail.com"));
        driver.findElement((By.xpath("/html/body/div[2]/div/div[2]/div/div[1]/div[1]/a/input"))).click();
        assertEquals(true, driver.getPageSource().contains("Edit Patient Record"));
        assertEquals(true, driver.getPageSource().contains("Champaign"));
    }

}
