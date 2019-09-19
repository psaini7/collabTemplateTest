/****************************************************************************
 * NpeOffsetRbbsAutoCompleter                                 				*
 * Represents a Auto Completer class for insertion and deletion of 	        *
 * NPE Offset to solve all the illegal offset positions on a single click 	*
 ***************************************************************************/
/**
    @author Aagreet Sinha
    @version 1.1 03/10/18 
 */
package com.virtubuild.services.abbeconf.npeoffsetrbbs;

import java.util.List;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.custom.abbeconf.general.Column;
import com.virtubuild.custom.abbeconf.general.ColumnType;
import com.virtubuild.custom.abbeconf.general.Switchboard;

public class NpeOffsetRbbsManager {

	private Configuration configurator;
	private static final String NPE_POSITION_VAR = "NPE_position";
	private static final String BOTH = "both";
	private static final String NO = "no";
	private static final String YES = "Yes";
	private static final String OFFSET = "offset";
	private static final String CONNECTION = "connection";
	private static final String BOTTOM = "bottom";
	private static final String TOP = "top";
	private static final String NONE = "none";
	private static final String DEVICE_CONNECTION = "device_connection";
	private static final String PREV_CONNECTION = "prev_connection";
	private static final String TRIGGER_OFFSET = "trigger_offset";
	private static final String IS_SET_BY_COMPLETER = "is_set_by_completer";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public NpeOffsetRbbsManager(Configuration configurator) {
		this.configurator = configurator;
	}

	public void run() {
		Switchboard switchboard = getSwitchboard();

		List<Column> columns = switchboard.getColumns();

		// last npe position which was NOT 'BOTH';
		String lastNpePosition = null;

		for (Column column : columns) {
			String npePos = column.getStringVariable(NPE_POSITION_VAR);
			// only check if the column is both
			if (!this.isCombilineColumn(column)) {
				boolean offsetAdded = tryAddOffset(column);
				if (!offsetAdded) {
					tryDeleteColumn(column, lastNpePosition);
				}
			}
			// save last none BOTH position
			if (npePos != BOTH) {
				lastNpePosition = npePos;
			}

		}

	}

	private Switchboard getSwitchboard() {
		// TODO: FIXIT: this needs to chance as it is possible to have multiple
		// switchboards in the future
		// (model has switchboard holder with switchboards in link mesh)
		return new Switchboard((Component) configurator.getComponentFromToa(Switchboard.SWITCHBOARD_COMP, 0));
	}

	private boolean checkTriggerOffset(Column column) {
		boolean trigger = false;
		if (column.getType().equals(ColumnType.INLINE_RBBS) && ((column.getStringVariable(PREV_CONNECTION).equals(TOP)
				&& column.getStringVariable(TRIGGER_OFFSET).equals("0"))
				|| (column.getStringVariable(PREV_CONNECTION).equals(BOTTOM)
						&& column.getStringVariable(TRIGGER_OFFSET).equals("1")))) {
			trigger = true;
		}
		return trigger;
	}

	/**
	 * returns true if the column was deleted, false otherwise
	 */
	private boolean tryDeleteColumn(Column column, String lastNoneBothPos) {
		// a) if first or last column -> delete
		// b) if prev or next column == combiline -> delete
		// c) if next npe == BOTH -> delete
		// d) if last non both npe pos == next NPE pos -> delete
		String currentNpePos = column.getStringVariable(NPE_POSITION_VAR);

		if (currentNpePos == BOTH && !column.isFirst() && !column.isLast()
				&& ((column.getType().equals(ColumnType.INLINE_RBBS) && !checkTriggerOffset(column))
						|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
						|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT))
				&& column.getPrevious().getStringVariable(NPE_POSITION_VAR) == column.getNext()
						.getStringVariable(NPE_POSITION_VAR)
				&& (!isCombilineColumn(column.getPrevious()) || !isCombilineColumn(column.getNext()))) {
			if (column.getStringVariable(CONNECTION).equals(NONE)
					&& column.getNext().getStringVariable(NPE_POSITION_VAR).equals(TOP) && !checkIsSetByUser(column)) {
				column.setStringVariable(OFFSET, NO);
				column.setStringVariable(CONNECTION, TOP);
				return true;
			} else if (column.getStringVariable(CONNECTION).equals(NONE)
					&& column.getNext().getStringVariable(NPE_POSITION_VAR).equals(BOTTOM)
					&& !checkIsSetByUser(column)) {
				column.setStringVariable(OFFSET, NO);
				column.setStringVariable(CONNECTION, BOTTOM);
				return true;
			}
		}

