package com.tool.common.report.html;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.Br;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PieChartCreater {



    public static void main(String[] args) {

        StringBuilder data = new StringBuilder();
        data.append("['Item','Count'],");
        data.append("['Matched',600],").append("['MisMatched',25],").append("['Orphan records',23]");
        String divId = "piechart";
        Document doc= new Document(DocumentType.HTMLStrict);

        addPieChart(doc,data.toString(),"File comparison Report",divId);

        HTMLCommons.saveHtmlFile(doc,"data/output/sample.html");
    }

    public static Div addPieChart(Document doc,String data,String title,String divId)
    {
        Script chartLoader = new Script("text/javascript");
        chartLoader.setSrc("https://www.gstatic.com/charts/loader.js");
        Script chartScript = new Script("text/javascript");
        chartScript.appendText("google.charts.load('current', {'packages':['corechart']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +data+
//                "          ['Task', 'Hours per Day'],\n" +
//                "          ['Work',     11],\n" +
//                "          ['Eat',      2],\n" +
//                "          ['Commute',  2],\n" +
//                "          ['Watch TV', 2],\n" +
//                "          ['Sleep',    7]\n" +
                "        ]);\n" +
                "\n" +
                "        var options = {\n" +
                "          title: '"+title+"',\n " +
                "chartArea:{left:20,top:0,width:'50%',height:'75%'}"+      //'My Daily Activities'\n" +

                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.PieChart(document.getElementById('"+divId+"'));\n" +
                "\n" +
                "        chart.draw(data, options);\n" +
                "      }");

        doc.head.appendChild(chartLoader).appendChild(chartScript);
        Div chartDiv = new Div();
        chartDiv.setId(divId);
        chartDiv.setCSSClass("pieChart");
//        chartDiv.setStyle("width: 80%; height: 70%;");
        chartDiv.setAlign("center");
        return chartDiv;

    }





}
