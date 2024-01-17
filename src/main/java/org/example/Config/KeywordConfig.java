package org.example.Config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.example.base.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Properties;

public class KeywordConfig {

    public WebDriver driver;
    public Properties prop;
    public static Workbook book;
    public static Sheet sheet;
    public Base base;


    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public String keywordPath = "src/main/resources/excel/automationstudy.xlsx";

    public void startExcution(String sheetName) throws Exception {

        String locatorName = null;
        String locatorValue = null;
        InputStream file = null;
        try {
            file = new FileInputStream(keywordPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            book = WorkbookFactory.create(file);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sheet = book.getSheet(sheetName);
        int k = 0;

        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            try {
                String locatorColValue = sheet.getRow(i + 1).getCell(k + 3).toString().trim();//id = username
                if (!locatorColValue.equalsIgnoreCase("NA")) {
                    locatorName = locatorColValue.split("=")[0].trim();//id
                    locatorValue = locatorColValue.substring(locatorColValue.indexOf("=") + 1).trim();//username
                }
                String action = sheet.getRow(i + 1).getCell(k + 4).toString().trim();
                String value = sheet.getRow(i + 1).getCell(k + 5).toString().trim();
                CellType cellType = sheet.getRow(i + 1).getCell(k + 5).getCellType();

                actionStep(locatorName, locatorValue, action, value,cellType);

            } catch (Exception e) {
                log.info("", e);
            }
        }
    }

    public void actionStep(String locatorName, String locatorValue, String action, String value, CellType cellType) throws FileNotFoundException {
        base = new Base();
        prop = base.initProperties();
        actionbrower(action,value);
        actionUrl(action,value);
        getLocation(locatorName,locatorValue);
        actionSendKey(action,locatorName,locatorValue,value,cellType);
        actionClick(action,locatorName,locatorValue);
        verifyPageTitle(action,value);
    }
    public void actionbrower(String action, String value) {

        if (action.equalsIgnoreCase("openBrowser")) {
            if (value.isEmpty() || value.equals("NA")) {
                driver = base.initDriver(prop.getProperty("browser"));

            } else {
                driver = base.initDriver(value);
            }
        }
    }

    public void actionUrl(String action, String value) {
        if (action.equalsIgnoreCase("navigateTo")) {
            if (value.isEmpty() || value.equals("NA")) {
                driver.get(prop.getProperty("url"));
            } else {
                driver.get(value);
            }
        }
    }

    public By getLocation(String locatorName, String locatorValue){
        //find by xpath
        if(locatorName.equalsIgnoreCase("xpath")){
            return By.xpath(locatorValue);
            //find by ID
        } else if(locatorName.equalsIgnoreCase("id")){
            return By.id(locatorValue);
            //find by class name
        }else if(locatorName.equalsIgnoreCase("class")){
            return By.className(locatorValue);
            //find by name
        } else if(locatorName.equalsIgnoreCase("name")){
            return By.name(locatorValue);
            //find by cssSelector
        } else if(locatorName.equalsIgnoreCase("cssSelector")){
            return By.cssSelector(locatorValue);
        }
        return null;
    }
    public void actionSendKey(String action, String locatorName, String locatorValue, String value, CellType cellType) {
        if (action.equalsIgnoreCase("sendKeys")){
            WebElement element = driver.findElement(this.getLocation(locatorName,locatorValue));
            element.clear();
            if (cellType.equals(CellType.NUMERIC)) {
                element.sendKeys(String.valueOf((int) Double.parseDouble(value)));
            } else {
                element.sendKeys(value);
            }
        }
    }

    public void actionClick(String action, String locatorName, String locatorValue){
        if (action.equalsIgnoreCase("click")){
            driver.findElement(this.getLocation(locatorName, locatorValue)).click();
        }
    }

    public void actionCaptcha(String action){
        if(action.equalsIgnoreCase("capcha"))
        {
            WebElement captchaElement = driver.findElement(By.xpath("//*[@id=\"rc-anchor-alert\"]"));
            String captchaUrl = captchaElement.getAttribute("src");
            try {
                URL url = new URL(captchaUrl);
                BufferedImage img = ImageIO.read(url);
                File captchaFile = new File("captcha.png");
                ImageIO.write(img, "png", captchaFile);

                // Sử dụng thư viện OCR để nhận dạng captcha
                String captchaText = solveCaptcha(captchaFile);

                WebElement captchaInput = driver.findElement(By.xpath("//*[@id=\"recaptcha-token\"]"));
                captchaInput.sendKeys(captchaText);

                WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"recaptcha-anchor\"]/div[1]"));
                submitButton.click();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private static String solveCaptcha(File captchaFile) {
        // Thực hiện xử lý nhận dạng captcha ở đây
        return "captcha_text";
    }
    public void verifyPageTitle(String action,String value){
        if(action.equalsIgnoreCase("verifyPage")){
            String expectedTitle = value;
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, expectedTitle);
        }

    }
}
