package com.android.utility;

import java.io.File;

public class WorkingDirectory {
	
	private static final String DIRECTORY_NAME = "android_workplace";
	private boolean doesExist;
	
	public WorkingDirectory() {
		
		
		
	}
	
	public void doCheckForDirectory() {
		
		
		
	}
	
	public static final String getWorkingDirectory() {
		
		String homeDir = System.getProperty("user.home");
		String workplace = homeDir + File.separator + DIRECTORY_NAME + File.separator;
		return workplace;
		
	}

}
