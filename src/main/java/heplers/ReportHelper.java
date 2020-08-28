package heplers;

public class ReportHelper {
    private ExcelReportHelper excelReport;

    private HtmlReportHelper htmlReport;

    public ReportHelper(ExcelReportHelper excelReport, HtmlReportHelper htmlReport) {
        this.excelReport = excelReport;
        this.htmlReport = htmlReport;
    }

    public ExcelReportHelper getExcelReport() {
        return excelReport;
    }

    public void setExcelReport(ExcelReportHelper excelReport) {
        this.excelReport = excelReport;
    }

    public HtmlReportHelper getHtmlReport() {
        return htmlReport;
    }

    public void setHtmlReport(HtmlReportHelper htmlReport) {
        this.htmlReport = htmlReport;
    }
}
