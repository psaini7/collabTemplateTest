/******************************************************************
 * AutoCompleterManagerDeviceQuantity  							  *
 * Initiates Kits insertion on the all column rack                *
 * component based user input								 	  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.device;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;


public class DynamicDeviceQuantityManager {
     
    /** 
     * Initiates the method to calculate the kit insertion into the racks
     * @param cmp List of all the available Components 
     * @param arg0 
     */
    public void loadKits( Component cmp,Configuration configurator, BaseComponent arg0) {
    		DynamicDeviceQuantity c = new DynamicDeviceQuantity(cmp,configurator,arg0);
    		c.run();
    		
    }

}
