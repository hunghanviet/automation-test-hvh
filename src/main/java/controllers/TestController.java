package controllers;

import heplers.*;
import listeners.TestFrameListener;
import models.TestExecuteStatus;
import test_executors.TestExecutor;
import test_executors.TestExecutorFactory;
import utils.DateTimeUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestController {
    private TestFrameListener listener;

    public void setListener(TestFrameListener listener) {
        this.listener = listener;
    }

    public void preprocess(String inputFilePath, String outputDirPath) throws IOException {
        String formattedInputFilePath;
        String formattedOutputDirPath;
        String uscacse = null;


        String dateName = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());


        formattedInputFilePath = inputFilePath.replace("\\", "\\\\");
        System.out.println("file test path is: " + formattedInputFilePath);

        String nameOfFileExcel = this.getUseCaseName(formattedInputFilePath);



        String patternUseCaseNameTest = "^([a-zA-Z0-9_]+)(\\.)([a-zA-Z]+)$";
        Pattern pattern = Pattern.compile(patternUseCaseNameTest);

        boolean b = pattern.matcher(nameOfFileExcel).matches();
        if (b) {
            Matcher matcher = pattern.matcher(nameOfFileExcel);
            if (matcher.matches()) {
                uscacse = matcher.group(1);
                System.out.println(uscacse);
            }

        }


        formattedOutputDirPath = outputDirPath.replace("\\", "\\\\");
        System.out.println("newLocation is:" + formattedOutputDirPath);



        //create date folder
        String acResultPath = formattedOutputDirPath + "\\" + dateName;
        File dir0 = new File(acResultPath);
        dir0.mkdir();
        System.out.println("create date forder: " + dateName);

        //create suitename folder
        String SuiteNamePath = acResultPath + "\\" + uscacse;
        File dir1 = new File(SuiteNamePath);
        dir1.mkdirs();
        //System.out.println("create suitname folder: "+ usecaseName);



        //create excelReport folder
        String excelReportPath = SuiteNamePath + "\\" + "excelReport";
        File dir2 = new File(excelReportPath);
        boolean createdExcelforder = dir2.mkdirs();
        System.out.println("Check excelReport exist: " + createdExcelforder);


        //create htmlReport forder
        String htmlReportPath = SuiteNamePath + "\\" + "htmlReport";
        File dir3 = new File(htmlReportPath);
        boolean createHtmlReport = dir3.mkdirs();

        System.out.println("check htmlReport folder exist:" + createHtmlReport);



        //create screenshot folder
        String screenshotPath = SuiteNamePath + "\\" + "screenshot";
        File dir4 = new File(screenshotPath);
        boolean createScreenShot  = dir4.mkdirs();
        System.out.println("create screenshot folder: " + createScreenShot);

        executeAutomationTest(inputFilePath, excelReportPath, screenshotPath, htmlReportPath);
    }

    public void executeAutomationTest(String inputFilePath, String excelReportPath, String screenShotPath, String htmReportPath) throws IOException {
        ReadExcelFileHepler readExcelFileHepler = ReadExcelFileHepler.getInstance(inputFilePath);
        List<Map<String, String>> dataSource = readExcelFileHepler.getDataSource("TestCases");
        List<Map<String, String>> testSteps = readExcelFileHepler.getDataSource("TestSteps");

        ReportHelper reportHelper = initReportHeplper(inputFilePath, excelReportPath, htmReportPath, screenShotPath);

        String startTime = DateTimeUtil.now("dd.MMMM.yyyy hh.mm.ss aaa");

        String usecaseName = readExcelFileHepler.getCellValue("UseCaseInfo", "UseCaseName", 1);
        String ENV = readExcelFileHepler.getCellValue("UseCaseInfo", "ENV", 1);
        String ResleaseVersion = readExcelFileHepler.getCellValue("UseCaseInfo", "ReleaseVersion", 1);

        reportHelper.getHtmlReport().startTesting(htmReportPath, "index", startTime, ENV, ResleaseVersion);

        WebActionHepler webActionHepler = new WebActionHepler();

        // HtmlReport.startSuite("Suite1");
        reportHelper.getHtmlReport().startSuite(usecaseName);

        TestExecuteStatus testExecuteStatus = new TestExecuteStatus();

        for (Map<String, String> data : dataSource) {
            String TCStatus = "Pass";
            testExecuteStatus.setTC(testExecuteStatus.getTC() + 1);
            // This will Read RunMode from TestSuite for testID
            String Skip = data.get("Skip");
            String testCaseName = data.get("TestCaseID");
            String testCaseType = data.get("TestCaseType");

            if (Skip.equals("N")) {

                TestExecutor testExecutor = TestExecutorFactory.getTestExcutor(testCaseType);
                testExecutor.init(
                        testSteps,
                        data,
                        reportHelper,
                        testExecuteStatus,
                        webActionHepler,
                        htmReportPath,
                        startTime,
                        readExcelFileHepler,
                        screenShotPath
                );
                testExecutor.executeTest();

            }

            else if(Skip.equals("Y")){

                // skip the testcase
                testExecuteStatus.setTCPending(testExecuteStatus.getTCPending() + 1);
                System.out.println("Skip testcase " + testExecuteStatus.getTC());

                reportHelper.getHtmlReport().addTestCase(htmReportPath, testCaseType, testCaseName, startTime,
                        DateTimeUtil.now("dd.MMMM.yyyy hh.mm.ss aaa"), "Skipped");

                reportHelper.getExcelReport().setResult("TestCases", "Skipped", testExecuteStatus.getTC(), readExcelFileHepler.getHeadCellNum("TestCases"), "TongHop", testExecuteStatus);

                if (webActionHepler.getDriver() != null) {
                    System.out.println("quit drive o day khi TC Skipped");
                    webActionHepler.getDriver().quit();
                }
            }

        }

        reportHelper.getHtmlReport().endSuite();
        reportHelper.getHtmlReport().updateEndTime(DateTimeUtil.now("dd.MMMM.yyyy hh.mm.ss aaa"));

    }

    public ReportHelper initReportHeplper(String inputFilePath, String excelReportPath, String htmReportPath, String screenShotPath) throws IOException {
//       Init Exel report hepler
        String excelOutputPath = excelReportPath + "//" + "Result.xlsx";
        ExcelReportHelper excelReport  = new ExcelReportHelper(inputFilePath, excelOutputPath);
        HtmlReportHelper  htmlReport   = new HtmlReportHelper();
        return new ReportHelper(excelReport, htmlReport);
    }

    public  String getUseCaseName(String path) {
        String[] arrName = path.split("\\\\");
        String usecaseName = arrName[arrName.length - 1];
        return usecaseName;
    }

}
