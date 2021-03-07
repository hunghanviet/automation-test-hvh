package test_executors;

import heplers.ReadExcelFileHepler;
import heplers.ReportHelper;
import heplers.WebActionHepler;
import models.TestExecuteStatus;
import models.ActionInputData;
import utils.DateTimeUtil;
import utils.PatternUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestExecutor {
    public List<Map<String, String>> testSteps;

    public Map<String, String> testCase;

    public ReportHelper reportHelper;

    public TestExecuteStatus testExecuteStatus;

    public WebActionHepler webActionHepler;

    public String htmlReportPath;

    public String startTime;

    public ReadExcelFileHepler readExcelFileHepler;

    public String testType;

    public String screenShotPath;


    public void init(List<Map<String, String>> testSteps,
                     Map<String, String> testCase,
                     ReportHelper reportHelper,
                     TestExecuteStatus testExecuteStatus,
                     WebActionHepler webActionHepler,
                     String htmlReportPath,
                     String startTime,
                     ReadExcelFileHepler readExcelFileHepler,
                     String screenShotPath){
        this.testSteps = testSteps;
        this.testCase = testCase;
        this.reportHelper = reportHelper;
        this.testExecuteStatus = testExecuteStatus;
        this.webActionHepler = webActionHepler;
        this.htmlReportPath = htmlReportPath;
        this.screenShotPath = screenShotPath;
        this.startTime = startTime;
        this.readExcelFileHepler = readExcelFileHepler;
    }

    public void executeTest(){

        String TCStatus = "Pass";
        // This will Read RunMode from TestSuite for testID
        String Skip = testCase.get("Skip");
        String testCaseName = testCase.get("TestCaseID");
        String testCaseType = testCase.get("TestCaseType");
        String TSStatus = "Pass";

        for (Map<String, String> teststep : testSteps) {

            String TSID = teststep.get("TSID");
            String Description = teststep.get("Description");
            System.out.println("Execute " + testExecuteStatus.getTC() + " And step:" + Description);
            String Action = teststep.get("Action");
            String LocatorType = teststep.get("LocatorType");
            String LocatorValue = teststep.get("LocatorValue");
            String TestDataField = teststep.get("TestDataField");
            String ProceedOnFail = teststep.get("ProceedOnFail");
            String RunFor = teststep.get("RunForTestCaseTypes");
            if (RunFor.contains(this.testType)) {
                if (TestDataField != null && TestDataField != "") {

                    if (PatternUtil.checkIsPattern(TestDataField)) {

                        String keyDataField = PatternUtil.findTestDataField(TestDataField);

                        TestDataField = testCase.get(keyDataField);
                        System.out.println("Key Test Data=" + TestDataField);

                    }
                }


                try {
                    TSStatus = webActionHepler.performAction(Action, new ActionInputData(LocatorType, LocatorValue, TestDataField));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("TSStatus is: " + TSStatus + "on stepID:" + TSID);

                if (TSStatus.contains("Failed")) {
                    // take the creenshot
                    String fileName = testCase.get("TestCaseID") + "_" + TSID;
                    String screenShot = webActionHepler.takeScreenShot(fileName, screenShotPath);

                    TCStatus = "Fail";
                    System.out.println("TCStatus is: " + TCStatus);

                    // report error

                    reportHelper.getHtmlReport().addKeyword(Description, Action, TSStatus, screenShot);

                    if (ProceedOnFail.equals("N")) {
                        break;
                    }

                } else {
                    System.out.println("test case pass is: " + testExecuteStatus.getTCPass());
                    reportHelper.getHtmlReport().addKeyword(Description, Action, TSStatus, null);
                    System.out.println("TCStatus is: " + TCStatus);
                }
            }

        }

        if (TCStatus.contains("Pass")) {
            testExecuteStatus.setTCPass(testExecuteStatus.getTCPass() + 1);
        } else {
            testExecuteStatus.setTCFail(testExecuteStatus.getTCFail() + 1);
        }

        try {
            System.out.println("Total TC = "+testExecuteStatus.getTC());
            System.out.println("Head Cell Num = "+readExcelFileHepler.getHeadCellNum("TestCases"));
            reportHelper.getExcelReport().setResult("TestCases", TCStatus, testExecuteStatus.getTC(), readExcelFileHepler.getHeadCellNum("TestCases"), "TongHop",
                    testExecuteStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

        reportHelper.getHtmlReport().addTestCase(htmlReportPath, testCaseType, testCaseName, startTime,
                DateTimeUtil.now("dd.MMMM.yyyy hh.mm.ss aaa"), TCStatus);
    }

}
