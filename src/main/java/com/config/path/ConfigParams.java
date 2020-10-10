package com.config.path;

public interface ConfigParams {
    enum FILE_FORMAT {XLS,XLSX};
    String masterFilePath = "data/input/CUAT-MasterPlan.xlsx";
    String masterSheetName= "MASTER_CUAT";
    String additionalParamHeaderName = "Additional Header";
    FILE_FORMAT selectedFormat = FILE_FORMAT.XLSX;
    String dateFormat = "M/dd/yyyy";
    String fileCompareHtmlFilePath = "data/output/filecomapre.html";
    String additionalCSVParamHeaderName = "Expected Status";
    boolean isToSuffixCounter = false;

}
