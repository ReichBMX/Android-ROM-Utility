package com.android.utility.ui;

import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.android.utility.WorkingDirectory;
import com.android.utility.config.Config;
import com.android.utility.config.VersionConfig;
import com.android.utility.file.FileUtils;
import com.android.utility.zip.Unzipper;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * 
 * @author BmXandXxX
 *
 */
@SuppressWarnings("serial")
public class UtilityFrame extends JFrame implements ActionListener, ListSelectionListener {

	private JPanel contentPane;

	private JList<String> list;
	DefaultListModel<String> listModel;
	
	private String selected;

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

		populateDirectoryList();
		list = new JList<String>(listModel);
		list.addListSelectionListener(this);
		list.setBounds(41, 278, 356, 134);
		contentPane.add(list);

		JButton btnUnzip = new JButton("Unzip");
		btnUnzip.addActionListener(this);
		btnUnzip.setBounds(12, 70, 105, 27);
		contentPane.add(btnUnzip);
		
	}

	private void populateDirectoryList() {
		List<File> files = FileUtils.listFiles(WorkingDirectory.getWorkingDirectory());
		listModel = new DefaultListModel<>();
		for (File f : files) {
			listModel.addElement(f.getName());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		selected = WorkingDirectory.getWorkingDirectory() + list.getSelectedValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		System.out.println("Zip File: " + selected);
		
		String newDir = selected.substring(0, selected.length() - 4);
		
		System.out.println("New Directory: " + newDir);
		File zip = new File(selected);
		File dir = new File(newDir);
		new Unzipper(zip, dir);
		
	}
	

}

