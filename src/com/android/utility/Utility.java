package com.android.utility;

import java.awt.EventQueue;

import com.android.utility.ui.UtilityFrame;

/**
 * 
 * @author BmXandXxX
 *
 */
public class Utility {
	
	public static void main(String[] args) {
		
		new WorkingDirectory();
		
		initUI();
		
	}
	
	private static void initUI() {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UtilityFrame frame = new UtilityFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
