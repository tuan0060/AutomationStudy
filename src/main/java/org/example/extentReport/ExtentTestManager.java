package org.example.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.base.Base;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    static ExtentReports extent;

    public static ExtentTest test;

    static {
        try {
            extent = ExtentManager.getExtentReports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static  ExtentTest saveToReport() throws IOException {
      try {
          FileInputStream file = new FileInputStream(new File("src/main/resources/excel/automationstudy.xlsx"));
          Workbook workbook = new XSSFWorkbook(file);
          Sheet sheet = workbook.getSheetAt(0);
          for (int i = 1; i <= sheet.getLastRowNum(); i++) {
              Row row = sheet.getRow(i);
              String testCaseID = row.getCell(1).getStringCellValue();
              String testCaseName = row.getCell(2).getStringCellValue();

              test = extent.createTest(testCaseName, testCaseID);
          }

          extentTestMap.put((int) Thread.currentThread().getId(), test);

      }catch (Exception e){
          e.printStackTrace();
      }
        return test;
    }

    public WebDriver getDriver() {
        WebDriver driver = Base.getDriver();
        return driver;
    }

    public static void addScreenShot(String message) {
        String base64Image = "data:image/png;base64,"
                + ((TakesScreenshot) Base.getDriver()).getScreenshotAs(OutputType.BASE64);
        getTest().log(Status.INFO, message,        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
    }

    public static void addScreenShot(Status status, String message) {

        String base64Image = "data:image/png;base64,"
                + ((TakesScreenshot) Base.getDriver()).getScreenshotAs(OutputType.BASE64);
        getTest().log(status, message,               MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
    }

    public static void logMessage(String message) {
        getTest().log(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        getTest().log(status, message);
    }
}
