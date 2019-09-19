/******************************************************************
 * NpeOffsetN185DeleteAction                                	  *
 * Sets the visibility of the Yellow box to hidden on click 	  *
 * on delete button in BUild.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.npeoffset;

import java.util.List;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class NpeOffsetN185DeleteAction {

	private Configuration configurator;
	private static final String SWITCHBOARD_TOA = "switchboard_n185";
	private static final String SWITCH_YELLOW_BOX = "switch_yellow_box";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public NpeOffsetN185DeleteAction(Configuration configurator) {
		this.configurator = configurator;
	}

	/**
	 * Initilize all the required methods
	 */
	public void load() {
		if (configurator.getComponentFromToa(SWITCHBOARD_TOA, 0) != null) {
			setYellowBoxVisibility();
		}
	}

	/**
	 * Gets all the component having TOA column
	 */
	private List<Component> getAllColumn() {
		return ((Component) configurator.getComponentFromToa(SWITCHBOARD_TOA, 0)).getLinkMesh();
	}
	
	/**
	 * Sets the visibility of the yellow box to hidden
	 */
	private void setYellowBoxVisibility() {
		List<Component> allColumns = getAllColumn();
		for (BaseComponent baseComponent : allColumns) {
			baseComponent.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("-1");
		}
	}
	

}
