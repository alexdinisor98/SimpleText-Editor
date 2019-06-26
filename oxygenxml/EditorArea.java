package com.oxygenxml;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The main panel for the editor which overrides the JTextArea used methods.
 */

public class EditorArea extends JPanel implements TextEditor {
	private JTextArea textArea = new JTextArea();

	private MyContextMenu contextualMenu;

	private JScrollPane textScrollPane = new JScrollPane(textArea);
	private JTree tree = new JTree();
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

		treeScrollPane.setMinimumSize(new Dimension(60, 100));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, treeScrollPane);
		splitPane.setOrientation(SwingConstants.VERTICAL);
		splitPane.setOneTouchExpandable(true);

		splitPane.setDividerLocation(700 + splitPane.getInsets().left);
		add(splitPane, BorderLayout.CENTER);
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

		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document doc;

		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();

			doc = builder.parse(fileName);
			DOMTreeModel treeModel = new DOMTreeModel();
			treeModel.setRoot(doc.getDocumentElement());

			tree.setModel(treeModel);
			tree.setRootVisible(false);

			XMLTreeCellRenderer xmlTreeCellRenderer = new XMLTreeCellRenderer();
			tree.setCellRenderer(xmlTreeCellRenderer);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
