package com.tool.common;

import java.util.ArrayList;
import java.util.List;

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



}
