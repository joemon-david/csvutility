package com.helper.csv;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReportingUtility {

    public static void generateReport()
    {

        String path= "target";
//        Collection<File> jsonFiles= FileUtils.listFiles(new File(path),new String[]{"json"},true);
        List<String> jsonPaths= new ArrayList(1);
//        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));

        jsonPaths.add(Paths.get("target//cucumber.json").toString());
        Configuration config= new Configuration(new File("target"), "Test Automation");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths,config);
        reportBuilder.generateReports();
    }
}
