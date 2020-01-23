package com.android.utility.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.android.utility.log.Log;

public class ZipUtils {
	
	    public static void unzip(String zipFile, String destinationDirectory) throws IOException {
	    	File destDir = new File(destinationDirectory);
	        byte[] buffer = new byte[1024];
	        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	        ZipEntry zipEntry = zis.getNextEntry();
	        while (zipEntry != null) {
	            File newFile = newFile(destDir, zipEntry);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.close();
	            zipEntry = zis.getNextEntry();
	        }
	        zis.closeEntry();
	        zis.close();
	    }
	     
	    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	        File destFile = new File(destinationDir, zipEntry.getName());
	         
	        String destDirPath = destinationDir.getCanonicalPath();
	        String destFilePath = destFile.getCanonicalPath();
	         
	        if (!destFilePath.startsWith(destDirPath + File.separator)) {
	            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	        }
	         
	        return destFile;
	    }
	    
	    private void extractFolder(String zipFile,String extractFolder) 
	    {
	        try
	        {
	            int BUFFER = 2048;
	            File file = new File(zipFile);

	            ZipFile zip = new ZipFile(file);
	            String newPath = extractFolder;

	            new File(newPath).mkdir();
	            Enumeration zipFileEntries = zip.entries();

	            // Process each entry
	            while (zipFileEntries.hasMoreElements())
	            {
	                // grab a zip file entry
	                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	                String currentEntry = entry.getName();

	                File destFile = new File(newPath, currentEntry);
	                //destFile = new File(newPath, destFile.getName());
	                File destinationParent = destFile.getParentFile();

	                // create the parent directory structure if needed
	                destinationParent.mkdirs();

	                if (!entry.isDirectory())
	                {
	                    BufferedInputStream is = new BufferedInputStream(zip
	                    .getInputStream(entry));
	                    int currentByte;
	                    // establish buffer for writing file
	                    byte data[] = new byte[BUFFER];

	                    // write the current file to disk
	                    FileOutputStream fos = new FileOutputStream(destFile);
	                    BufferedOutputStream dest = new BufferedOutputStream(fos,
	                    BUFFER);

	                    // read and write until last byte is encountered
	                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                        dest.write(data, 0, currentByte);
	                    }
	                    dest.flush();
	                    is.close();
	                }


	            }
	        }
	        catch (Exception e) 
	        {
	            Log.normal("ERROR: "+e.getMessage());
	        }

	    }
	    
	    public static void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException {
	        File[] files = folder.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
	                addFolderToZip(file, zip, baseName);
	            } else {
	                String name = file.getAbsolutePath().substring(baseName.length());
	                ZipEntry zipEntry = new ZipEntry(name);
	                zip.putNextEntry(zipEntry);
	                IOUtils.copy(new FileInputStream(file), zip);
	                zip.closeEntry();
	            }
	        }
	    }

}