		if (((column.getType().equals(ColumnType.INLINE_RBBS) && !checkTriggerOffset(column))
				|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
				|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)) && currentNpePos == BOTH
				&& column.isFirst() && column.getNext() != null
				&& column.getNext().getStringVariable(NPE_POSITION_VAR) == BOTH && !checkIsSetByUser(column)) {
			column.setStringVariable(OFFSET, NO);
			return true;
		} else if ((((column.getType().equals(ColumnType.INLINE_RBBS) && !checkTriggerOffset(column))
				&& (column.getStringVariable(DEVICE_CONNECTION) != "1"))
				|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
				|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)) && currentNpePos == BOTH && column.isLast()
				&& column.getPrevious() != null && column.getPrevious().getStringVariable(NPE_POSITION_VAR) == BOTH
				&& !checkIsSetByUser(column)) {
			column.setStringVariable(OFFSET, NO);
			return true;
		} else if ((column.getType().equals(ColumnType.INLINE_RBBS)
				|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
				|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)) && currentNpePos == BOTH
				&& (column.isLast() || column.isFirst())) {
			return true;
		} else if (!column.isLast()
				&& ((column.getType().equals(ColumnType.INLINE_RBBS)
						|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
						|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))
				&& currentNpePos == BOTH && column.getNext().getStringVariable(NPE_POSITION_VAR) == BOTH
				&& !checkIsSetByUser(column.getNext())) {
			column.getNext().setStringVariable(OFFSET, NO);
			return true;
		} else if (currentNpePos == BOTH && (column.isFirst() || column.isLast()
				|| isCombilineColumn(column.getPrevious()) || isCombilineColumn(column.getNext())
				|| column.getNext().getStringVariable(NPE_POSITION_VAR) == BOTH
				|| column.getPrevious().getStringVariable(NPE_POSITION_VAR) == column.getNext()
						.getStringVariable(NPE_POSITION_VAR)
				|| lastNoneBothPos == column.getNext().getStringVariable(NPE_POSITION_VAR))) {
			if (column.getType().equals(ColumnType.OFFSET)) {
				column.delete();
			}

			return true;
		}
		return false;
	}

	/**
	 * Try to add offset when required
	 * 
	 * @return
	 */
	private boolean tryAddOffset(Column column) {
		boolean offsetAdded = false;
		String currentNpePos = column.getStringVariable(NPE_POSITION_VAR);
		if (!column.isLast() && currentNpePos != BOTH) {
			Column next = column.getNext();
			String nextNpePos = next.getStringVariable(NPE_POSITION_VAR);
			if (!isCombilineColumn(next) && !nextNpePos.equalsIgnoreCase(BOTH)
					&& !nextNpePos.equalsIgnoreCase(currentNpePos)) {

				if (((column.getType().equals(ColumnType.INLINE_RBBS)
						|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
						|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))
						&& ((next.getType().equals(ColumnType.INLINE_RBBS)
								|| next.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
								|| next.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))) {
					offsetAdded = next.setStringVariable(OFFSET, YES);
					updateIsSetByCompleter(next);
				} else if (((column.getType().equals(ColumnType.INLINE_RBBS)
						|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
						|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))
						&& (!(next.getType().equals(ColumnType.INLINE_RBBS)
								|| next.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
								|| next.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))) {
					offsetAdded = column.setStringVariable(OFFSET, YES);
					updateIsSetByCompleter(column);
				} else if (!((column.getType().equals(ColumnType.INLINE_RBBS)
						|| column.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
						|| column.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))
						&& ((next.getType().equals(ColumnType.INLINE_RBBS)
								|| next.getType().equals(ColumnType.EXT_CABLE_COMPARTMENT_RBBS)
								|| next.getType().equals(ColumnType.CENTRALEARTHINGPOINT)))) {
					offsetAdded = next.setStringVariable(OFFSET, YES);
					updateIsSetByCompleter(next);
				} else {
					offsetAdded = column.insertAfter(ColumnType.OFFSET);
				}
			}
		}
		return offsetAdded;
	}

	/**
	 * Checks if the column is combiline column
	 * 
	 * @return
	 */
	private boolean isCombilineColumn(Column column) {
		return column.isColumnType(ColumnType.COMBILINE_RBBS);
	}

	/**
	 * Checks if the offset variable is user set or completer set
	 * 
	 * @return
	 */
	private boolean checkIsSetByUser(Column column) {
		boolean setByUser = false;
		String isSetByCompleter = column.getStringVariable(IS_SET_BY_COMPLETER);
		if (isSetByCompleter.equalsIgnoreCase("false") && column.checkVariableIsUserSet(OFFSET)) {
			setByUser = true;
		}
		return setByUser;
	}

	/**
	 * Updates the isSetByCompleter to true
	 * 
	 * @return
	 */
	private void updateIsSetByCompleter(Column column) {
		column.setStringVariable(IS_SET_BY_COMPLETER, "true");
	}

}
