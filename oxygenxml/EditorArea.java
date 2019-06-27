package com.oxygenxml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The main panel for the editor which overrides the JTextArea used methods.
 */

public class EditorArea extends JPanel implements TextEditor {
	private JTextArea textArea = new JTextArea();

	private MyContextMenu contextualMenu;

	private JScrollPane textScrollPane = new JScrollPane(textArea);
	private JTree tree = new JTree();
	private TreeUpdatesLabel updatesLabel;

	public JLabel getUpdatesLabel() {
		return this.updatesLabel;
	}

	private JScrollPane treeScrollPane = new JScrollPane(tree);

	String filenameString;

	/**
	 * EditorArea includes textArea, scrollPane and contextual menu.
	 */
	public EditorArea() {
		setLayout(new BorderLayout());
		textArea.setEditable(true);

		contextualMenu = new MyContextMenu(this);
		contextualMenu.addTo(textArea);

		updatesLabel = new TreeUpdatesLabel(" ", SwingConstants.CENTER);

		treeScrollPane.setMinimumSize(new Dimension(60, 100));

		// splitPlane between the textArea and the Outliner
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, treeScrollPane);
		splitPane.setOrientation(SwingConstants.VERTICAL);
		splitPane.setOneTouchExpandable(true);

		splitPane.setDividerLocation(700 + splitPane.getInsets().left);
		add(splitPane, BorderLayout.CENTER);

		tree.setModel(null);
		// Listener for updating the tree while modifying the textArea
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshTree();

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshTree();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshTree();
			}
		});

		XMLTreeCellRenderer xmlTreeCellRenderer = new XMLTreeCellRenderer();
		tree.setCellRenderer(xmlTreeCellRenderer);

		setVisible(true);

	}

	@Override
	public void read(String fileName, Reader reader) throws IOException {
		this.filenameString = fileName;
		// textArea.read(reader, null);

		char[] buf = new char[1024];
		int len = -1;
		StringBuilder sBuilder = new StringBuilder();
		while ((len = reader.read(buf)) != -1) {
			sBuilder.append(buf, 0, len);
		}
		textArea.setText(sBuilder.toString());
	}

	/*
	 * Updating the tree while making changes to textArea.
	 */
	private void refreshTree() {
		new Thread() {
			@Override
			public void run() {
				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder;
					builder = factory.newDocumentBuilder();
					InputSource is = new InputSource(filenameString);
					is.setCharacterStream(new StringReader(textArea.getText()));
					Document doc;
					doc = builder.parse(is);
					DOMTreeModel treeModel = new DOMTreeModel();

					treeModel.setRoot(doc.getDocumentElement());
					tree.setModel(treeModel);
					tree.setRootVisible(false);
					XMLTreeCellRenderer xmlTreeCellRenderer = new XMLTreeCellRenderer();
					tree.setCellRenderer(xmlTreeCellRenderer);

					updatesLabel.setText("Sucessful parsing");
					updatesLabel.setForeground(Color.green);

				} catch (SAXException | ParserConfigurationException | IOException e) {
					updatesLabel.setText("Failed to parse: " + e.getMessage());
					updatesLabel.setForeground(Color.red);
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public String getText() {
		return textArea.getText();
	}

	@Override
	public Font getTextAreaFont() {
		return textArea.getFont();
	}

	@Override
	public void setTextAreaFont(Font font) {
		textArea.setFont(font);
	}

	@Override
	public void copy() {
		textArea.copy();
	}

	@Override
	public void paste() {
		textArea.paste();
	}

	@Override
	public void replaceSelection(String string) {
		textArea.replaceSelection(string);
	}

	@Override
	public void selectAll() {
		textArea.selectAll();
	}

	@Override
	public Component getComponent() {
		return textArea;
	}

	@Override
	public String getSelectedText() {
		return textArea.getSelectedText();
	}

	@Override
	public void write(Writer writer) throws IOException {
		textArea.write(writer);
	}

}
