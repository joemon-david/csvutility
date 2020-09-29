package com.tool.common.report.html;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.FertileNode;
import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.*;


import java.util.ArrayList;
import java.util.Map;

public class TableCreator {

    public static Div createTableFromList(ArrayList<ArrayList<String>> tableData)
    {
        Div tableDiv = new Div();
        tableDiv.setAlign("center");
        Table table = new Table();
        table.setId("customers");
        table.setBorder("2px");
        int rowCounter=0;
        for(ArrayList<String> row:tableData)
        {
            addRow(table,row,rowCounter);
            rowCounter++;
        }

        tableDiv.appendChild(table);
        return tableDiv;
    }


    private static void addRow(Table table,ArrayList<String> row,int counter)
    {
        Tr tr = new Tr();
        for(String col:row){

            if(counter==0)
            {
                Th th = new Th();
                tr.appendChild(th);
                th.appendChild(new Text(col));

            } else
            {
                Td td = new Td();
                tr.appendChild(td);
                td.appendChild(new Text(col));
            }

        }
        table.appendChild(tr);

    }

}
