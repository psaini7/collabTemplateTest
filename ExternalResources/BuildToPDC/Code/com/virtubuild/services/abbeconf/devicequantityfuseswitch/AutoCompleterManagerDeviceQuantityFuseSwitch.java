/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityfuseswitch;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class AutoCompleterManagerDeviceQuantityFuseSwitch {
     
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( Component cmp,Configuration configurator ) {
    		AutoCompleterDeviceQuantityFuseSwitch c = new AutoCompleterDeviceQuantityFuseSwitch(cmp,configurator);
    		c.loadKits();
    }

}
