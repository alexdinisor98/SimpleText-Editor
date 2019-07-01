package com.oxygenxml;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class TreeUpdatesLabel extends JLabel {

	/**
	 * Set the label for updates in the tree.
	 * @param text of the label
	 * @param horizontalAlignment
	 */
	public TreeUpdatesLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		
		this.setBackground(Color.white);
		this.setForeground(Color.black);
		
        // set the border of the label
        //Border border = BorderFactory.createLineBorder(Color.black, 3);
        //this.setBorder(border);
        
		this.setVisible(true);
		this.setOpaque(true);
		this.setMinimumSize(new Dimension(1000, 80));
	}
}
