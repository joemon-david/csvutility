package com.tool.common.report.html;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.FertileNode;
import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.Br;
import com.hp.gagawa.java.elements.Div;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class HTMLCommons {

    public static void addBr(Div node, int count)
    {
        for(int i=0; i<count;i++)
            node.appendChild(new Br());
    }

    public static void saveHtmlFile(Document doc,String filePath)
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
}
