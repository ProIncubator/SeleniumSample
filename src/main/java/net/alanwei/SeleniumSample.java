package net.alanwei;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class SeleniumSample {

    private final static FirefoxDriver DRIVER;

    private final static WebDriverWait WAIT;
    private final static String HOME_DIR = System.getProperty("user.dir");
    private final static String SEPERATOR = System.getProperty("file.separator");

    static {
        String driverName = "mac.geckodriver";
        // 如果是windows系统 注释上面一行代码, 下面一行代码取消注释,
        //String driverName = "win64.geckodriver.exe";
        String webDriverPath = HOME_DIR + SEPERATOR + "src" + SEPERATOR + "main" + SEPERATOR + "resources" + SEPERATOR + "webdrivers" + SEPERATOR + driverName;
        System.setProperty("webdriver.gecko.driver", webDriverPath);
        DRIVER = new FirefoxDriver();
        WAIT = new WebDriverWait(DRIVER, 10);
    }

    public static void main(String[] args) throws Throwable {
        DRIVER.get("https://shop.10086.cn/i/?f=rechargecredit&mobileNo=13687298754&amount=50");

        wait(2);
        WebElement btnSubmit = find(By.id("stc-recharge-submit"));
        System.out.println(btnSubmit.getTagName() + btnSubmit.getText());
        btnSubmit.click();
        wait(3);
        if (btnSubmit.isDisplayed()) {
            btnSubmit.click();
        }

        ArrayList<String> tabs = new ArrayList<>(DRIVER.getWindowHandles());
        DRIVER.switchTo().window(tabs.get(1));

        WebElement btnPay = find(By.cssSelector("input[name=ConfirmPay]"));
        System.out.println("image: " + btnPay.getAttribute("src"));

        WebElement alipay = find(By.cssSelector("#payment input[value=ALIPAY]"));
        alipay.click();
        wait(1);
        btnPay.click();

        tabs = new ArrayList<>(DRIVER.getWindowHandles());
        DRIVER.switchTo().window(tabs.get(2));
        wait(5);
        System.out.println(DRIVER.getTitle());
        WebElement picQrCode = find(By.cssSelector("#J_qrPayArea canvas"));
        Object base64Value = DRIVER.executeScript("return arguments[0].toDataURL('image/png').substring(22);", picQrCode);
        byte[] data = Base64.getDecoder().decode(base64Value.toString());
        FileUtils.writeByteArrayToFile(new File(HOME_DIR + SEPERATOR + "qrcode.png"), data);

        //DRIVER.quit();
    }

    static void wait(int seconds) {
        DRIVER.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    static WebElement find(By by) {
        return WAIT.until(d -> d.findElement(by));
    }

}
