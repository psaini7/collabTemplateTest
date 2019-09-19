/******************************************************************
 * FuseSwitchAutoCompleterManager  								  *
 * Initiates Kits insertion on the current column rack            *
 * component or based on all rack components in the configuration *
 ******************************************************************/
package com.virtubuild.services.abbeconf.rbbsXlh;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class RbbsXlhAutoCompleterManager {
     
    /** 
     * Calculates kits for the selected column
     * @param column  column Component to calculate the frames on
     */
    public void loadKits( Component column,Configuration config ) {
    	RbbsXlhAutoCompleter c = new RbbsXlhAutoCompleter(column,config);
        c.loadKits();
    }

}
