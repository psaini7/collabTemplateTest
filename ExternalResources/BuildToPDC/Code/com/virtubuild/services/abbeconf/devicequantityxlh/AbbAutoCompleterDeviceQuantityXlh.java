/******************************************************************
 * ABBAutoCompleterDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.devicequantityxlh;


import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class AbbAutoCompleterDeviceQuantityXlh  extends CompleterSkeleton {

    private Configuration configuration;
    private AutoCompleterManagerDeviceQuantityXlh autoManager;
    
    @Override
	public boolean doComplete() {
    	Component cmp = initAutoManager();
    	autoManager.loadKits(cmp,configuration);
        return true;
    }
    
    @Override
    public boolean doCompleteSupported() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
    }

    // get all components as a list
    private Component initAutoManager() {
    	configuration = getConfiguration();
    	/* get the currently picked component (i.e. the component currently selected in Configurator) */
	    Component currentlyPickedComponent = ((World) configuration).getCurrentComponent();
	    autoManager = new AutoCompleterManagerDeviceQuantityXlh();
	    return currentlyPickedComponent;
    }      
}
