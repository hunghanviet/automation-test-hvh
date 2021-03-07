package heplers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadExcelFileHepler {
    private static final String Sring = null;
    XSSFWorkbook workbook;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    String excelPath;

    XSSFSheet sheet;

    private static ReadExcelFileHepler instance = null;

    public static ReadExcelFileHepler getInstance(String excelPath){
        if(instance == null){
            instance = new ReadExcelFileHepler(excelPath);
        }
        return instance;
    }

    private ReadExcelFileHepler(String excelPath) {
        this.excelPath = excelPath;
        try {
            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the data from a cell
    public String getCellValue(String sheetName, String colName, int rowNum) {

        int index = workbook.getSheetIndex(sheetName);
        sheet = workbook.getSheetAt(index);

        XSSFRow row = sheet.getRow(0);

        int col_Num = 0;

        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                col_Num = i;
                break;
            }
        }

        row = sheet.getRow(rowNum);

        XSSFCell cell = row.getCell(col_Num);

        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {

            // System.out.println("cell is not present");
            return "";
        }

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);

    }

    public List<Map<String, String>> getDataSource(String sheetName) {
        int rowNum;
        int colNum;
        String key;
        String value;

        int index = workbook.getSheetIndex(sheetName);
        sheet = workbook.getSheetAt(index);

        rowNum = sheet.getPhysicalNumberOfRows() - 1;
        colNum = sheet.getRow(0).getLastCellNum();

        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        List<String> listOfValue = new ArrayList<String>();
        List<Integer> listOfStatus = new ArrayList<Integer>();

        for (int row = 0; row < rowNum; row++) {
            listOfValue.clear();
            int col = -1;

            Map<String, String> map = new HashMap<String, String>();
            while (col != (colNum - 1)) {
                col++;
                DataFormatter formatter = new DataFormatter();

                XSSFCell keyCell = sheet.getRow(0).getCell(col);
                XSSFCell valueCell = sheet.getRow(row + 1).getCell(col);

                key = formatter.formatCellValue(keyCell).replaceAll("\n", "");
                value = formatter.formatCellValue(valueCell).replaceAll("\n", "");
                map.put(key, value);
                listOfValue.add(value);
                //System.out.println("list of value is: " + listOfValue);

            }

            if (listOfValue.get(0).length() == 0) break;

            for (String l : listOfValue) {
                if (l != null) {
                    listOfStatus.add(1);
                } else
                    listOfStatus.add(0);
            }

            if (listOfStatus.contains(1)) {
                results.add(map);
                System.out.println(map);
            }
        }

        return results;
    }

    public int getHeadCellNum(String sheetName) {
        int cellHeaderNum = workbook.getSheet(sheetName).getRow(0).getLastCellNum();
        return cellHeaderNum;
    }

}
