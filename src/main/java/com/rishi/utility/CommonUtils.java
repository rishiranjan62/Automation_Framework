package com.rishi.utility;

import java.io.File;

public class CommonUtils {

    public static File getLatestFilefromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    public static File renameFile(String fileName, String expectedFileName){
        File existing = new File(fileName);
        File expected = new File(System.getProperty("user.dir")+"/Transcribe/"+expectedFileName);
        boolean renamed = existing.renameTo(expected);
       if(renamed)
           return expected;
       else
           return existing;
    }

    public static String getFileName(File file){
        String[] filename = file.toString().split("/");
        return filename[filename.length - 1];
    }

}