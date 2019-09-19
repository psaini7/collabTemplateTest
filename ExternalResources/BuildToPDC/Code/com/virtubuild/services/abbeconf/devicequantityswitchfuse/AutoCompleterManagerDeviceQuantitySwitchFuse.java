/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityswitchfuse;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class AutoCompleterManagerDeviceQuantitySwitchFuse {
    
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( Component cmp,Configuration configurator ) {
    		AutoCompleterDeviceQuantitySwitchFuse c = new AutoCompleterDeviceQuantitySwitchFuse(cmp,configurator);
    		c.loadKits();
    		
    }

}
