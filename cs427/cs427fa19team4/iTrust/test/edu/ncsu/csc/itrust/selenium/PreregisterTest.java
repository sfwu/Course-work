package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PreregisterTest extends iTrustSeleniumTest {
    protected WebDriver driver;

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
        if(driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/li/a")) == null){
            throw new NoSuchElementException("Pre-register link");
        }
    }

    public void testMandatoryData(){
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

        WebElement confirmation = driver.findElement(By.className("iTrustMessage"));
        if(confirmation == null){
            throw new NoSuchElementException("Can't find 'Successful Registration'");
        }
        assertEquals("Successful Pre-registration",confirmation.getText());
    }

    public void testAdditionalInformation() throws Exception{
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
        WebElement phone = driver.findElement(By.name("phone"));
        WebElement icName = driver.findElement(By.name("icName"));
        WebElement icstreetAddress = driver.findElement(By.name("icAddress1"));
        WebElement icCity = driver.findElement((By.name("icCity")));
        Select icState = new Select(driver.findElement(By.name("icState")));
        WebElement icZip = driver.findElement(By.name("icZip"));
        WebElement icPhone = driver.findElement(By.name("icPhone"));
        WebElement weight = driver.findElement(By.name("weight"));
        WebElement height = driver.findElement(By.name("height"));
        WebElement isSmoker = driver.findElement(By.name("isSmoker"));
        WebElement submit = driver.findElement(By.name("action"));


        firstName.sendKeys("Krastan");
        lastName.sendKeys("Dimitrov");
        email.sendKeys("bleh@gmail.com");
        password.sendKeys("12345678");
        confirmpassword.sendKeys("12345678");
        streetAddress1.sendKeys("123 Street Ave.");
        streetAddress2.sendKeys("Unit 123");
        city.sendKeys("Champaign");
        state.selectByVisibleText("Alabama");
        zip.sendKeys("34567");
        phone.sendKeys("123-456-7890");
        icName.sendKeys("Bob Johnson");
        icstreetAddress.sendKeys("123 Rando St.");
        icCity.sendKeys("Urbana");
        icState.selectByVisibleText("Illinois");
        icZip.sendKeys("45678");
        icPhone.sendKeys("123-456-6789");
        height.sendKeys("72");
        weight.sendKeys("300");
        isSmoker.click();

        submit.click();

        WebElement confirmation = driver.findElement(By.className("iTrustMessage"));
        if(confirmation == null){
            throw new NoSuchElementException("Can't find 'Successful Registration'");
        }
        assertEquals("Successful Pre-registration",confirmation.getText());
    }
}
