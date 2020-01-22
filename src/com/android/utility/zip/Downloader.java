package com.android.utility.zip;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class Downloader {

   public Downloader(File zipFile, File destinationFolder) {
      JFrame frm = new JFrame();
      final JProgressBar current = new JProgressBar(0, 100);
      current.setSize(50, 100);
      current.setValue(0);
      current.setStringPainted(true);
      frm.add(current);
      frm.setVisible(true);
      frm.setLayout(new FlowLayout());
      frm.setSize(50, 100);
      frm.setDefaultCloseOperation(EXIT_ON_CLOSE);
      final Worker worker = new Worker(zipFile, destinationFolder);
      worker.addPropertyChangeListener(new PropertyChangeListener() {

         @Override
         public void propertyChange(PropertyChangeEvent pcEvt) {
            if ("progress".equals(pcEvt.getPropertyName())) {
               current.setValue((Integer) pcEvt.getNewValue());
            } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
               try {
                  worker.get();
               } catch (InterruptedException | ExecutionException e) {
                  // handle any errors here
                  e.printStackTrace(); 
               }
            }

         }
      });
      worker.execute();
   }
}

class Worker extends SwingWorker<Void, Void> {
   private File zipfile;
   private File folder;

   public Worker(File site, File file) {
      this.zipfile = site;
      this.folder = file;
   }

   @Override
   protected Void doInBackground() throws Exception {
	   FileInputStream is = new FileInputStream(zipfile.getCanonicalFile());
       FileChannel channel = is.getChannel();
       ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
       ZipEntry ze = null;
       try {
           while ((ze = zis.getNextEntry()) != null) {
                File f = new File(folder.getCanonicalPath(), ze.getName());
               if (ze.isDirectory()) {
                   f.mkdirs();
                   continue;
               }
               f.getParentFile().mkdirs();
               OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
               try {
                   try {
                       final byte[] buf = new byte[1024];
                       int bytesRead;
                       long nread = 0L;
                       long length = zipfile.length();

                       while (-1 != (bytesRead = zis.read(buf))){
                           fos.write(buf, 0, bytesRead);
                           nread += bytesRead;
                           System.out.println(nread + "/" + length);
                          // updateProgress(channel.position(), length);
                           setProgress((int) channel.position());
                       }
                   } finally {
                       fos.close();
                   }
               } catch (final IOException ioe) {
                   f.delete();
                   throw ioe;
               }
           }
       } finally {
           zis.close();
       }
       return null;
   }
}