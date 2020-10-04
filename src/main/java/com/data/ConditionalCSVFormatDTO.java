package com.data;

public class ConditionalCSVFormatDTO {

    String conditionHeaderName;
    String conditionHeaderValue;
    String formatHeaderName;
    String formatHeaderValue;

    public ConditionalCSVFormatDTO()
    {

    }
    public ConditionalCSVFormatDTO(String conditionHeaderName,String conditionHeaderValue,String formatHeaderName,String formatHeaderValue)
    {
        this.conditionHeaderName = conditionHeaderName;
        this.conditionHeaderValue = conditionHeaderValue;
        this.formatHeaderName = formatHeaderName;
        this.formatHeaderValue = formatHeaderValue;

    }

    public String getConditionHeaderName() {
        return conditionHeaderName;
    }

    public void setConditionHeaderName(String conditionHeaderName) {
        this.conditionHeaderName = conditionHeaderName;
    }

    public String getConditionHeaderValue() {
        return conditionHeaderValue;
    }

    public void setConditionHeaderValue(String conditionHeaderValue) {
        this.conditionHeaderValue = conditionHeaderValue;
    }

    public String getFormatHeaderName() {
        return formatHeaderName;
    }

    public void setFormatHeaderName(String formatHeaderName) {
        this.formatHeaderName = formatHeaderName;
    }

    public String getFormatHeaderValue() {
        return formatHeaderValue;
    }

    public void setFormatHeaderValue(String formatHeaderValue) {
        this.formatHeaderValue = formatHeaderValue;
    }

    @Override
    public String toString() {
        return "ConditionalCSVFormateDTO{" +
                "conditionHeaderName='" + conditionHeaderName + '\'' +
                ", conditionHeaderValue='" + conditionHeaderValue + '\'' +
                ", formatHeaderName='" + formatHeaderName + '\'' +
                ", formatHeaderValue='" + formatHeaderValue + '\'' +
                '}';
    }
}
