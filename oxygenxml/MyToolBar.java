package com.oxygenxml;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

/**
 * Editor's toolBar with buttons for open, save, bold and italic 
 * on the left side of the toolBar
 * And combo boxes for font type and font size on the right side of the toolBar.
 */
public class MyToolBar extends JToolBar {

	static ImageIcon saveIcon = new ImageIcon(new ImageIcon(MyToolBar.class.getResource("/resources/save.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
	static ImageIcon openIcon = new ImageIcon(new ImageIcon(MyToolBar.class.getResource("/resources/open.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
	static ImageIcon boldIcon = new ImageIcon(new ImageIcon(MyToolBar.class.getResource("/resources/bold.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
	static ImageIcon italicIcon = new ImageIcon(new ImageIcon(MyToolBar.class.getResource("/resources/italic.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

	private JButton openButton = new JButton(openIcon);
	private JButton saveButton = new JButton(saveIcon);
	private JButton boldButton = new JButton(boldIcon);
	private JButton italicButton = new JButton(italicIcon);

	// combo boxes with all the possible font types and several font sizes
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private String[] fonts = ge.getAvailableFontFamilyNames();
	private JComboBox<String> fontTypeCombo = new JComboBox<>(fonts);
	private JComboBox<Integer> fontSizeCombo = new JComboBox(new Integer[] { 12, 14, 16, 18, 20, 22, 24 });

	public MyToolBar(TextEditor textEditor, ActionListener openListener, ActionListener saveListener) {

		fontTypeCombo.addItemListener(new ItemListener() {
			/**
			 * ItemListener for the selected font type.
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Font selectedFont = new Font((String) fontTypeCombo.getSelectedItem(), Font.PLAIN, 12);
					textEditor.setTextAreaFont(selectedFont);
				}
			}
		});

		fontSizeCombo.addItemListener(new ItemListener() {
			/**
			 * ItemListener for getting the selected font sizes keeping the current font
			 * type.
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					float size = (Integer) fontSizeCombo.getSelectedItem();
					Font deriveFont = textEditor.getTextAreaFont().deriveFont((float) size);
					textEditor.setTextAreaFont(deriveFont);
				}
			}
		});
		setRollover(true);

		add(openButton);
		openButton.addActionListener(openListener);

		addSeparator();

		add(saveButton);
		saveButton.addActionListener(saveListener);

		addSeparator();

		add(boldButton);

		// ActionListener for the selected bold text.
		boldButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				Font boldFont = textEditor.getTextAreaFont().deriveFont(Font.BOLD);
				textEditor.setTextAreaFont(boldFont);
			}
		});
		addSeparator();

		add(italicButton);

		// ActionListener for the selected italic text
		italicButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				Font italicFont = textEditor.getTextAreaFont().deriveFont(Font.ITALIC);
				textEditor.setTextAreaFont(italicFont);
			}
		});
		addSeparator();

		add(fontTypeCombo);
		add(fontSizeCombo);

	}
}
