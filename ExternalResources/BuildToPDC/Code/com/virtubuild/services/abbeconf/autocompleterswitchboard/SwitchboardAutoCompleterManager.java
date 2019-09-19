/******************************************************************
 * Auto Completer Manager Switchboard   						  *
 * Initiates Kits insertion on the all column rack                *
 * component based on all rack components in the configuration 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.autocompleterswitchboard;

import java.util.List;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class SwitchboardAutoCompleterManager {
     
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( List<Component> cmp,Configuration configurator ) {
    	SwitchboardAutoCompleter c = new SwitchboardAutoCompleter(cmp,configurator);
    	c.loadKits();
    }

}
