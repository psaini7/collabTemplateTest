package com.virtubuild.services.abbeconf.mbbsposition;

import com.virtubuild.core.api.Configuration;
import com.virtubuild.services.abbeconf.mbbsposition.MbbsN185MoveDownAutoCompleter;

public class MbbsN185MoveDownAutoCompleterManager {
	//private static final String SWITCHBOARD_TOA = "switchboard_n185";
	/** 
	 * Initiates the method to calculate the kit insertion into the racks
	 * @param cmp List of all the available Components 
	 */
	public void loadKits(Configuration configurator ) {
		//String mainbus = configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("main_busbar_position").getValue().toString();
		MbbsN185MoveDownAutoCompleter c = new MbbsN185MoveDownAutoCompleter(configurator);
		c.load();
	}

}

