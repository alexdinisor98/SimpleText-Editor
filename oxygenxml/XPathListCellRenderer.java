package com.oxygenxml;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathListCellRenderer extends JLabel implements ListCellRenderer {

	public XPathListCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		String resultString = null;
		//set text for value if node
		if (value instanceof Node) {
			Node node = (Node) value;
			resultString = getResultName(resultString, node);
			this.setText(resultString);
		}
		return this;
	}

	/**
	 * Sets the resultName depending on the nodeTypes.
	 * @param resultString outputs the result
	 * @param node to be executed
	 * @return
	 */
	private String getResultName(String resultString, Node node) {
		if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			resultString = node.getNodeName() + " - " + node.getTextContent();
		}

		if (node.getNodeType() == Node.TEXT_NODE) {
			resultString = "text - " + node.getTextContent();
		}

		if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
			resultString = "ref - " + node.getTextContent();
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
            // call every childNode for output hierarchy
			resultString = node.getNodeName() + ": ";
			if (node.hasChildNodes()) {
				NodeList childNodes = node.getChildNodes();
				int length = node.getChildNodes().getLength();
				for (int j = 0; j < length; j++) {
					Node childNode = childNodes.item(j);
					System.out.println(
							"NODE NAME " + childNode.getNodeName() + " CONTENT S " + childNode.getTextContent());
					
					resultString += childNode.getNodeName() + ": " + childNode.getTextContent() + " ";
					
				}
			}
		}

		return resultString;
	}

}
