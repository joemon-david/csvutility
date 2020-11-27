package com.tool.common;

public class NumericUtils {

    public static Double percentageOfDifference(Double value1,Double value2)
    {

        return (((value2-value1)/value1)*100);

    }

    public static void main(String[] args) {
        System.out.println(Math.abs(NumericUtils.percentageOfDifference(0.0,0.0)));

    }
}
