package com.tool.common.report.html;

import com.data.FileCompareDTO;
import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileComparisonReporter {

    static Document doc = new Document(DocumentType.HTMLStrict);

    public static void createFileComparisonReport(FileCompareDTO dto)
    {
//        setBodyAttributes(doc);
        Div mainDiv = new Div().setId("mainDiv");
        linkCSS();

        doc.body.appendChild(mainDiv);
        // Add Title
        mainDiv.appendChild(addReportTitleDiv(dto.getDocumentTitle()));
        // Add Report Briefing from map
        mainDiv.appendChild(addReportBriefing(dto.getBriefingMap()));
        //Add Pie Chart
        mainDiv.appendChild(PieChartCreater.addPieChart(doc,dto.getPieChartData().toString(),dto.getPieChartTitle(),dto.getPieChartDivId()));
        // Add Table
        mainDiv.appendChild(TableCreator.createTableFromList(dto.getTableData()));
        HTMLCommons.saveHtmlFile(doc,dto.getOutputFilePath());
    }

    private static Div addReportTitleDiv(String title)
    {
        Div d1 = new Div().setId("titleDiv");
        d1.appendChild(new H2().appendText(title));
        d1.setAlign("center");
        return d1;

    }

    private static void setBodyAttributes(Document doc)
    {
        doc.body.setStyle("background-color:#D5D8DC;");
    }

    private static Div addReportBriefing(HashMap<String,String> briefingMap)
    {
        Div briefingDiv = new Div();
        briefingDiv.setAlign("center");
        briefingDiv.setCSSClass("briefing");
        briefingMap.forEach((key,value)-> {
            StringBuilder sb = new StringBuilder();
            sb.append(key).append(" : ").append(value);
            P brief = new P();
            brief.setCSSClass("brief");
            brief.appendText(sb.toString());
            briefingDiv.appendChild(brief);

        });

        return briefingDiv;


    }

    private static void linkCSS()
    {
        Link link = new Link();
        link.setRel("stylesheet");
        link.setHref("report.css");
        doc.head.appendChild(link);
        Link googleFont = new Link();
        googleFont.setRel("stylesheet");
        googleFont.setHref("https://fonts.googleapis.com/css?family=Artifika");
        doc.head.appendChild(googleFont);
    }

}
