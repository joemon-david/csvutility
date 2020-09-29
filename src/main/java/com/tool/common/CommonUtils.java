package com.tool.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommonUtils  implements CommonNames{


    public static String extractFileName(String filePath)
    {
        return filePath.substring(filePath.lastIndexOf("//")).replace("//","");
    }





}
