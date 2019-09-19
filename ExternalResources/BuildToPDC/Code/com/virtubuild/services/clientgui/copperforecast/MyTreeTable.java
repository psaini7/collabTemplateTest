package com.virtubuild.services.clientgui.copperforecast;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.coderazzi.filters.gui.TableFilterHeader;
import net.coderazzi.filters.gui.TableFilterHeader.Position;

public class MyTreeTable extends JTable {

	private static MyTreeTableCellRenderer tree;

	// Tree nodes are expanded by default for readability.

	public static boolean treeNodesExpanded = false;

	/**
	 * Expands all the nodes and it's child node of the BOM table
	 */
	public static void expandAllNodes() {

		int nodesCount = 0;
		while (nodesCount < tree.getRowCount()) {
			tree.expandRow(nodesCount);
			nodesCount++;

		}

		treeNodesExpanded = true;

	}
	
	/**
	 * Collapses all the nodes and it's child node of the BOM table
	 */
	public static void collapseAllNodes() {

		int nodesCount = 0;
		while (nodesCount < tree.getRowCount()) {
			tree.expandRow(nodesCount);
			nodesCount++;
		}

		while (nodesCount >= 0) {
			tree.collapseRow(nodesCount);
			nodesCount--;
		}

		treeNodesExpanded = false;

	}

	public MyTreeTable(MyAbstractTreeTableModel treeTableModel) {
		super();

		// JTree create
		tree = new MyTreeTableCellRenderer(this, treeTableModel);
		
		// Model.
		super.setModel(new MyTreeTableModelAdapter(treeTableModel, tree));

		// Simultaneous selection for tree and table.
		MyTreeTableSelectionModel selectionModel = new MyTreeTableSelectionModel();
		tree.setSelectionModel(selectionModel); // For the tree
		putClientProperty("Tree.paintLines", Boolean.TRUE);
		tree.setRootVisible(false);
		setSelectionModel(selectionModel.getListSelectionModel()); // For the table

		tree.setShowsRootHandles(true);

		// Renderer for the tree.

		TableCellRenderer cellRenderer = new CellRenderer();

		try {
			setDefaultRenderer(Class.forName("com.virtubuild.services.clientgui.custom.MyTreeTableModel"), cellRenderer);
		} catch (ClassNotFoundException e) {

		}

		setDefaultRenderer(MyTreeTableModel.class, tree);

		// Editor for the TreeTable
		setDefaultEditor(MyTreeTableModel.class, new MyTreeTableCellEditor(tree, this));

		// Show no grid

		setShowGrid(true); // -----------------------Showing no Gridlines for Jtable
		setGridColor(Color.BLACK);
		setSelectionBackground(new Color(206, 218, 229));
		setSelectionForeground(Color.BLACK);
		setShowHorizontalLines(true); // ------------------------Showing horizontal Gridlines for Jtable
		setShowVerticalLines(true);
		// getTableHeader().setUI(null); //-----------------------to hide the header row
		// of table

		// No events.
		setIntercellSpacing(new Dimension(5, 5));


		ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/zoomin.png"));

		LocalRenderer localCellRenderer = new LocalRenderer();

		localCellRenderer.setOpenIcon(imageIcon);
		localCellRenderer.setLeafIcon(imageIcon);

		tree.setCellRenderer(localCellRenderer);

		// By default expanding all the nodes of the BOM Tree

		int nodesCount = 0;
		while (MyTreeTable.treeNodesExpanded && nodesCount < tree.getRowCount()) // Making sure the tree remains
																					// collapsed or expanded when user
																					// control goes to other tab.
		{
			tree.expandRow(nodesCount);
			nodesCount++;
		}

	}
}

class LocalRenderer extends DefaultTreeCellRenderer {


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasfocus) {
		DefaultTreeCellRenderer result = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel,
				expanded, leaf, row, hasfocus);

		result.setOpaque(true);
		
		if (true) {
			result.setIcon(null);
		}
		if (sel) {
			result.setBackgroundSelectionColor(new Color(184, 207, 229));
			result.setForeground(Color.BLACK);
		} else {

			result.setBackgroundSelectionColor(Color.WHITE);

		}

		return (result);
	}

}

class CellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		 /*JLabel lbl = new JLabel();

		 ImageIcon icon = new ImageIcon(getClass().getResource("resources/download_icon.png"));
		 
		 lbl.setText((String) value);
		 lbl.setIcon(icon);
		 lbl.setBounds(0, 0, 100, 100);
		 
		 return lbl;*/

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		return c;
	}

}
