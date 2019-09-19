/******************************************************************
 * Auto Completer Manager   								 	  *
 * Initiates Kits insertion on the current column rack            *
 * component or based on all rack components in the configuration *
 ******************************************************************/
package com.virtubuild.services.abbeconf.blindpanels;

import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.services.abbeconf.blindpanels.BlindPanelAutoCompleter;

public class AutoCompleterManager {

	/**
	 * Calculates kits for the selected column
	 * 
	 * @param column column Component to calculate the frames on
	 */
	public void loadKits(Component column, Configuration config) {
		BlindPanelAutoCompleter c = new BlindPanelAutoCompleter(column, config);
		Component kitFramesTwinlineN = ((World) config).getCurrentComponent();
		if (kitFramesTwinlineN.getName().startsWith("kit_frames_twinlineN")) {
			c.loadKitsTwinline();
		} else if (kitFramesTwinlineN.getName().startsWith("kit_frames_n185_rbbs")) {
			c.loadKits();
		}
	}

}
