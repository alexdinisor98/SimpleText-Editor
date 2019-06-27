package com.oxygenxml;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.Node;

public class XMLTreeCellRenderer extends DefaultTreeCellRenderer {

	public XMLTreeCellRenderer() {
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof Node) {

			Node node = (Node) value;
			String toDisplayString = null;

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				toDisplayString = node.getNodeName();
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				toDisplayString = node.getNodeValue();
			}
			this.setText("<html>" + toDisplayString + "</html>");

			if (leaf) {
				ImageIcon imageIcon = new ImageIcon(XMLTreeCellRenderer.class.getResource("/resources/leaf.png"));
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

				imageIcon = new ImageIcon(newimg);
				this.setIcon(imageIcon);
				imageIcon = (ImageIcon) getLeafIcon();

			} else if (expanded) {
				ImageIcon alreadyExpandedIcon = new ImageIcon(
						XMLTreeCellRenderer.class.getResource("/resources/circle.png"));
				Image image = alreadyExpandedIcon.getImage();
				Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

				alreadyExpandedIcon = new ImageIcon(newimg);
				this.setIcon(alreadyExpandedIcon);
				alreadyExpandedIcon = (ImageIcon) getOpenIcon();
				

			} else {
				ImageIcon expandableIcon = new ImageIcon(XMLTreeCellRenderer.class.getResource("/resources/arrow.png"));
				Image image = expandableIcon.getImage();
				Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);

				expandableIcon = new ImageIcon(newimg);
				this.setIcon(expandableIcon);
				expandableIcon = (ImageIcon) getClosedIcon();
			}
			
			//expandAllNodes(tree, 0, tree.getRowCount());
		}
		return this;

	}

	private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
		for (int i = startingIndex; i < rowCount; ++i) {
			tree.expandRow(i);
		}

		if (tree.getRowCount() != rowCount) {
			expandAllNodes(tree, rowCount, tree.getRowCount());
		}
	}
}
