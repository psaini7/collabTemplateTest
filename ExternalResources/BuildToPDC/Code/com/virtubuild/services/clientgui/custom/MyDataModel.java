package com.virtubuild.services.clientgui.custom;

public class MyDataModel extends MyAbstractTreeTableModel {
    

	static protected String[] columnNames = { "Column/ Category", "Product ID", "Typecode", "Position", "Quantity", "UoM", "Description", "Instruction Manual", "Weight",
			"Gross Price", "Gross Price Sum", "Link"};

	static protected Class<?>[] columnTypes = { MyTreeTableModel.class, String.class, String.class, String.class, Float.class,
			String.class, String.class, String.class, Float.class, Float.class, Float.class, String.class };

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
		    return ((MyDataNode) node).getProductType();
		case 1:
			return ((MyDataNode) node).getProductID();
		case 2:
			return ((MyDataNode) node).getOrderNumber();
		case 3:
			return ((MyDataNode) node).getPosition();
		case 4:
			return ((MyDataNode) node).getQuantity();
		case 5:
			return ((MyDataNode) node).getUoM();
		case 6:
			return ((MyDataNode) node).getDesc();
		case 7: 
			return ((MyDataNode) node).getInstructionManual();
		case 8:
			return ((MyDataNode) node).getWeight();
		case 9:
			return ((MyDataNode) node).getGrossPrice();
		case 10:
			return ((MyDataNode) node).getGrossPriceSum();
		case 11:
			return ((MyDataNode) node).getLink();

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
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			return false;
		default:
			break;

		}

		return true; // Important to activate TreeExpandListener
	}

	public void setValueAt(Object aValue, Object node, int column) {

		switch (column) {
		    
	    case 1:
	        ((MyDataNode) node).setProductType((String) aValue);
            break;
		case 2:
			((MyDataNode) node).setProductID((String) aValue);
			break;
		case 3:
			((MyDataNode) node).setOrderNumber((String) aValue);
			break;
		case 4:
			((MyDataNode) node).setPosition((String) aValue);
			break;
		case 5:
			((MyDataNode) node).setQuantity((Float) aValue);
			break;
		case 6:
			((MyDataNode) node).setUoM((String) aValue);
			break;
		case 7:
			((MyDataNode) node).setDesc((String) aValue);
			break;
		case 8:
			((MyDataNode) node).setInstructionManual((String) aValue);
			break;
		case 9:
			((MyDataNode) node).setWeight((Float) aValue);
			break;
		case 10:
			((MyDataNode) node).setGrossPrice((Float) aValue);
			break;
		case 11:
			((MyDataNode) node).setGrossPriceSum((float) aValue);
			break;
		case 12:
			((MyDataNode) node).setLink((String) aValue);
			break;

		default:
			break;
		}

	}

}
