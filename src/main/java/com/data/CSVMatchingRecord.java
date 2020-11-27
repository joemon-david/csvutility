package com.data;

import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class CSVMatchingRecord {

    CSVRecord srcRecord;
    CSVRecord targetRecord;
    boolean isMatchFound;
    HashMap<String,String> srcTransLogicMap;
    LinkedHashSet<String> primaryKeySet;
    HashMap<String,Object> rowToleranceMap;
    HashMap<String,String> srcTargetMap;

    public HashMap<String, String> getSrcTargetMap() {
        return srcTargetMap;
    }

    public void setSrcTargetMap(HashMap<String, String> srcTargetMap) {
        this.srcTargetMap = srcTargetMap;
    }


    public HashMap<String, Object> getRowToleranceMap() {
        return rowToleranceMap;
    }
    public void setRowToleranceMap(HashMap<String, Object> rowToleranceMap) {
        this.rowToleranceMap = rowToleranceMap;
    }


    public CSVRecord getSrcRecord() {
        return srcRecord;
    }

    public void setSrcRecord(CSVRecord srcRecord) {
        this.srcRecord = srcRecord;
    }

    public CSVRecord getTargetRecord() {
        return targetRecord;
    }

    public void setTargetRecord(CSVRecord targetRecord) {
        this.targetRecord = targetRecord;
    }

    public boolean isMatchFound() {
        return isMatchFound;
    }

    public void setMatchFound(boolean matchFound) {
        isMatchFound = matchFound;
    }

    public HashMap<String, String> getSrcTransLogicMap() {
        return srcTransLogicMap;
    }

    public void setSrcTransLogicMap(HashMap<String, String> srcTransLogicMap) {
        this.srcTransLogicMap = srcTransLogicMap;
    }

    public LinkedHashSet<String> getPrimaryKeySet() {
        return primaryKeySet;
    }

    public void setPrimaryKeySet(LinkedHashSet<String> primaryKeySet) {
        this.primaryKeySet = primaryKeySet;
    }
}
