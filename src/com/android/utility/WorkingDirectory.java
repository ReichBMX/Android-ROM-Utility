package com.android.utility;

import java.io.File;

public class WorkingDirectory {
	
	private static final String DIRECTORY_NAME = "android_workplace";
	private boolean doesExist;
	
	public WorkingDirectory() {
		
		doCheckForDirectory();
		
	}
	
	public void doCheckForDirectory() {
		
		File dir = new File(getWorkingDirectory());
		
		if ((doesExist = dir.exists())) {
			System.out.println("Working Driectory exists");
			return;
		} else {
			dir.mkdir();
			System.out.println("Working Driectory created");
		}
		
	}
	
	public static final String getWorkingDirectory() {
		
		String homeDir = System.getProperty("user.home");
		String workplace = homeDir + File.separator + DIRECTORY_NAME + File.separator;
		return workplace;
		
	}
	
	public boolean doesExist() {
		return doesExist;
	}

}
