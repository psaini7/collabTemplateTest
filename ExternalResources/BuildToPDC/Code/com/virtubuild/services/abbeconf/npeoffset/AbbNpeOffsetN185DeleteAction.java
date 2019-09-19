/******************************************************************
 * NpeOffsetN185DeleteAction                                	  *
 * Sets the visibility of the Yellow box to hidden on click 	  *
 * on delete button in BUild.		  							  *
 ******************************************************************/
 /**@author Aagreet Sinha*/ 
package com.virtubuild.services.abbeconf.npeoffset;

import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class AbbNpeOffsetN185DeleteAction extends CompleterSkeleton {

	private Configuration configuration;
	private NpeOffsetN185DeleteActionManager autoManager;

	@Override
	public boolean doComplete() {
		initAutoManager();
		autoManager.loadKits(configuration);
		return true;
	}

	@Override
	public boolean doCompleteSupported() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
	}

	// get all components as a list
	private void initAutoManager() {
		configuration = getConfiguration();
		autoManager = new NpeOffsetN185DeleteActionManager();
	}

}
