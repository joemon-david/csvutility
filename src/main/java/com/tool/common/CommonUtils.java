package com.tool.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommonUtils  implements CommonNames{


    public static String extractFileName(String filePath)
    {
        return filePath.substring(filePath.lastIndexOf("//")).replace("//","");
    }

    public static HashMap<String,String> createBriefingMap(int totalFile1Records,int totalFile2Records,int compared,int mismatch)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put(REP_TOT_REC_FIL1,""+totalFile1Records);
        map.put(REP_TOT_REC_FIL2,""+totalFile2Records);
        map.put(REP_TOT_REC_COMPRD,""+compared);
        map.put(REP_TOT_MIS_MATCH,""+mismatch);
        return map;
    }

    public static ArrayList<String> createMismatchTableHeader()
    {
        return new ArrayList<>(Arrays.asList(MISMATCH_TABLE_SI,MISMATCH_TABLE_ROW_NO,MISMATCH_TABLE_COLMN,
                MISMATCH_TABLE_FILE1_VAL,MISMATCH_TABLE_FILE2_VAL));
    }

}
