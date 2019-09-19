/******************************************************************
 * AbbDynamicDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.device;

import com.virtubuild.core.World;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.core.frame.FrameComplete;


public class AbbDynamicDeviceQuantity extends CompleterSkeleton implements FrameComplete{

    private Configuration configuration;
    private DynamicDeviceQuantityManager autoManager;
        
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
	    autoManager = new DynamicDeviceQuantityManager();
	    return currentlyPickedComponent;
    }

	@Override
	public boolean doComplete(BaseComponent arg0) {
		Component cmp = initAutoManager();
    	autoManager.loadKits(cmp,configuration,arg0);
        return true;
	}      
}


