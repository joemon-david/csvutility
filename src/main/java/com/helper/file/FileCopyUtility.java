package com.helper.file;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class FileCopyUtility {



        public static void main(String[] args) {
           
            String fileLocationSourceNetwork = "//Qaha-uber-db//mhcureshare//wctp";
            String fileLocationSourceDrive = "C://mavenRepo";

            String fileLocationDestination = "C://temp//";
            int numberOfFilesToCopy = 2;

            /**Copy the files from the network**/
            copyFiles(	fileLocationSourceNetwork,
                    fileLocationDestination,
                    numberOfFilesToCopy);

            System.out.println("nn");

//            /**Copy the files from the drive**/
//            copyFiles(	fileLocationSourceDrive,
//                    fileLocationDestination,
//                    numberOfFilesToCopy);

        }





        public static void copyFiles(String fileLocationSource,
                                     String fileLocationDestination, int numberOfFilesToCopy) {
            File inputLocation = new File(fileLocationSource);
            if (inputLocation.isDirectory()) {
                System.out.println("Listing the files...");
                File[] attachmentFiles = inputLocation.listFiles();
                System.out.println("Total files in the folder: "
                        + attachmentFiles.length);
                for (File aFile : attachmentFiles) {
                    if (!aFile.isDirectory()) {
                        String fileName = aFile.getName();
                        String sourceFileName = aFile.getAbsolutePath();
                        String destinationFileName = fileLocationDestination
                                + fileName;
                        copyFile(sourceFileName, destinationFileName);
                    }
                    if (numberOfFilesToCopy >= 0) {
                        if (--numberOfFilesToCopy == 0) {
                            break;
                        }
                    }
                }
            }else {
                System.out.println("inputLocation is Directory ? "+inputLocation.isDirectory());
            }
            System.out.println("Completed...");
        }

        /**
         *
         * @param sourceFileName
         * @param destionFileName
         *
         *            Copies a single file from source location to a destination
         *            location.
         */
        private static void copyFile(String sourceFileName, String destionFileName) {
            try {
                System.out.println("Reading..." + sourceFileName);
                File sourceFile = new File(sourceFileName);
                File destinationFile = new File(destionFileName);
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destinationFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
                System.out.println("Copied: " + sourceFileName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

