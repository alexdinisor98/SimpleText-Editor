package com.oxygenxml;

import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class DOMTreeModel implements TreeModel {
	private Node root;
	private EventListenerList listenerList = new EventListenerList();

	public DOMTreeModel() {
		this.root = null;
	}

	public Object getRoot() {
		return root;
	}

	public void setRoot(Node node) {
		this.root = node;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return getFilteredChildren((Node) parent).get(index);
	}

	/**
	 * The children of the tree without the leaves with empty text.
	 * @param parent
	 * @return the list of childNodes
	 */
	private java.util.List<Node> getFilteredChildren(Node parent) {
		java.util.List<Node> childNodesList = new ArrayList<Node>();
		NodeList childNodes = ((Node) parent).getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.TEXT_NODE) {
				if (!item.getNodeValue().trim().isEmpty()) {
					childNodesList.add(item);
				}
			} else {
				childNodesList.add(item);
			}
		}
		return childNodesList;
	}

	@Override
	public int getChildCount(Object parent) {
		return getFilteredChildren((Node) parent).size();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int n = getChildCount(parent);
		for (int i = 0; i < n; i++) {
			if (getChild(parent, i).equals(child)) {
				System.out.println(i);
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (getChildCount(node) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		listenerList.remove(TreeModelListener.class, l);
	}

	public void fireTreeStructureChanged(Object oldRoot) {
		TreeModelEvent event = new TreeModelEvent(this, new Object[] { oldRoot });
		EventListener[] listeners = listenerList.getListeners(TreeModelListener.class);
		for (int i = 0; i < listeners.length; i++)
			((TreeModelListener) listeners[i]).treeStructureChanged(event);
	}

}