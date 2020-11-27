package com.config.path;

public interface ConfigParams {
    enum FILE_FORMAT {XLS,XLSX};
    String masterFilePath = "data/input/Master_New.xlsx";
    String masterSheetName= "MASTER_CUAT";
    String additionalParamHeaderName = "Additional Header";
    FILE_FORMAT selectedFormat = FILE_FORMAT.XLSX;
    String dateFormat = "M/dd/yyyy";
    String fileCompareHtmlFilePath = "data/output/filecomapre.html";
    String additionalCSVParamHeaderName = "Expected Status";
    boolean isToSuffixCounter = false;
    String COMPARE_FILE_TYPE = "FIleType";
    String COMPARE_SRC_FILE = "SourceFile";
    String COMPARE_SRC_COLUMN = "SourceColumns";
    String COMPARE_TAR_FILE = "TargetFIle";
    String COMPARE_TAR_COLUMN = "TargetColumn";
    String COMPARE_PRIMARY_KEY = "Primarykey";
    String COMPARE_TRANSF_LOGIC = "Tranformation Logic";
    String COMPARE_SRC_DIR_PATH = "data/input/compare/source/";
    String COMPARE_TARGET_DIR_PATH = "data/input/compare/target/";
    String COMPARE_RESULT_MATCHES = "PASS";
    String COMPARE_RESULT_MIS_MATCHES= "FAILED";
    Integer COMPARE_MAX_COLUMN_IN_CONFIG_FILE = 7;
    String COMPARE_TRANS_LOGIC_TOLERANCE= "Tolerance";





}
