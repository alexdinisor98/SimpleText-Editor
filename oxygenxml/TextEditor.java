package com.oxygenxml;

import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Text Editor interface for JTextArea.
 */
public interface TextEditor {

	public void read(String fileName, Reader reader) throws IOException;

	public void write(Writer writer) throws IOException;

	public String getText();

	public Font getTextAreaFont();

	public void setTextAreaFont(Font italicFont);

	public void copy();

	public void paste();

	public void replaceSelection(String string);

	public void selectAll();

	public void requestFocus();

	public Component getComponent();

	public String getSelectedText();

}
