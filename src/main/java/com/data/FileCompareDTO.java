package com.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;

@Setter
@Getter
@ToString
@Builder
public class FileCompareDTO {

    String documentTitle;
    HashMap<String,String> briefingMap;
    ArrayList<ArrayList<String>> tableData;
    String outputFilePath;
    String pieChartTitle;
    String pieChartDivId;
    StringBuilder pieChartData;
}
