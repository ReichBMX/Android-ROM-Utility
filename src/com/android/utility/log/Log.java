package com.android.utility.log;

import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

public class Log {
	
	public static void normal(String log) {
		Logger.getLogger(Log.class).log(Level.INFO, log);
	}

}
