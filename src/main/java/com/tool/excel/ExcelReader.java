package com.tool.excel;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.config.path.FilePath;
import cucumber.api.java.hu.Ha;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelReader implements FilePath {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");

    public static Recordset filterWorkSheet(String sheet, String Colname, String criteria) throws Throwable
    {

        Fillo fillo=new Fillo();
        Connection connection;
        Recordset recordset;
        String whereConditionString;
        String strQuery;
        connection = fillo.getConnection(FilePath.masterFilePath);
        whereConditionString=" where " + Colname + " ='"+criteria+"'";
        strQuery="Select * from " + sheet + " " + whereConditionString;
        recordset=connection.executeQuery(strQuery);
        return recordset;
    }


    public static LinkedHashMap<Integer,LinkedHashMap<String,Object>> readDataFromExcelFile(String filePath, String sheetName)
    {
        LinkedHashMap<Integer,LinkedHashMap<String,Object>> excelDataMap = null;
        try {

            FileInputStream excelFile = new FileInputStream(new File(filePath));
            Workbook workbook = new HSSFWorkbook(excelFile);
            excelDataMap = new LinkedHashMap<>();
            LinkedHashMap<Integer,String> headerMap = new LinkedHashMap<>();
            int recordNumber=0;

            Sheet datatypeSheet = workbook.getSheet(sheetName);

            for (Row currentRow : datatypeSheet) {

                Iterator<Cell> cellIterator = currentRow.iterator();
                int columnNumber = 0;
                LinkedHashMap<String, Object> rowDataMap = new LinkedHashMap<>();

                while (cellIterator.hasNext()) {


                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellType() == CellType.STRING) {
                        String sValue = currentCell.getStringCellValue();
                        if (recordNumber == 0)
                            headerMap.put(columnNumber, sValue);
                        else
                            rowDataMap.put(headerMap.get(columnNumber), sValue);
                    } else if (currentCell.getCellType() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            String dtValue = sdf.format(currentCell.getDateCellValue());
                            rowDataMap.put(headerMap.get(columnNumber), dtValue);
                        } else {
                            rowDataMap.put(headerMap.get(columnNumber), currentCell.getNumericCellValue());
//
                        }
                    }

                    columnNumber++;
                }
                if (recordNumber > 0)
                    excelDataMap.put(recordNumber, rowDataMap);
                recordNumber++;

            }

            System.out.println("Scanning Completed ->Total Records "+recordNumber);
            System.out.print("Header values -> ");
            headerMap.forEach((key,value)-> System.out.print(value+","));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelDataMap;

    }
    public static LinkedHashMap<Integer,LinkedHashMap<String,Object>> readDataFromExcelFile()
    {
        return readDataFromExcelFile(masterFilePath,masterSheetName);
    }



    public static void main(String[] args) {
        ExcelReader.readDataFromExcelFile(masterFilePath,masterSheetName);
    }
}
