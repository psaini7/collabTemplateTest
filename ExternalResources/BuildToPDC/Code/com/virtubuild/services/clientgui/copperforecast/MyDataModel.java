package com.virtubuild.services.clientgui.copperforecast;

public class MyDataModel extends MyAbstractTreeTableModel {
    

	static protected String[] columnNames = { "Column No.", "Width", "Depth", "Length", "Weight", "Unit"};

	static protected Class<?>[] columnTypes = { MyTreeTableModel.class, Float.class, Float.class, Float.class, Double.class,
			String.class};

	public MyDataModel(MyDataNode rootNode) {
		super(rootNode);
		root = rootNode;
	}

	public Object getChild(Object parent, int index) {
		return ((MyDataNode) parent).getChildren().get(index);
	}

	public int getChildCount(Object parent) {
		return ((MyDataNode) parent).getChildren().size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Class<?> getColumnClass(int column) {
		return columnTypes[column];
	}

	public Object getValueAt(Object node, int column) {
		switch (column) {
		    
		case 0:
		    return ((MyDataNode) node).getColumnNumberDesc();
		case 1:
			return ((MyDataNode) node).getWidth();
		case 2:
			return ((MyDataNode) node).getDepth();
		case 3:
			return ((MyDataNode) node).getLength();
		case 4:
			return ((MyDataNode) node).getWeight();
		case 5:
			return ((MyDataNode) node).getUnit();
		default:
			break;
		}
		return null;
	}

	public boolean isCellEditable(Object node, int column) {
		switch (column) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return false;
		default:
			break;

		}

		return true; // Important to activate TreeExpandListener
	}

	public void setValueAt(Object aValue, Object node, int column) {

		switch (column) {
		    
	    case 1:
	        ((MyDataNode) node).setColumnNumberDesc((String) aValue);
            break;
		case 2:
			((MyDataNode) node).setWidth((Float) aValue);
			break;
		case 3:
			((MyDataNode) node).setDepth((Float) aValue);
			break;
		case 4:
			((MyDataNode) node).setLength((Float) aValue);
			break;
		case 5:
			((MyDataNode) node).setWeight((Double) aValue);
			break;
		case 6:
			((MyDataNode) node).setUnit((String) aValue);
			break;

		default:
			break;
		}

	}

}
