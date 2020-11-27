package com.tool.common;

import com.config.path.ConfigParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommonUtils  implements CommonNames, ConfigParams {


    public static String extractFileName(String filePath)
    {
        return filePath.substring(filePath.lastIndexOf("//")).replace("//","");
    }

    public static  String extractTransLogicType(String entry)
    {

        String transType = (null!=entry  && !entry.isEmpty())?entry.substring(0,entry.indexOf('(')):entry;
        return transType;
    }
    public static  Object extractTransLogicValue(String entry)
    {
        Object transValue = (null!=entry  && !entry.isEmpty())? entry.substring(entry.indexOf('(')+1,entry.indexOf(')')):entry;
        return transValue;
    }

    public static void main(String[] args) {
        System.out.println(CommonUtils.extractTransLogicValue("Tolerance(10)"));
    }





}
