package com.tool.common;

import java.util.*;

public class Reporter {

    public static void printArrayList(List<String> list)
    {
        System.out.println("=======================================================================");
        list.forEach((value)-> System.out.print(value+"\t"));
        System.out.println();
    }
    public static void printListOfList(ArrayList<ArrayList<String>> listOfList)
    {
        listOfList.forEach((list)->{
            printArrayList(list);
        } );

    }
    public static void printHashMap(LinkedHashMap<String, Object> map)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(Object obj: map.keySet())
        {
            sb.append(obj).append(":").append(map.get(obj)).append(",");
        }
        sb.append(" }");
        System.out.println(sb.toString());
    }




}
