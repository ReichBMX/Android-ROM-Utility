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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class Unzipper {

	private JFrame frame;
	private JProgressBar current;

	public Unzipper(File zipFile, File destinationFolder) {
		frame = new JFrame();
		current = new JProgressBar(0, 100);
		current.setSize(50, 100);
		current.setValue(0);
		current.setStringPainted(true);
		frame.add(current);
		frame.setSize(150, 50);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		final Worker worker = new Worker(frame, zipFile, destinationFolder);
		worker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent pcEvt) {
				if ("progress".equals(pcEvt.getPropertyName())) {
					current.setValue((Integer) pcEvt.getNewValue());
				} else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
					try {
						worker.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}

			}
		});
		worker.execute();
	}
}

class Worker extends SwingWorker<Void, Void> {

	private JFrame frame;
	private File zipfile;
	private File folder;

	public Worker(JFrame frame, File zipfile, File folder) {

		this.setFrame(frame);
		this.zipfile = zipfile;
		this.folder = folder;
	}

	@Override
	protected Void doInBackground() throws Exception {
		//extractFile(zipfile, folder);
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
						final byte[] buf = new byte[10240];
						int bytesRead;
						long nread = 0L;
						long length = zipfile.length();
						System.out.println("Extracting: " + f.getName());
						//while (-1 != (bytesRead = zis.read(buf))) {
						while ((bytesRead = zis.read(buf)) > 0) {
							fos.write(buf, 0, bytesRead);
							nread += bytesRead;
							//System.out.println(f.getName() + " : " + nread + "/" + f.length());
							setProgress(getPercentage((int) channel.position(), length));
						}
					} finally {
						System.out.println("Extracted: " + f.getName());
						fos.close();
					}
				} catch (final IOException ioe) {
					f.delete();
					throw ioe;
				}
			}
		} finally {
			System.out.println("CLOSE");
			setProgress(100);
			frame.setVisible(false);
			frame = null;
			zis.close();
		}
		return null;
	}
	
	private void extractFile(File zipFile, File directory) throws Exception
    {
        byte[] buffer = new byte[1024];
        ZipFile zf = new ZipFile(zipFile);

        Enumeration<ZipArchiveEntry> entries = zf.getEntries();
        
        int total  = 0;

        while(entries.hasMoreElements()) {
        	
            ZipArchiveEntry ze = entries.nextElement();
            String zefilename = ze.getName();
            
            System.out.println("Extracting file: " + zefilename);
            File extfile = new File(zefilename);
            File newFile = new File(directory.getAbsolutePath(), extfile.getName());
            
            	System.out.println("Creating Directory: " + newFile.getAbsolutePath());
            	newFile.mkdirs();

            InputStream zis = zf.getInputStream(ze);
            FileOutputStream fos = new FileOutputStream(newFile);
            
            try {
            	
                int numBytes;
                while ((numBytes = zis.read(buffer, 0, buffer.length)) != -1) {
                	total += numBytes;
                    fos.write(buffer, 0, numBytes);
                    //System.out.println(total + " : " + zipFile.length());
                    setProgress(getPercentage(total, zipFile.length()));
                }
                System.out.println("Extracted: " + newFile.getName());
                
            } finally {
                fos.close();
                zis.close();
            }

        }
        System.out.println("CLOSE");
		setProgress(100);
		frame.setVisible(false);
		frame = null;
        zf.close();
    }

	private int getPercentage(long current, long length) {
		long cur = current;
		int len = (int) length;
		int percent = (int) ((cur * 100) / (len));
		return percent > 100 ? 100 : percent;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}