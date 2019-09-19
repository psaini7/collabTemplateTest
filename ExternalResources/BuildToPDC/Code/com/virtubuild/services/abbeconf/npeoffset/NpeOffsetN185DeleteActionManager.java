/******************************************************************
 * NpeOffsetN185DeleteAction                                	  *
 * Sets the visibility of the Yellow box to hidden on click 	  *
 * on delete button in BUild.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.npeoffset;

import com.virtubuild.core.api.Configuration;

public class NpeOffsetN185DeleteActionManager {

	/** 
	 * Initiates the method to calculate the kit insertion into the racks
	 * @param cmp List of all the available Components 
	 */
	public void loadKits(Configuration configurator ) {
		NpeOffsetN185DeleteAction c = new NpeOffsetN185DeleteAction(configurator);
		c.load();
	}

}

