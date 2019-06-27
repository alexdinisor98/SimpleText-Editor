package com.oxygenxml;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

public class TreeUpdatesLabel extends JLabel {

	/**
	 * Set the label for updates in the tree.
	 * @param text of the label
	 * @param horizontalAlignment
	 */
	public TreeUpdatesLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		
		this.setBackground(Color.black);
		this.setVisible(true);
		this.setOpaque(true);
		this.setMinimumSize(new Dimension(1000, 80));
	}

}
