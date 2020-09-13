package com.config.path;

public interface ConfigParams {
    enum FILE_FORMAT {XLS,XLSX};
    String masterFilePath = "data/input/master.xlsx";
    String masterSheetName= "data";
    String additionalParamHeaderName = "Additional Header";
    FILE_FORMAT selectedFormat = FILE_FORMAT.XLSX;

}
