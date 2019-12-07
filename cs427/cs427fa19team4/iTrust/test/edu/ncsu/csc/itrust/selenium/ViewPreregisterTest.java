package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewPreregisterTest extends iTrustSeleniumTest {

     protected WebDriver driver;
     private StringBuffer verificationErrors = new StringBuffer();


    @Override
        protected void setUp() throws Exception{
            super.setUp();
            gen.clearAllTables();
            gen.standardData();
        }

        public void testHomePage() throws Exception{
            driver = new Driver();
            driver.get("http://localhost:8080/iTrust");
            assertEquals("iTrust - Login", driver.getTitle());

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
            WebElement submit = driver.findElement(By.name("action"));

            firstName.sendKeys("Krastan");
            lastName.sendKeys("Dimitrov");
            email.sendKeys("bleh@gmail.com");
            password.sendKeys("12345678");
            confirmpassword.sendKeys("12345678");
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


            assertEquals(driver.getPageSource().contains("Krastan Dimitrov"), true);

        }
    }
