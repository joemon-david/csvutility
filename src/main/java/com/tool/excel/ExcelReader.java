package com.tool.excel;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.config.path.ConfigParams;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ExcelReader implements ConfigParams {
    private static final SimpleDateFormat sdf = new SimpleDateFormat(ConfigParams.dateFormat);

    public static Recordset filterWorkSheet(String sheet, String Colname, String criteria) throws Throwable
    {

        Fillo fillo=new Fillo();
        Connection connection;
        Recordset recordset;
        String whereConditionString;
        String strQuery;
        connection = fillo.getConnection(ConfigParams.masterFilePath);
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

            Workbook workbook = (ConfigParams.selectedFormat == FILE_FORMAT.XLSX)?new XSSFWorkbook(excelFile) :new HSSFWorkbook(excelFile);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
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
                    CellType cellType = currentCell.getCellType();

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
                            long lng = (long) currentCell.getNumericCellValue();
                            rowDataMap.put(headerMap.get(columnNumber), lng);
//
                        }
                    }else if (currentCell.getCellType() == CellType.BOOLEAN)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),currentCell.getBooleanCellValue());
                    }
                    else if(currentCell.getCellType() == CellType.FORMULA)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),getFormulaValue(evaluator,currentCell));
                    }else if (currentCell.getCellType() == CellType.BLANK)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),"");
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
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelDataMap;

    }
    public static LinkedHashMap<Integer,LinkedHashMap<String,Object>> readDataFromExcelFile()
    {
        return readDataFromExcelFile(masterFilePath,masterSheetName);
    }


    private static String getFormulaValue(FormulaEvaluator evaluator, Cell cell)
    {
       CellType cellType = evaluator.evaluateFormulaCell(cell);

        switch (cellType) {
        case NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell))
                return sdf.format(cell.getDateCellValue());
             else
                return ""+cell.getNumericCellValue();
        case STRING:
            return cell.getRichStringCellValue().getString();

        case BOOLEAN:
            return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
        case BLANK:
            return "";
        case ERROR:
            return FormulaError.forInt(cell.getErrorCellValue()).getString();
        default:
            throw new RuntimeException("Unexpected celltype (" + cellType + ")");
    }
}

    public  LinkedHashMap<Integer,LinkedHashMap<String,Object>> readAllDataFromExcelFile(String filePath, String sheetName,int maxCellToCheck)
    {
        LinkedHashMap<Integer,LinkedHashMap<String,Object>> excelDataMap = null;
        try {

            FileInputStream excelFile = new FileInputStream(new File(filePath));

            Workbook workbook = (ConfigParams.selectedFormat == FILE_FORMAT.XLSX)?new XSSFWorkbook(excelFile) :new HSSFWorkbook(excelFile);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            excelDataMap = new LinkedHashMap<>();
            LinkedHashMap<Integer,String> headerMap = new LinkedHashMap<>();
            int recordNumber=0;

            Sheet datatypeSheet = workbook.getSheet(sheetName);

            for (Row currentRow : datatypeSheet) {

                LinkedHashMap<String, Object> rowDataMap = new LinkedHashMap<>();

                for (int columnNumber =0;columnNumber<maxCellToCheck;columnNumber++) {

                    Cell currentCell = currentRow.getCell(columnNumber);
                    if(null==currentCell)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),"");
                        continue;
                    }



                    CellType cellType = currentCell.getCellType();

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
                            long lng = (long) currentCell.getNumericCellValue();
                            rowDataMap.put(headerMap.get(columnNumber), lng);
//
                        }
                    }else if (currentCell.getCellType() == CellType.BOOLEAN)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),currentCell.getBooleanCellValue());
                    }
                    else if(currentCell.getCellType() == CellType.FORMULA)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),getFormulaValue(evaluator,currentCell));
                    }else if (currentCell.getCellType() == CellType.BLANK)
                    {
                        rowDataMap.put(headerMap.get(columnNumber),"");
                    }


                }
                if (recordNumber > 0)
                    excelDataMap.put(recordNumber, rowDataMap);
                recordNumber++;

            }

            System.out.println("Scanning Completed ->Total Records "+recordNumber);
            System.out.print("Header values -> ");
            headerMap.forEach((key,value)-> System.out.print(value+","));
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelDataMap;

    }



    public static void main(String[] args) {
        ExcelReader.readDataFromExcelFile(masterFilePath,masterSheetName);
    }
}
