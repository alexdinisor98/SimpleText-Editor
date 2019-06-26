package com.oxygenxml;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import javax.swing.filechooser.FileSystemView;

/**
 * Simple Text Editor frame.
 */
public class SimpleTextEditorFrame extends JFrame {

	private EditorArea textArea = new EditorArea();

	private ActionListener openListener;
	private ActionListener saveListener;

	public SimpleTextEditorFrame() {
		super("Alex SimpleText Editor");

		setSize(1000, 1000);
		setResizable(true);

		JMenuBar menuBar = new JMenuBar();

		// Listener for open file button/item
		openListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(SimpleTextEditorFrame.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					openFileAction(selectedFile);
				} else {
					// User cancelled
				}
			}
		};

		// ActionListener for save button/item.
		saveListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				saveFileAction();
			}

		};
		FileMenu menu = new FileMenu(textArea, openListener, saveListener);
		menuBar.add(menu);
		setJMenuBar(menuBar);

		MyToolBar toolbar = new MyToolBar(textArea, openListener, saveListener);
		getContentPane().add(toolbar, BorderLayout.NORTH);

		getContentPane().add(textArea, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new SimpleTextEditorFrame();
	}

	/**
	 * Read some text from the resource file to display in the textArea or show a
	 * fail dialog for exceptions.
	 */
	private void openFileAction(File selectedFile) {
		try {
			String filenameString = selectedFile.toURI().toURL().toString();
			textArea.read(filenameString, new InputStreamReader(new FileInputStream(selectedFile), "UTF-8"));

		} catch (IOException e) {
			JOptionPane optionPane = new JOptionPane("Cannot open this file", JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Failure");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);

			e.printStackTrace();
		}
	}

	/**
	 * Save the modified file displayed in the textArea or show a fail dialog for
	 * exceptions.
	 */
	private void saveFileAction() {
		try {
			String s = textArea.getText();
			if (s.length() > 0) {
				JFileChooser fileChooser = new JFileChooser();
				int retval = fileChooser.showSaveDialog(SimpleTextEditorFrame.this);
				if (retval == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					if (!file.getName().toLowerCase().endsWith(".txt")) {
						file = new File(file.getParentFile(), file.getName() + ".txt");
					}
					try {
						textArea.write(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
					} catch (Exception e) {
						JOptionPane optionPane = new JOptionPane("Cannot save this file", JOptionPane.ERROR_MESSAGE);
						JDialog dialog = optionPane.createDialog("Failure");
						dialog.setAlwaysOnTop(true);
						dialog.setVisible(true);

						e.printStackTrace();
					}
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
