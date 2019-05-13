import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class getTestData  {
    final  static String filePath = "TestData/RestAssured.xlsx";

    public static HashMap<String, String> readUrls(String sheetname) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        FileInputStream inStream = new FileInputStream(file);
        Workbook testWorkbook = new XSSFWorkbook(inStream);
        HashMap<String,String> hmap=new HashMap<String, String>();
        Sheet sh = testWorkbook.getSheet(sheetname);
        int totalNoOfRows = (sh.getLastRowNum()-sh.getFirstRowNum())+1;
        // To get the number of columns present in sheet
        int totalNoOfCols = sh.getRow(0).getLastCellNum();
        for (int rows = 0; rows < totalNoOfRows; rows++) {
            Row row = sh.getRow(rows);
            String str="";
            for (int col = 0; col < totalNoOfCols; col++) {
                 str=str+row.getCell(col).getStringCellValue()+"::";
            }
            String[] map = str.split("::");
            hmap.put(map[0],map[1]);
        }
        return hmap;
    }
    public  static Object[][] readData(String sheetname) throws IOException,FileNotFoundException {
        File file = new File(filePath);
        FileInputStream inStream = new FileInputStream(file);
        Workbook testWorkbook = new XSSFWorkbook(inStream);
        Sheet sh = testWorkbook.getSheet(sheetname);

        int totalNoOfRows = sh.getPhysicalNumberOfRows();
        // To get the number of columns present in sheet
        int totalNoOfCols = sh.getRow(0).getLastCellNum();
        Object data[][] = new Object[totalNoOfRows][totalNoOfCols];

        for (int row = 0; row < totalNoOfRows; row++) {
            for (int col = 0; col < totalNoOfCols; col++) {
                 data[row][col]=sh.getRow(row).getCell(col).toString();
            }
        }
        return data;
    }

}
