/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityxlh;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class AutoCompleterManagerDeviceQuantityXlh {
     
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     */
    public void loadKits( Component cmp,Configuration configurator ) {
    		AutoCompleterDeviceQuantityXlh c = new AutoCompleterDeviceQuantityXlh(cmp,configurator);
    		c.loadKits();
    }

}
