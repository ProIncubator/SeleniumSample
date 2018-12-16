package net.alanwei;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String driverName = "win64.geckodriver.exe";
        String userDir = System.getProperty("user.dir");
        String  fileSeparator = System.getProperty("file.separator");
        String webDriverPath = userDir + fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "webdrivers" + fileSeparator + driverName;

        System.setProperty("webdriver.gecko.driver", webDriverPath);


        FirefoxDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://10086.cn/");
        WebElement inputNo = wait.until(d -> d.findElement(By.id("#cz_pho")));
        inputNo.sendKeys("13687298754");

    }
}
