package com.oxygenxml;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Contextual menu for the text editor area.
 */
public class MyContextMenu extends JPopupMenu {

	private TextEditor textEditor;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenuItem delete;
	private JMenuItem selectAll;

	/**
	 * It includes copy, paste, delete & selectAll buttons with the keyboard
	 * shortcuts.
	 * 
	 * @param textEditor
	 */
	public MyContextMenu(TextEditor textEditor) {
		this.textEditor = textEditor;
		copy = new JMenuItem("Copy");
		copy.setEnabled(false);
		copy.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		copy.addActionListener(event -> textEditor.copy());
		add(copy);

		paste = new JMenuItem("Paste");
		paste.setEnabled(false);
		paste.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		paste.addActionListener(event -> textEditor.paste());
		add(paste);

		delete = new JMenuItem("Delete");
		delete.setEnabled(false);
		delete.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		delete.addActionListener(event -> textEditor.replaceSelection(""));
		add(delete);

		add(new JSeparator());

		selectAll = new JMenuItem("Select All");
		selectAll.setEnabled(false);
		selectAll.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		selectAll.addActionListener(event -> textEditor.selectAll());
		add(selectAll);
	}

	public void addTo(JTextComponent textComponent) {

		textComponent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent releasedEvent) {
				handleContextMenu(releasedEvent);
			}

			@Override
			public void mouseReleased(MouseEvent releasedEvent) {
				handleContextMenu(releasedEvent);
			}

			@Override
			public void mouseClicked(MouseEvent releasedEvent) {
				if (SwingUtilities.isRightMouseButton(releasedEvent)) {
					handleContextMenu(releasedEvent);
				}
			}
		});

	}

	public void handleContextMenu(MouseEvent releasedEvent) {
		if (SwingUtilities.isRightMouseButton(releasedEvent)) {
			processClick(releasedEvent);
		}
	}

	/**
	 * Enable the buttons when necessary.
	 */
	public void processClick(MouseEvent event) {
		textEditor.requestFocus();

		boolean enableCopy = false;
		boolean enablePaste = false;
		boolean enableDelete = false;
		boolean enableSelectAll = false;

		String selectedText = textEditor.getSelectedText();
		String text = textEditor.getText();

		if (text != null) {
			if (text.length() > 0) {
				enableSelectAll = true;
			}
		}

		if (selectedText != null) {
			if (selectedText.length() > 0) {
				enableCopy = true;
				enableDelete = true;
			}
		}

		if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			enablePaste = true;
		}

		copy.setEnabled(enableCopy);
		paste.setEnabled(enablePaste);
		delete.setEnabled(enableDelete);
		selectAll.setEnabled(enableSelectAll);

		// shows the popUp menu
		show(textEditor.getComponent(), event.getX(), event.getY());
	}

}
