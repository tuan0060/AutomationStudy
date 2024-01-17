package org.example.testcase;


import org.example.Config.KeywordConfig;
import org.example.listener.ReportListener;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners(ReportListener.class)
public class Tests {

    public KeywordConfig keywordConfig;
//    public static ExtentReports report;

    @BeforeTest
    public void setUp() throws IOException {
//        report = new ExtentReports();

        keywordConfig = new KeywordConfig();

    }


    @Test
    public void loginTest() throws Exception {

        keywordConfig.startExcution("sheet1");

    }

}

