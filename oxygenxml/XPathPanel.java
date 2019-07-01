package com.oxygenxml;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XPathPanel extends JPanel {
	private JTextField xpathExpressionField = new JTextField(30);
	private JTextField resultField;
	private JComboBox<String> typeCombo = new JComboBox(new String[] { "STRING", "NODESET" });

	public JComboBox<String> getTypeCombo() {
		return typeCombo;
	}

	public void setTypeCombo(JComboBox<String> typeCombo) {
		this.typeCombo = typeCombo;
	}

	private XPath xpath;
	private Document doc;

	public XPathPanel() {
		typeCombo.setSelectedItem("STRING");
		EvaluateListener evaluateListener = new EvaluateListener();
		xpathExpressionField.setVisible(true);

		xpathExpressionField.addActionListener(evaluateListener);
		JButton evaluateButton = new JButton("Evaluate");
		evaluateButton.addActionListener(evaluateListener);
		add(xpathExpressionField, BorderLayout.NORTH);
		add(typeCombo, BorderLayout.EAST);
		add(evaluateButton, BorderLayout.WEST);

		resultField = new JTextField();
		resultField.setBorder(new TitledBorder("Result"));
		add(resultField, BorderLayout.SOUTH);

		XPathFactory xpfactory = XPathFactory.newInstance();
		xpath = xpfactory.newXPath();
	}

}
