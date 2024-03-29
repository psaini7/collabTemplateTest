/******************************************************************
 * ABBFuseSwitchAutoCompleter                                     *
 * Represents a Auto Completer class for insertion of 	          *
 * minimum number of blind panel in the empty slots for the 	  *
 * current column selected.          							  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.switchfuse;

import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class AbbSwitchFuseAutoCompleter  extends CompleterSkeleton {

    private Configuration configuration;
    private SwitchFuseAutoCompleterManager autoManager;
    
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

    // get configuration
    private Component initAutoManager() {
        configuration = getConfiguration();
        /* get the currently picked component (i.e. the component currently selected in Configurator) */
	    Component currentlyPickedComponent = ((World) configuration).getCurrentComponent();
	    autoManager = new SwitchFuseAutoCompleterManager();
	    return currentlyPickedComponent;
    }      
}
