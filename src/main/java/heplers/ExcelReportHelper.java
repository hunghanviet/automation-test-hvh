package heplers;

import models.TestExecuteStatus;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelReportHelper {
    String inputPath;
    String outputPath;

    public ExcelReportHelper(String inputPath, String outputPath) throws IOException {
        this.inputPath = inputPath;
        this.outputPath = outputPath;

    }

    public void setResult(String sheetName1, String result, int rowIndex, int cellHeaderNum, String sheetName2, TestExecuteStatus testExecuteStatus) throws IOException {
        int totalTC = testExecuteStatus.getTC();
        int TCPass = testExecuteStatus.getTCPass();
        int TCFail = testExecuteStatus.getTCFail();
        int TCPending = testExecuteStatus.getTCPending();
        int colNum = 0;
        String headerCellName = "Result";

        try {
            FileInputStream inputFile = null;
            XSSFWorkbook workbook = null;
            // XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            File fos = new File(outputPath);
            if (!fos.exists()) {

                File fis = new File(inputPath);
                try {
                    FileUtils.copyFile(fis, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            DataFormatter formatter = new DataFormatter();

//				open file
            inputFile = new FileInputStream(fos);
            workbook = new XSSFWorkbook(inputFile);
            inputFile.close();

            XSSFSheet sheet = workbook.getSheet(sheetName1);
            XSSFRow headRow = sheet.getRow(0);

            headRow.createCell(cellHeaderNum).setCellValue(headerCellName);

            for (int i = 0; i < headRow.getLastCellNum(); i++) {
                XSSFCell cols = headRow.getCell(i);
                String colsval = formatter.formatCellValue(cols);

                if (colsval.trim().equalsIgnoreCase(headerCellName.trim())) {
                    colNum = i;
                    System.out.println("Col num is: " + colNum);
                }
            }

            XSSFRow currentRow = sheet.getRow(rowIndex);

            XSSFCell cell = currentRow.getCell(colNum);
            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
                cell = currentRow.createCell(colNum);

            if(result.equalsIgnoreCase("Fail")) {
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                style.setFont(font);

                cell.setCellValue(result);
                cell.setCellStyle(style);
            } else
                cell.setCellValue(result);


            System.out.println("set result = " + result + "to row " + currentRow.getRowNum());

            XSSFSheet sheet2 = workbook.getSheet(sheetName2);

            float ratePass = ((float) TCPass / totalTC) * 100;
            float rateFail = ((float) TCFail / totalTC) * 100;

            float ratePassValue = (float) (Math.round((ratePass) * 100.0) / 100.0);
            System.out.println("ratePAss = " + ratePassValue);

            float rateFailValue = (float) (Math.round((rateFail) * 100.0) / 100.0);
            System.out.println("rateFail = " + rateFailValue);

            float ratePendingValue = 100 - ratePassValue - rateFailValue;
            System.out.println("ratePendingValue = " + ratePendingValue);

            System.out.println("123");
            XSSFRow rowToSetResult = sheet2.createRow(1);
            System.out.println("create row to set total result: " + rowToSetResult.getRowNum());
            System.out.println("456");

            // Check if cell null or empty then create cell and set sesult
            XSSFCell totalTCCell = rowToSetResult.getCell(0);

            if (totalTCCell == null || totalTCCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                totalTCCell = rowToSetResult.createCell(0);
            }
            totalTCCell.setCellValue(String.valueOf(totalTC));
            System.out.println("Set total result=" + String.valueOf(totalTC));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell TCPassCell = rowToSetResult.getCell(1);
            if (TCPassCell == null || TCPassCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                TCPassCell = rowToSetResult.createCell(1);
            }
            TCPassCell.setCellValue(String.valueOf(TCPass));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell TCFailCell = rowToSetResult.getCell(2);
            if (TCFailCell == null || TCFailCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                TCFailCell = rowToSetResult.createCell(2);
            }
            TCFailCell.setCellValue(String.valueOf(TCFail));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell TCPendingCell = rowToSetResult.getCell(3);
            if (TCPendingCell == null || TCPendingCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                TCPendingCell = rowToSetResult.createCell(3);
            }
            TCPendingCell.setCellValue(String.valueOf(TCPending));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell ratePassValueCell = rowToSetResult.getCell(4);
            if (ratePassValueCell == null || ratePassValueCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                ratePassValueCell = rowToSetResult.createCell(4);
            }
            ratePassValueCell.setCellValue(String.valueOf(ratePassValue));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell rateFailValueCell = rowToSetResult.getCell(5);
            if (rateFailValueCell == null || ratePassValueCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                rateFailValueCell = rowToSetResult.createCell(5);
            }
            rateFailValueCell.setCellValue(String.valueOf(rateFailValue));

            // Check if cell null or empty then create cell and set sesult
            XSSFCell ratePendingValueCell = rowToSetResult.getCell(6);
            if (ratePendingValueCell == null || ratePassValueCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                ratePendingValueCell = rowToSetResult.createCell(6);
            }
            ratePendingValueCell.setCellValue(String.valueOf(ratePendingValue));

            System.out.println("789");

            FileOutputStream fileOutput = new FileOutputStream(new File(outputPath));
            workbook.write(fileOutput);

            System.out.println("hihi");

            fileOutput.flush();
            fileOutput.close();
            workbook.close();
            System.out.println("set cell data" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can not set result");
        }
    }

    public static void copyFile(File inputFile, File outputFile) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(inputFile);
            output = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public File checkCopyFile(String inputPath, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            File inputFile = new File(inputPath);
            copyFile(inputFile, outputFile);
        }

        return outputFile;
    }
}
