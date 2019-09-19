/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityxlv;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class AutoCompleterManagerDeviceQuantityXlv {
    
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( Component cmp,Configuration configurator ) {
    		AutoCompleterDeviceQuantityXlv c = new AutoCompleterDeviceQuantityXlv(cmp,configurator);
    		c.loadKits();
    		
    }

 
}
