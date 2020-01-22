package com.android.utility.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.android.utility.Utility;
import com.android.utility.WorkingDirectory;
import com.android.utility.config.Config;
import com.android.utility.config.VersionConfig;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UtilityFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public UtilityFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAndroidRomUtility = new JLabel("Android ROM Utility - v" + VersionConfig.VERSION);
		lblAndroidRomUtility.setHorizontalAlignment(SwingConstants.CENTER);
		lblAndroidRomUtility.setBounds(12, 12, 424, 17);
		contentPane.add(lblAndroidRomUtility);
		
		JLabel lblNewLabel = new JLabel("By " + Config.AUTHOR);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 41, 424, 17);
		contentPane.add(lblNewLabel);
		
		JLabel lblWorkingDirectory = new JLabel("Working Directory: " + WorkingDirectory.getWorkingDirectory());
		lblWorkingDirectory.setBounds(12, 474, 424, 17);
		contentPane.add(lblWorkingDirectory);
	}
}
