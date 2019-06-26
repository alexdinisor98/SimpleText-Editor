package com.oxygenxml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.io.*;

//frame
public class MyOutlineTest extends JFrame{
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	File file;
	Document doc;
	JTree tree;
	JScrollPane scroll;
	String filenameString = "C:\\Users\\test\\Documents\\OxygenXMLEditor\\samples\\smartphones.xml";

	public MyOutlineTest() {
		try {
			
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			file = new File("C:\\Users\\test\\Documents\\OxygenXMLEditor\\samples\\smartphones.xml");
			doc = builder.parse(file);
			String treeHierarchyString = "";
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeHierarchyString);

			tree = new JTree(node);
			Element e = doc.getDocumentElement();

			if (e.hasChildNodes()) {
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(e.getTagName());
				NodeList children = e.getChildNodes();

				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);

					visit(child, root);
				}
				node.add(root);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void visit(Node child, DefaultMutableTreeNode parent) {
		short type = child.getNodeType();
		if (type == Node.ELEMENT_NODE) {
			Element e = (Element) child;
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(e.getTagName());
			parent.add(node);

			if (e.hasChildNodes()) {
				NodeList list = e.getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					visit(list.item(i), node);
				}
			}

		} else if (type == Node.TEXT_NODE) {
			Text t = (Text) child;
			String textContent = t.getTextContent();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(textContent);
			parent.add(node);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyOutlineTest();
			}
		});
	}
}