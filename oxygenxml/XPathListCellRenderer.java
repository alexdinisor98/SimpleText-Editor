package com.oxygenxml;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.w3c.dom.NamedNodeMap;
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
		// set text for value if node
		if (value instanceof Node) {
			Node node = (Node) value;
			resultString = getResultName(resultString, node);
			this.setText(resultString);
		}
		return this;
	}

	/**
	 * Sets the resultName depending on the nodeTypes.
	 * 
	 * @param resultString outputs the result
	 * @param node         to be executed
	 * @return
	 */
	private String getResultName(String resultString, Node node) {
		if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			resultString = node.getNodeName() + " - " + node.getTextContent() + " ";
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			resultString = node.getNodeValue() + " ";
		} else if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
			resultString = "entity ref - " + node.getTextContent() + " ";
		} else if (node.getNodeType() == Node.COMMENT_NODE) {
			resultString = "comment - " + node.getTextContent() + " ";
		} else if (node.getNodeType() == Node.DOCUMENT_NODE) {
			resultString = "doc - " + node.getTextContent() + " ";
		} else if (node.getNodeType() == Node.ELEMENT_NODE) {
			// call every childNode for output hierarchy
			resultString = node.getNodeName() + ": ";
			if (node.hasChildNodes()) {
				NodeList childNodes = node.getChildNodes();
				int length = node.getChildNodes().getLength();
				for (int j = 0; j < length; j++) {
					Node childNode = childNodes.item(j);
					// call recursively the resultName for each childNode
					resultString += getResultName(null, childNode);
				}
			}
		}

		return resultString;
	}

}
