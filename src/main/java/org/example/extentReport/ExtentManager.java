package org.example.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;

public class ExtentManager {
    private static final ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports getExtentReports() throws IOException {
            ExtentSparkReporter reporter = new ExtentSparkReporter("./ExtentReports/ExtentReport.html");
            reporter.config().setReportName("Demo Extent Report");
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("Framework Name", "Selenium Java Framework | KeyWord Driven FrameWork");
            extentReports.setSystemInfo("Author", "TUANNQ");
        return extentReports;
    }
}
