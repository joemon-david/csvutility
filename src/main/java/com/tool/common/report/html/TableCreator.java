package com.tool.common.report.html;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.elements.*;


import java.util.ArrayList;
import java.util.Map;

public class TableCreator {

    public static Div createTableFromList(ArrayList<ArrayList<String>> tableData)
    {
        Div tableDiv = new Div();
        tableDiv.setAlign("center");
        Table table = new Table();
        table.setBorder("2px");
        for(ArrayList<String> row:tableData)
            addRow(table,row);

        tableDiv.appendChild(table);
        return tableDiv;
    }


    private static void addRow(Table table,ArrayList<String> row)
    {
        Tr tr = new Tr();
        for(String col:row){
            Td td = new Td();
            tr.appendChild(td);
            td.appendChild(new Text(col));
        }
        table.appendChild(tr);

    }

}
