/******************************************************************
 * ABBAutoCompleterNPE                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.npeoffsetn185;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.custom.abbeconf.general.Column;
import com.virtubuild.custom.abbeconf.general.Switchboard;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

public class NpeOffsetN185Completer extends CompleterSkeleton {

	private static final Logger logger = LoggerFactory.getLogger(NpeOffsetN185Completer.class);

	private static final String SWITCHBOARD_N185_TOA = "switchboard_n185";
	private static final String DEFAULT_CONNECTION_DIRECTION = "bottom";

	private Configuration configuration;
	private NpeOffsetN185Manager manager;

	private int previousColumnCount = 0;
	private String previousConnectionDirection = "";

	private Map<String, String> previousColumnConnections = new HashMap<>();

	@Override
	public boolean doComplete() {
		initCompleter();
		if (canRun()) {
			logger.info("Running NPE Offset N185 completer.");
			manager.run();
			// update number of columns in case the manager created new columns
			previousColumnCount = getCurrentColumnCount();
		}
		return true;
	}

	@Override
	public boolean doCompleteSupported() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
		configuration = getConfiguration();
		previousConnectionDirection = DEFAULT_CONNECTION_DIRECTION;
	}

	private void initCompleter() {
		manager = new NpeOffsetN185Manager(configuration);
	}

	/**
	 * Check whether the completer should run and only run if specific criteria are
	 * fulfilled.
	 */
	private boolean canRun() {

		BaseComponent switchboard = getSwitchboard();

		// check whether it is the correct switchboard type and whether there are any columns to run on
		if (switchboard == null || !isN185(switchboard) || !hasColumns() ) return false;

		// check if criteria for running is fulfilled
		return columnCountChanged(switchboard) || switchboardConnectionDirectionChanged(switchboard)
				|| columnVariablesChanged(switchboard);
	}
	
	/**
	 * Checks if there are any columns in the configuration
	 * @return true if there are columns to run on, otherwise false
	 */
	private boolean hasColumns() {
        int currentColumnCount = getCurrentColumnCount();
        if (currentColumnCount > 0) {
            return true;
        }
        // reset previous column count in case user deleted the last column of the configuration
        previousColumnCount = 0;
        logger.info("No columns in current configuration.");
	    return false;
	}

	/**
	 * Check whether the number of columns has changed
	 */
	private boolean columnCountChanged(BaseComponent switchboard) {

		int currentColumnCount = getCurrentColumnCount();

		if (currentColumnCount != previousColumnCount) {
			logger.info("Column count has changed.");
			previousColumnCount = currentColumnCount;
			updatePreviousColumnConnectionList(switchboard);
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the connection direction has changed on switchboard level
	 */
	private boolean switchboardConnectionDirectionChanged(BaseComponent switchboard) {
		String currentConnectionDirection = getConnectionDirection(switchboard);
		if (!currentConnectionDirection.equalsIgnoreCase(previousConnectionDirection)) {
			logger.info("Switchboard connection direction has changed.");
			previousConnectionDirection = currentConnectionDirection;
			updatePreviousColumnConnectionList(switchboard);
			return true;
		}

		return false;
	}

	/**
	 * Gets the current number of columns in the configuration
	 */
	private int getCurrentColumnCount() {
		return configuration.getTOAComponents(Column.COLUMN_TOA).size();
	}

	/**
	 * gets the current switchboard
	 */
	private BaseComponent getSwitchboard() {
		List<BaseComponent> switchboards = configuration.getTOAComponents(Switchboard.SWITCHBOARD_TOA);
		if (!switchboards.isEmpty()) {
			return switchboards.get(0);
		}

		logger.info("No switchboards in current configuration.");
		return null;
	}

	/**
	 * checks whether the input BaseComponent is the correct switchboard
	 */
	private boolean isN185(BaseComponent switchboard) {
		return switchboard.isTypeOfActor(SWITCHBOARD_N185_TOA);
	}

	/**
	 * Checks whether any column has changed variables. Specific variables are
	 * checked
	 */
	private boolean columnVariablesChanged(BaseComponent switchboard) {
		List<Component> columns = getColumns(switchboard);

		for (Component column : columns) {
			// return as soon as a change is found. Continue with next column if none found
			if (columnConnectionDirectionChanged(column)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks that the direction connection has changed on column level
	 */
	private boolean columnConnectionDirectionChanged(Component column) {
		String id = column.getID();
		String connection = getConnectionDirection(column);

		if (!previousColumnConnections.containsKey(id)) {
			// add to previous connections map and assume no change in connection direction
			previousColumnConnections.put(id, connection);
		} else {
			// check if connection direction has changed
			String prevConnection = previousColumnConnections.get(id);
			if (!connection.equalsIgnoreCase(prevConnection)) {
				previousColumnConnections.replace(id, connection);
				logger.info("Column connection direction has changed.");
				return true;
			}
		}
		return false;
	}

	/**
	 * gets the connection direction for the input component
	 */
	private String getConnectionDirection(BaseComponent component) {
		String connection = null;
		if (component != null) {
			connection = VariableUtils.getStringValue((Component) component, Switchboard.CONNECTION_VAR);
		}
		return (connection != null) ? connection : DEFAULT_CONNECTION_DIRECTION;
	}

	/**
	 * updates the list of column connections to match the current columns
	 * 
	 * @param switchboard
	 */
	private void updatePreviousColumnConnectionList(BaseComponent switchboard) {
		List<Component> columns = ((Component) switchboard).getLinkMesh();
		previousColumnConnections.clear();
		for (Component column : columns) {
			previousColumnConnections.put(column.getID(), getConnectionDirection(column));
		}
	}

	/**
	 * Gets the columns for the switchboard
	 */
	private List<Component> getColumns(BaseComponent switchboard) {
		return ((Component) switchboard).getLinkMesh();

	}
}
