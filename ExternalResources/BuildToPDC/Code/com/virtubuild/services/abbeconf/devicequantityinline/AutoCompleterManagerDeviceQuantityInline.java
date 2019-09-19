/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityinline;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class AutoCompleterManagerDeviceQuantityInline {
     
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( Component cmp,Configuration configurator ) {
    		AutoCompleterDeviceQuantityInline c = new AutoCompleterDeviceQuantityInline(cmp,configurator);
    		c.loadKits();
    }

}
