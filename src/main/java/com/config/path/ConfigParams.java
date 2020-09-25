package com.config.path;

public interface ConfigParams {
    enum FILE_FORMAT {XLS,XLSX};
    String masterFilePath = "data/input/master.xlsx";
    String masterSheetName= "Sheet1";
    String additionalParamHeaderName = "Additional Header";
    FILE_FORMAT selectedFormat = FILE_FORMAT.XLSX;
    String dateFormat = "M/dd/yyyy";
    String fileCompareHtmlFilePath = "data/output/filecomapre.html";

}
