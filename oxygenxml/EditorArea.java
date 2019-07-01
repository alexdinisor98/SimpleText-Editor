package com.oxygenxml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The main panel for the editor which overrides the JTextArea used methods.
 */

public class EditorArea extends JPanel implements TextEditor {

	public static final int TIMER_DELAY = 500;
	private ActionListener timerListener = new TimerListener();
	private Timer updateTreeTimer = new Timer(TIMER_DELAY, timerListener);

	private JTextArea textArea = new JTextArea();
	private MyContextMenu contextualMenu;

	private JScrollPane textScrollPane = new JScrollPane(textArea);
	private JTree tree = new JTree();
	private TreeUpdatesLabel updatesLabel;

	public JLabel getUpdatesLabel() {
		return this.updatesLabel;
	}

	private JScrollPane treeScrollPane = new JScrollPane(tree);
	private String filenameString;

	private Document doc;
	/**
	 * Friendly for tests
	 */
	XPathPanel xPanel;

	/**
	 * EditorArea includes textArea, scrollPane and contextual menu.
	 */
	public EditorArea() {
		setLayout(new BorderLayout());
		textArea.setEditable(true);
		updateTreeTimer.setRepeats(false);

		contextualMenu = new MyContextMenu(this);
		contextualMenu.addTo(textArea);

		xPanel = new XPathPanel();
		add(xPanel, BorderLayout.NORTH);

		// label for warnings and errors on the XML file
		updatesLabel = new TreeUpdatesLabel("No input file", SwingConstants.CENTER);

		treeScrollPane.setMinimumSize(new Dimension(60, 100));

		// splitPlane between the textArea and the OutLiner
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, treeScrollPane);
		splitPane.setOrientation(SwingConstants.VERTICAL);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(700 + splitPane.getInsets().left);
		add(splitPane, BorderLayout.CENTER);
		tree.setModel(null);

		// Listener for updating the tree while modifying the textArea
		DocumentListener treeListener = new TreeDocumentListener();
		textArea.getDocument().addDocumentListener(treeListener);

		// add a customized cell renderer on JTree for the XML file
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
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(filenameString);
			is.setCharacterStream(new StringReader(textArea.getText()));

			doc = builder.parse(is);
			DOMTreeModel treeModel = new DOMTreeModel();

			// set the treeModel with the current document root
			treeModel.setRoot(doc.getDocumentElement());
			tree.setModel(treeModel);
			tree.setRootVisible(false);
			XMLTreeCellRenderer xmlTreeCellRenderer = new XMLTreeCellRenderer();
			tree.setCellRenderer(xmlTreeCellRenderer);

			// if success in the parsed XML file
			updatesLabel.setText("Successful parsing");
			updatesLabel.setForeground(Color.green);

		} catch (SAXException | ParserConfigurationException | IOException e) {
			// show error messages to the user in the label
			updatesLabel.setText("Failed to parse: " + e.getMessage());
			updatesLabel.setForeground(Color.red);
//			e.printStackTrace();
		}
	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Notify the tree about the change.
			// System.out.println("Triggered");
			refreshTree();
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

	class TreeDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			updateTreeTimer.restart();

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			updateTreeTimer.restart();

		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateTreeTimer.restart();

		}

	}

	class EvaluateListener implements ActionListener {

		private DefaultListModel<Node> dataListModel;

		public EvaluateListener(DefaultListModel<Node> dataListModel) {
			this.dataListModel = dataListModel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			evaluate();

		}

		/**
		 * Evaluate the XPath given by matching its content
		 */
		public void evaluate() {
			try {

				System.err.println("DOC " + doc);
				NodeList resNodeList = (NodeList) XPathFactory.newInstance().newXPath().compile(xPanel.getText())
						.evaluate(doc, XPathConstants.NODESET);

				dataListModel.clear();
				// add in the resNode list all the childNodes
				if (resNodeList != null) {
					int lenList = resNodeList.getLength();

					for (int i = 1; i <= lenList; i++) {
						Node resNode = (Node) resNodeList.item(i - 1);
						dataListModel.addElement(resNode);
					}
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class XPathPanel extends JPanel {
		/**
		 * Friendly for tests
		 */
		JTextField xpathExpressionField = new JTextField(40);
		JList<Node> resultlist = null;
		private DefaultListModel<Node> dataListModel = new DefaultListModel<>();
		EvaluateListener evaluateListener = new EvaluateListener(dataListModel);

		public XPathPanel() {

			setLayout(new GridBagLayout());
			boolean shouldFill = true;
			boolean shouldWeightX = true;

			GridBagConstraints c = new GridBagConstraints();

			if (shouldFill) {
				// natural height, maximum width
				c.fill = GridBagConstraints.HORIZONTAL;
			}
			// add XPath field on the WEST side and Evaluate button on the EAST side,
			// proportionally
			xpathExpressionField.setBorder(new TitledBorder("XPath"));
			xpathExpressionField.setVisible(true);
			xpathExpressionField.addActionListener(evaluateListener);
			if (shouldWeightX) {
				c.weightx = 0.5;
			}
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			add(xpathExpressionField, c);

			JButton evaluateButton = new JButton("Evaluate");
			evaluateButton.addActionListener(evaluateListener);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.5;
			c.gridx = 1;
			c.gridy = 0;
			add(evaluateButton, c);

			// add a scrollPane for the resultList under the 2 fields
			resultlist = new JList<>(dataListModel);
			XPathListCellRenderer xPathRenderer = new XPathListCellRenderer();
			resultlist.setCellRenderer(xPathRenderer);
			JScrollPane listScroller = new JScrollPane(resultlist);

			c.fill = GridBagConstraints.HORIZONTAL;
			// make this component tall
			c.ipady = 20;
			c.weightx = 0.0;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 1;
			add(listScroller, c);
		}

		public String getText() {
			return xpathExpressionField.getText();
		}

	}

}
