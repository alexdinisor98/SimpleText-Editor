package com.oxygenxml;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Simple Text Editor menu.
 */
public class FileMenu extends JMenu {

	/**
	 * Create the file menu with open, save and URL item.
	 * 
	 * @param textEditor
	 * @param openListener
	 * @param saveListener
	 */

	public FileMenu(TextEditor textEditor, ActionListener openListener, ActionListener saveListener) {
		super("File");

		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(openListener);
		add(openItem);

		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(saveListener);
		add(saveItem);

		JMenuItem openURLItem = new JMenuItem("Open URL...");

		openURLItem.addActionListener(new ActionListener() {
			/**
			 * ActionListener for openURL item which opens a file from a specified input
			 * path.
			 */
			@Override
			public void actionPerformed(ActionEvent ev) {

				String stringURL = JOptionPane.showInputDialog("Enter URL: ");
				File file = new File(stringURL);
				URL url = null;

				// Throws MalformedURLException when the string cannot match a legal path
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException e) {
					JOptionPane optionPane = new JOptionPane("No legal protocol could be found",
							JOptionPane.ERROR_MESSAGE);
					JDialog dialog = optionPane.createDialog("Failure");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);

					e.printStackTrace();
				}

				// Throws IOException if no permissions to open the file
				try {
					textEditor.read(url.toString(), new InputStreamReader(url.openStream()));
				} catch (IOException ee) {
					JOptionPane optionPane = new JOptionPane("Cannot open this file", JOptionPane.ERROR_MESSAGE);
					JDialog dialog = optionPane.createDialog("Failure");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);

					ee.printStackTrace();
				}

			}
		});
		add(openURLItem);
	}
}
