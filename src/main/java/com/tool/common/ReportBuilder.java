package com.tool.common;


import com.config.path.CONSTANTS;
import com.data.FileCompareDTO;
import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.*;
import com.tool.common.report.html.FileComparisonReporter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ReportBuilder implements CONSTANTS {
    private static Document doc = new Document(DocumentType.HTMLStrict);

    public static void createHTMLReport(String title,String outputFilePath)
    {

    addBr(2);
        doc.body.setStyle("background-color:#D5D8DC;");

        Div d1 = new Div().setId("myDiv");
        Div d2 = new Div().setCSSClass("mainClass");
        d1.appendChild(d2);
        d2.appendChild(new H2().appendText(title));
        d2.setAlign("center");
        doc.body.setStyle("background-color:#D5D8DC;");
        doc.body.appendChild(d1);
     addBr(4);

        Div tableDiv = new Div().setAlign("center");
        d2.appendChild(tableDiv);

        Table table = new Table();
        tableDiv.appendChild(table);
        table.setBorder("2px");
        Tr titleRow = new Tr();
        table.appendChild(titleRow);
        setTitleRow(titleRow);

        int count = 0;

//        doc.body.appendChild(table);
        saveHtmlFile(outputFilePath);
    }
    private static void setTitleRow(Tr titleRow)
    {
        for(int col = 0; col < 10; col++){
            Td td = new Td();
            titleRow.appendChild(td);
            td.appendChild(new Text(""));
        }

    }

    private static void saveHtmlFile(String filePath)
    {
        try {
            File output = new File(filePath);
            PrintWriter out = new PrintWriter(new FileOutputStream(output));
            out.println(doc.write());
            System.out.println(doc.write());
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static void addBr(int count)
    {
      for(int i=0; i<count;i++)
          doc.body.appendChild(new Br());
    }


    public static void main(String[] args) {

        HashMap<String,String> briefingMap = new HashMap<>();
        briefingMap.put("Total Records in File 1 ","45");
        briefingMap.put("Total Records in File 2 ","40");
        briefingMap.put("Total Records compared ","40");
        briefingMap.put("Total Mismatch found","5");

        StringBuilder data = new StringBuilder();
        data.append("['Item','Count'],");
        data.append("['Matched',40],").append("['MisMatched',5],").append("['Orphan records',5]");
        String divId = "piechart";
        ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();

        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"SI","Mismatch Row No","Mismatch Column Name","File 1 Value","File 2 Value"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","SecurityID","62944TAF2","44988MAA3"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","TradeDate","9/07/2020","23/07/2020"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","SecurityID","62944TAF2","44988MAA3"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","TradeDate","9/07/2020","23/07/2020"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","SecurityID","62944TAF2","44988MAA3"})));
        tableData.add(new ArrayList<String>(Arrays.asList(new String[]{"1","2","TradeDate","9/07/2020","23/07/2020"})));


        FileCompareDTO dto = FileCompareDTO.builder().build();
        dto.setBriefingMap(briefingMap);
        dto.setDocumentTitle("File Comparison Report (File1 & File2)");
        dto.setOutputFilePath("data/output/sample.html");
        dto.setPieChartData(data);
        dto.setPieChartDivId(divId);
        dto.setPieChartTitle("File Comparison Status");
        dto.setTableData(tableData);



        FileComparisonReporter.createFileComparisonReport(dto);

//        IssueDto dto;
//        dto = new IssueDto("We can expect a long summary ","HHUS-23456","Alternate Caller","Admin/Server","QA Blocked","3.0");
//
//        ArrayList<IssueDto> dtoList = new ArrayList<IssueDto>();
//        dtoList.add(dto);
//        createHTMLReport(dtoList,"","");

//        Document document = new Document(DocumentType.XHTMLStrict);
//
//        Comment c1 = new Comment("This is part 1.");
//        c1.appendChild(new Text("This is a comment!\n"));
//        c1.appendChild(new Text("This is part 2!"));
//        document.body.appendChild(c1);
//        document.body.setBgcolor("#CCC");
//        Div d1 = new Div().setId("myDiv");
//        Div d2 = new Div().setCSSClass("mainClass");
//        d1.appendChild(d2);
//        d2.appendChild(new Text("Inside of div2"));
//        d2.appendChild(new Br());
//        d2.appendChild(new A("http://www.jumppage.com","_blank").appendChild(new Text("jumppage")));
//        Img img = new Img("http://www.w3schools.com/tags/angry.gif","");
//        d2.appendChild(new Br());
//        d2.appendChild(img);
//        document.body.appendChild(d1);
//
//        Table table = new Table();
//        table.setBorder("2px");
//
//        int count = 0;
//        for(int row = 0; row < 10; row++){
//            Tr tr = new Tr();
//            table.appendChild(tr);
//            for(int col = 0; col < 10; col++){
//                Td td = new Td();
//                tr.appendChild(td);
//                td.appendChild(new Text(count++));
//            }
//        }
//        document.body.appendChild(table);
//
//        d1.setStyle("float:left");


    }
}
