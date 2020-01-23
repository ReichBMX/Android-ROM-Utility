package com.android.utility.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	public static List<File> listFiles(String startDir) {
        File dir = new File(startDir);
        File[] files = dir.listFiles();
        List<File> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                // Check if the file is a directory
                if (file.isDirectory()) {
                    // We will not print the directory name, just use it as a new
                    // starting point to list files from
                    //listDirectory(file.getAbsolutePath());
                	System.out.println("Directory: " + file.getName());
                } else {
                    // We can use .length() to get the file size
                    System.out.println(file.getName() + " (size in bytes: " + file.length()+")");
                }
                list.add(file);
            }
        }
        return list;
    }
	
	public static boolean createDirectory(String location) {
		File dir = new File(location);
		if (dir.exists()) {
			return true;
		}
		if (dir.isFile()) {
			return false;
		}
		if (dir.isDirectory()) {
			return true;
		}
		if (dir.mkdirs()) {
			return true;
		}
		return false;
	}

}
