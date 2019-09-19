package com.virtubuild.custom.abbeconf.general;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Role;
import com.virtubuild.core.state.StateVar;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

/**
 * ABB eConfigure Column component contains methods to access relevant variables
 * for a Column component
 */
public class Column {

	private Component columnComponent; // associated column Component from configuration
	private Component rackComponent;
	private List<Kit> kits = new LinkedList<>();

	// Model component toa
    public static final String COLUMN_TOA = "column";
	public static final String COLUMN_COMP = "column";

	// Model variable
	private static final String WIDTH_VAR = "availableColumns";
	private static final String HEIGHT_VAR = "availableRows";

	// Model control variables
	private static final String LINEINSERT_CVAR = "lineinsert";

	// Model role variables
	private static final String NEXTLINK_RVAR = "next_link";
	private static final String PREVLINK_RVAR = "prev_link";
	private static final String RACK_RVAR_PREFIX = "kit_frames";

	/**
	 * Takes the associated model component as input
	 * 
	 * @param column
	 */
	public Column(Component column) {
		this.columnComponent = column;
		initRackComponent();
		initKits();
	}

	/**
	 * Initializes the list of Kits
	 */
	private void initKits() {
		if (hasRackComponent()) {
			fetchKits();
		}
	}

	private void initRackComponent() {
		Role frameRole = findFrameRole();
		if (frameRole != null) {
			rackComponent = frameRole.getComponentValue();
		}
	}

	private Role findFrameRole() {
		Collection<Role> roles = (Collection<Role>) columnComponent.getStateVector().getAllRoles();
		for (Role role : roles) {
			if (role.getID().startsWith(RACK_RVAR_PREFIX)) {
				return role;
			}
		}
		return null;
	}

	/**
	 * Returns whether this column contains a rack.
	 */
	public boolean hasRackComponent() {
		return rackComponent != null;
	}

	/**
	 * Gets the column type (component toa)
	 * 
	 * @return ColumnType for this column
	 */
	public ColumnType getType() {
		String type = columnComponent.getTypeID();
		return ColumnType.getColumnType(type);
	}
	
	public boolean checkVariableIsUserSet(String variable) {
		StateVar offset = columnComponent.getStateVector().getVar(variable);
		return offset.isUserSet();
	}

	/**
	 * Returns a list of Kit objects
	 * 
	 * @return kits
	 */
	public List<Kit> getKits() {
		return kits;
	}

	/**
	 * Returns width value
	 * 
	 * @return width
	 */
	public int getWidth() {
		return VariableUtils.getIntegerValue(columnComponent, WIDTH_VAR);
	}

	/**
	 * Returns height value
	 * 
	 * @return height
	 */
	public int getHeight() {
		return VariableUtils.getIntegerValue(columnComponent, HEIGHT_VAR);
	}

	/**
	 * Get the String value of the specified variable
	 * 
	 * @param variableName String
	 * @return String containing the value of the variable
	 */
	public String getStringVariable(String variableName) {
		return VariableUtils.getStringValue(columnComponent, variableName);

	}
	
	/**
     * Set the String value of the specified variable
     * 
     * @param variableName
     *            String
     * @param variableValue
     *            String
	 * @return 
     */
	public boolean setStringVariable(String variableName, String variableValue) {
		VariableUtils.setStringValue(columnComponent, variableName, variableValue);
		return true;
	}

	/**
	 * Get the int value of the specified variable
	 * 
	 * @param variableName String
	 * @return int containing the value of the variable
	 */
	public int getIntegerVariable(String variableName) {
		return VariableUtils.getIntegerValue(columnComponent, variableName);
	}

	/**
	 * Get the boolean value of the specified variable
	 * 
	 * @param variableName String
	 * @return boolean containing the value of the variable
	 */
	public boolean getBooleanVariable(String variableName) {
		return VariableUtils.getBooleanValue(columnComponent, variableName);
	}

	/**
	 * Gets the legal values for the height of this Column
	 * 
	 * @return Collection of legal values in Integer
	 */
	public Collection<Integer> getLegalHeights() {
		return VariableUtils.getLegalValues(columnComponent, HEIGHT_VAR);
	}

	/**
	 * Gets the legal values for the width of this Column
	 * 
	 * @return Collection of legal values in Integer
	 */
	public Collection<Integer> getLegalWidths() {
		return VariableUtils.getLegalValues(columnComponent, WIDTH_VAR);
	}

	/**
	 * Get the kits components for the inside of a column component
	 */
	private void fetchKits() {
		for (Component child : rackComponent.getChildren()) {
			// only get kit components that are legal
			if (Kit.isKitComponent(child) && Kit.isLegalPosition(child, rackComponent)) {
				Kit kit = new Kit(child);
				kits.add(kit);
			}
		}
	}

	/**
	 * Deletes the column if the model allows it
	 */
	public void delete() {
		if (columnComponent.canDelete()) {
			columnComponent.delete();
		}
	}

	/**
	 * Checks if a column is of the specified ColumnType
	 * 
	 * @param ColumnType type of column the check for
	 * @return true if the Column is of the specified type. Otherwise false
	 */
	public boolean isColumnType(ColumnType type) {
		return columnComponent.isTypeOfActor(type.toString());
	}

	/**
	 * Checks whether this Column is the first column in the line
	 * 
	 * @return true if the Column is the first column. Otherwise false
	 */
	public boolean isFirst() {
		Component prevColumn = getPrevColumn();
		return prevColumn == null;
	}

	/**
	 * Checks whether this Column is the last column in the line
	 * 
	 * @return true if the Column is the last column. Otherwise false
	 */
	public boolean isLast() {
		Component nextColumn = getNextColumn();
		return nextColumn == null;
	}

	/**
	 * Gets the previous Column to this one, if there is one
	 * 
	 * @return the previous Column if it exists. Otherwise null
	 */
	public Column getPrevious() {
		Component prev = this.getPrevColumn();
		if (prev == null) {
			return null;
		}
		return new Column(prev);
	}

	/**
	 * Gets the next Column from this one, if there is one
	 * 
	 * @return the next Column if it exists. Otherwise null
	 */
	public Column getNext() {
		Component next = this.getNextColumn();
		if (next == null) {
			return null;
		}
		return new Column(next);
	}

	/**
	 * Get previous column from the previous link role
	 */
	private Component getPrevColumn() {
		return columnComponent.getStateVector().getRole(PREVLINK_RVAR).getComponentValue();
	}

	/**
	 * Gets the next column from the next link role
	 */
	private Component getNextColumn() {
		return columnComponent.getStateVector().getRole(NEXTLINK_RVAR).getComponentValue();
	}

	/**
	 * Inserts a new Column of the specified ColumntType after this Column
	 * 
	 * @param type ColumnType to insert
	 * @return true if the Column was inserted. Otherwise false;
	 */
	public boolean insertAfter(ColumnType type) {
		return columnComponent.getStateVector().getVar(LINEINSERT_CVAR).setValue(type.toString());
	}

}
